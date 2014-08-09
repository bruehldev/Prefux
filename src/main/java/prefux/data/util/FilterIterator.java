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

import java.util.Iterator;
import java.util.NoSuchElementException;

import prefux.data.Tuple;
import prefux.data.expression.Predicate;

/**
 * Iterator over tuples that filters the output by a given predicate.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FilterIterator<T extends Tuple> implements Iterator<T> {
    
    private Predicate predicate;
    private Iterator<T> tuples;
    private T next;
    
    /**
     * Create a new FilterIterator.
     * @param tuples an iterator over tuples
     * @param p the filter predicate to use
     */
    public FilterIterator(Iterator<T> tuples, Predicate p) {
        this.predicate = p;
        this.tuples = tuples;
        next = advance();
    }
    
    private T advance() {
        while ( tuples.hasNext() ) {
            T t = tuples.next();
            if ( predicate.getBoolean(t) ) {
                return t;
            }
        }
        tuples = null;
        next = null;
        return null;
    }

    /**
     * @see java.util.Iterator#next()
     */
    public T next() {
        if ( !hasNext() ) {
            throw new NoSuchElementException("No more elements");
        }
        T retval = next;
        next = advance();
        return retval;
    }
    
    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return ( tuples != null );
    }
    
    /**
     * Not supported.
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
} // end of class FilterIterator
