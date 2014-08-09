/*  
 * Copyright (c) 2004-2013 Regents of the University of California.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3.  Neither the name of the University nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * Copyright (c) 2014 Martin Stockhammer
 */

package prefux.data.search;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import prefux.data.Tuple;
import prefux.util.StringLib;
import prefux.util.collections.IntObjectHashMap;

/**
 * <p>
 * SearchTupleSet implementation that performs text searches on indexed Tuple
 * data using the Lucene search engine.
 * <a href="http://lucene.apache.org/">Lucene</a> is an open source search
 * engine supporting full text indexing and keyword search. Please refer to
 * the Lucene web page for more information. Note that for this class to be
 * used by prefux applications, the Lucene classes must be included on the
 * application classpath.
 * </p> 
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @see prefux.data.query.SearchQueryBinding
 */
public class KeywordSearchTupleSet extends SearchTupleSet {
    
    private static final Logger s_logger 
        = Logger.getLogger(KeywordSearchTupleSet.class.getName());
    
    protected IntObjectHashMap m_map = new IntObjectHashMap();
    protected String m_query = "";
    
    protected LuceneSearcher m_lucene = null;
    protected boolean m_storeTermVectors = false;
    
    protected int m_id = 1;
    
    /**
     * Creates a new KeywordSearchFocusSet using an in-memory search index.
     */
    public KeywordSearchTupleSet() {
        m_lucene = new LuceneSearcher();
    }
    
    /**
     * Creates a new TextSearchFocusSet with the given LuceneSearcher.
     * @param searcher the {@link LuceneSearcher} to use.
     */
    public KeywordSearchTupleSet(LuceneSearcher searcher) {
        m_lucene = searcher;
    }
    
    /**
     * Returns the current search query, if any.
     * @return the currently active search query
     */
    public String getQuery() {
        return m_query;
    }
    
    /**
     * Searches the indexed Tuple fields for matching keywords, using
     * the Lucene search engine. Matching Tuples are available as the
     * members of this TupleSet.
     * @param query the query string to search for
     */
    public void search(String query) {
        if ( query == null )
            query = "";
        
        if ( query.equals(m_query) )
            return; // no change
        
        Tuple[] rem = clearInternal();
        m_query = query;
        
        query.trim();
        if ( query.length() == 0 ) {
            this.fireTupleEvent(null, DELETE);
            return;
        }
        
        m_lucene.setReadMode(true);
        try {
            TopDocs hits = m_lucene.search(query);
            for ( ScoreDoc doc : hits.scoreDocs) {
                Tuple t = getMatchingTuple(m_lucene.getIndexSearcher().doc(doc.doc));
                addInternal(t);
            }
            Tuple[] add = getTupleCount() > 0 ? toArray() : null;
            fireTupleEvent(add, rem);
        } catch (ParseException e) {
            s_logger.warning("Lucene query parse exception.\n"+
                    StringLib.getStackTrace(e));
        } catch (IOException e) {
            s_logger.warning("Lucene IO exception.\n"+
                    StringLib.getStackTrace(e));
        }
        
    }
    
    /**
     * Return the Tuple matching the given Lucene Document, if any.
     * @param d the Document to lookup.
     * @return the matching Tuple, or null if none.
     */
    protected Tuple getMatchingTuple(Document d) {
        int id = Integer.parseInt(d.get(LuceneSearcher.ID));
        return (Tuple)m_map.get(id);
    }
    
    /**
     * @see prefux.data.search.SearchTupleSet#index(prefux.data.Tuple, java.lang.String)
     */
    public void index(Tuple t, String field) {
        m_lucene.setReadMode(false);
        String s;
        if ( (s=t.getString(field)) == null ) return;
        
        int id = m_id++;
        m_lucene.addDocument(getDocument(id, s));        
        m_map.put(id, t);
    }

    /**
     * Returns false, as unindexing values is not currently supported.
     * @see prefux.data.search.SearchTupleSet#isUnindexSupported()
     */
    public boolean isUnindexSupported() {
        return false;
    }
    
    /**
     * This method throws an exception, as unidexing is not supported.
     * @see prefux.data.search.SearchTupleSet#unindex(prefux.data.Tuple, java.lang.String)
     * @throws UnsupportedOperationException
     */
    public void unindex(Tuple t, String attrName) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Create a Lucene Document instance with the given document ID and text.
     * @param id the document ID
     * @param text the text the Document should contain
     * @return a new Lucene Document instance
     */
    protected Document getDocument(int id, String text) {
        Document d = new Document();
        d.add(new Field(LuceneSearcher.FIELD, text, Store.YES, Index.ANALYZED, TermVector.YES));
        d.add(new Field(LuceneSearcher.ID, String.valueOf(id),Store.YES, Index.ANALYZED, TermVector.YES));
        return d;
    }
    
    /**
     * Get the {@link LuceneSearcher} instance used by this class.
     * @return returns the backing lucene searcher.
     */
    public LuceneSearcher getLuceneSearcher() {
        return m_lucene;
    }
    
    /**
     * Returns a copy of the mapping from Lucene document IDs to prefux Tuple instances.
     * @return a copy of the map from lucene doc IDs to prefux Tuples.
     */
    public IntObjectHashMap getTupleMap() {
        return (IntObjectHashMap)m_map.clone();
    }
    
    /**
     * Removes all search hits and clears out the index.
     * @see prefux.data.tuple.TupleSet#clear()
     */
    public void clear() {
        m_lucene = new LuceneSearcher();
        super.clear();
    }
    
}  // end of class KeywordSearchTupleSet
