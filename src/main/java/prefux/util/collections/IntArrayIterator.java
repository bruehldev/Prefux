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
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.util.collections;

import java.util.NoSuchElementException;

/**
 * IntIterator implementation that provides an iteration over the
 * contents of an int array.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class IntArrayIterator extends IntIterator {

    private int[] m_array;
    private int m_cur;
    private int m_end;
    
    public IntArrayIterator(int[] array, int start, int len) {
        m_array = array;
        m_cur = start;
        m_end = start+len;
    }
    
    /**
     * @see prefux.util.collections.IntIterator#nextInt()
     */
    public int nextInt() {
        if ( m_cur >= m_end )
            throw new NoSuchElementException();
        return m_array[m_cur++];
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return m_cur < m_end;
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }

} // end of class IntArrayIterator
