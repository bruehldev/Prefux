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

import java.util.BitSet;
import java.util.NoSuchElementException;

import prefux.util.collections.IntIterator;

/**
 * IntIterator over rows that ensures that no duplicates appear in the
 * iteration. Uses a bitset to note rows it has has seen and not pass along
 * duplicate row values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class UniqueRowIterator extends IntIterator {

    private IntIterator m_iter;
    private int m_next;
    private BitSet m_visited;
    
    /**
     * Create a new UniqueRowIterator.
     * @param iter a source iterator over table rows
     */
    public UniqueRowIterator(IntIterator iter) {
        m_iter = iter;
        m_visited = new BitSet();
        advance();
    }
    
    private void advance() {
        int r = -1;
        while ( r == -1 && m_iter.hasNext() ) {
            r = m_iter.nextInt();
            if ( visit(r) )
                r = -1;
        }
        m_next = r;
    }
    
    private boolean visit(int row) {
        if ( m_visited.get(row) ) {
            return true;
        } else {
            m_visited.set(row);
            return false;
        }
    }
    
    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return m_next != -1;
    }

    /**
     * @see prefux.util.collections.LiteralIterator#nextInt()
     */
    public int nextInt() {
        if ( m_next == -1 )
            throw new NoSuchElementException();
        int retval = m_next;
        advance();
        return retval;
    }
    
    /**
     * Not supported.
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

} // end of class UniqueRowIterator
