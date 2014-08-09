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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import prefux.data.Tuple;
import prefux.data.tuple.DefaultTupleSet;
import prefux.data.tuple.TupleSet;
import prefux.util.StringLib;

/**
 * SearchTupleSet implementation that treats the query as a regular expression
 * to match against all indexed Tuple data fields.
 * The regular expression engine provided by the
 * standard Java libraries
 * ({@link java.util.regex.Pattern java.util.regex.Pattern}) is used; please
 * refer to the documentation for that class for more about the regular
 * expression syntax.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @see prefux.data.query.SearchQueryBinding
 */
public class RegexSearchTupleSet extends SearchTupleSet {
    
    private String m_query = "";
    private boolean m_caseSensitive;
    private LinkedHashMap m_source = new LinkedHashMap();
    
    /**
     * Create a new, case-insensitive regular expression search tuple set.
     */
    public RegexSearchTupleSet() {
        this(false);
    }
    
    /**
     * Create a new regular expression search tuple set.
     * @param caseSensitive true to make the indexing case sensitive, false
     * otherwise.
     */
    public RegexSearchTupleSet(boolean caseSensitive) {
        m_caseSensitive = caseSensitive;
    }
    
    /**
     * @see prefux.data.search.SearchTupleSet#getQuery()
     */
    public String getQuery() {
        return m_query;
    }

    /**
     * @see prefux.data.search.SearchTupleSet#search(java.lang.String)
     */
    public void search(String query) {
        if ( query == null )
            query = "";
        if ( !m_caseSensitive )
            query = query.toLowerCase();
        if ( query.equals(m_query) )
            return;
        
        Pattern pattern = null;
        try {
            pattern = Pattern.compile(query);
        } catch ( Exception e ) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.warning("Pattern compile failed."
                    + "\n" + StringLib.getStackTrace(e));
            return;
        }
        
        Tuple[] rem = clearInternal();    
        m_query = query;
        Iterator fields = m_source.keySet().iterator();
        while ( fields.hasNext() ) {
            String field = (String)fields.next();
            TupleSet ts = (TupleSet)m_source.get(field);
            
            Iterator tuples = ts.tuples();
            while ( tuples.hasNext() ) {
                Tuple t = (Tuple)tuples.next();
                String text = t.getString(field);
                if ( !m_caseSensitive )
                    text = text.toLowerCase();
                
                if ( pattern.matcher(text).matches() )
                    addInternal(t);
            }
        }
        Tuple[] add = getTupleCount() > 0 ? toArray() : null;
        fireTupleEvent(add, rem);
    }

    /**
     * @see prefux.data.search.SearchTupleSet#index(prefux.data.Tuple, java.lang.String)
     */
    public void index(Tuple t, String field) {
        TupleSet ts = (TupleSet)m_source.get(field);
        if ( ts == null ) {
            ts = new DefaultTupleSet();
            m_source.put(field, ts);
        }
        ts.addTuple(t);
    }

    /**
     * @see prefux.data.search.SearchTupleSet#unindex(prefux.data.Tuple, java.lang.String)
     */
    public void unindex(Tuple t, String field) {
        TupleSet ts = (TupleSet)m_source.get(field);
        if ( ts != null ) {
            ts.removeTuple(t);
        }
    }

    /**
     * Returns true, as unidexing is supported by this class.
     * @see prefux.data.search.SearchTupleSet#isUnindexSupported()
     */
    public boolean isUnindexSupported() {
        return true;
    }
    
    /**
     * Removes all search hits and clears out the index.
     * @see prefux.data.tuple.TupleSet#clear()
     */
    public void clear() {
        m_source.clear();
        super.clear();
    }

} // end of class RegexSearchTupleSet
