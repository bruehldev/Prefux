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
/**
 * 
 */
package prefux.data.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import prefux.data.Tuple;

/**
 * Iterator that provides a sorted iteration over a set of tuples.
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class SortedTupleIterator implements Iterator<Tuple> {

    private ArrayList<Tuple> m_tuples;
    private Comparator<Tuple> m_cmp;
    private Iterator<Tuple> m_iter;
    
    /**
     * Create a new SortedTupleIterator that sorts tuples in the given
     * iterator using the given comparator.
     * @param iter the source iterator of tuples
     * @param c the comparator to use for sorting
     */
    public SortedTupleIterator(Iterator<? extends Tuple> iter, Comparator<? extends Tuple> c) {
        this(iter, 128, c);
    }
    
    /**
     * Create a new SortedTupleIterator that sorts tuples in the given
     * iterator using the given comparator.
     * @param iter the source iterator of tuples
     * @param size the expected number of tuples in the iterator
     * @param c the comparator to use for sorting
     */
    public SortedTupleIterator(Iterator<? extends Tuple> iter, int size, Comparator<? extends Tuple> c) {
        m_tuples = new ArrayList<>(size);
        init(iter, c);
    }
    
    /**
     * Initialize this iterator for the given source iterator and
     * comparator.
     * @param iter the source iterator of tuples
     * @param c the comparator to use for sorting
     */
    public void init(Iterator<? extends Tuple> iter, Comparator<? extends Tuple> c) {
        m_tuples.clear();
        m_cmp = (Comparator<Tuple>) c;
        
        // populate tuple list
        while ( iter.hasNext() ) {
            Tuple t = (Tuple)iter.next();
            m_tuples.add(t);
        }
        // sort tuple list
        Collections.sort(m_tuples, m_cmp);
        // create sorted iterator
        m_iter = m_tuples.iterator();
    }
    
    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return m_iter.hasNext();
    }

    /**
     * @see java.util.Iterator#next()
     */
    public Tuple next() {
        return m_iter.next();
    }

    /**
     * Throws an UnsupportedOperationException
     * @see java.util.Iterator#remove()
     * @throws UnsupportedOperationException
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

} // end of class SortedTupleIterator