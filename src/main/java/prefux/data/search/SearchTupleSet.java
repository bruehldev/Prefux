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

import prefux.data.Tuple;
import prefux.data.tuple.DefaultTupleSet;

/**
 * <p>Abstract base class for TupleSet implementations that support text
 * search. These sets provide search engine functionality -- Tuple data fields
 * can be indexed and then searched over using text queries, the results of
 * which populate the TupleSet. A range of search techniques are provided by
 * subclasses of this class.</p>
 * 
 * <p>
 * <b>NOTE:</b> The {@link #addTuple(Tuple)} and
 * {@link #removeTuple(Tuple)}, methods are not supported by this 
 * implementation or its derived classes. Calling these methods will result
 * in thrown exceptions. Instead, membership is determined by the search
 * matches found using the {@link #search(String) search} method, which
 * searches over the terms indexed using the {@link #index(Iterator, String)}
 * and {@link #index(Tuple, String)} methods.
 * </p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @see prefux.data.query.SearchQueryBinding
 */
public abstract class SearchTupleSet extends DefaultTupleSet {
    
    /**
     * Returns the current search query, if any.
     * @return the currently active search query
     */
    public abstract String getQuery();
    
    /**
     * Searches the indexed fields of this TupleSet for matching
     * strings, adding the Tuple instances for each search match
     * to the TupleSet. The details of how the query is matched to
     * indexed fields is left to subclasses.
     * @param query the query string to search for. Indexed fields
     *  with matching text will be added to the TupleSet.
     */
    public abstract void search(String query);
    
    /**
     * Indexes the data values for the given field name for
     * each Tuple in the provided Iterator. These values are used
     * to construct an internal data structure allowing fast searches
     * over these attributes. To index multiple fields, simply call
     * this method multiple times with the desired field names.
     * @param tuples an Iterator over Tuple instances to index
     * @param field the name of the attribute to index
     * @throws ClassCastException is a non-Tuple instance is
     * encountered in the iteration.
     */
    public void index(Iterator tuples, String field) {
        while ( tuples.hasNext() ) {
            Tuple t = (Tuple)tuples.next();
            index(t, field);
        }
    }
    
    /**
     * Index an individual Tuple field, so that it can be searched for.
     * @param t the Tuple
     * @param field the data field to index
     */
    public abstract void index(Tuple t, String field);

    /**
     * Un-index an individual Tuple field, so that it can no longer be 
     * searched for.
     * @param t the Tuple
     * @param field the data field to unindex
     * @see #isUnindexSupported()
     */
    public abstract void unindex(Tuple t, String field);
    
    /**
     * Indicates if this TupleSearchSet supports the unindex operation.
     * @return true if unindex is supported, false otherwise.
     * @see #unindex(Tuple, String)
     */
    public abstract boolean isUnindexSupported();
    
    // ------------------------------------------------------------------------
    // Unsupported Operations
    
    /**
     * This method is not supported by this implementation. Don't call it!
     * Instead, use the {@link #search(String) search} or
     * {@link #clear() clear} methods.
     */
    public Tuple addTuple(Tuple t) {
        throw new UnsupportedOperationException();
    }
    /**
     * This method is not supported by this implementation. Don't call it!
     * Instead, use the {@link #search(String) search} or
     * {@link #clear() clear} methods.
     */
    public boolean removeTuple(Tuple t) {
        throw new UnsupportedOperationException();
    }
    
} // end of abstract class SearchTupleSet
