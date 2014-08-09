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
package prefux.data.util;

import java.util.NoSuchElementException;

import prefux.data.Table;
import prefux.data.expression.Predicate;
import prefux.util.collections.IntIterator;

/**
 * Iterator over table rows that filters the output by a given predicate. For
 * each table row, the corresponding tuple is checked against the predicate.
 * Only rows whose tuples pass the filter are included in this iteration.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FilterRowIterator extends IntIterator {
    
    private Predicate predicate;
    private IntIterator rows;
    private Table t;
    private int next;
    
    /**
     * Create a new FilterRowIterator.
     * @param rows an iterator over table rows
     * @param t the whos rows are being iterated over
     * @param p the filter predicate to use
     */
    public FilterRowIterator(IntIterator rows, Table t, Predicate p) {
        this.predicate = p;
        this.rows = rows;
        this.t = t;
        next = advance();
    }
    
    private int advance() {
        while ( rows.hasNext() ) {
            int r = rows.nextInt();
            if ( predicate.getBoolean(t.getTuple(r)) ) {
                return r;
            }
        }
        rows = null;
        next = -1;
        return -1;
    }

    /**
     * @see prefux.util.collections.LiteralIterator#nextInt()
     */
    public int nextInt() {
        if ( !hasNext() ) {
            throw new NoSuchElementException("No more elements");
        }
        int retval = next;
        next = advance();
        return retval;
    }
    
    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return ( rows != null );
    }
    
    /**
     * Not supported.
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
} // end of class FilterRowIterator
