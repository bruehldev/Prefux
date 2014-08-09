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
package prefux.util.collections;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A resizable array that maintains a list of byte values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ByteArrayList {

    private byte[] m_bytes;
    private int m_size;
    
    public ByteArrayList() {
        this(4096);
    }
    
    public ByteArrayList(int capacity) {
        m_bytes = new byte[capacity];
        m_size = 0;
    }
    
    private void rangeCheck(int i) {
        if ( i < 0 || i >= m_size ) 
            throw new IndexOutOfBoundsException(
                    "Index: "+i+" Size: " + m_size);
    }
    
    private void ensureCapacity(int cap) {
        if ( m_bytes.length < cap ) {
            int capacity = Math.max((3*m_bytes.length)/2 + 1, cap);
            byte[] nbytes = new byte[capacity];
            System.arraycopy(m_bytes, 0, nbytes, 0, m_size);
            m_bytes = nbytes;
        }
    }
    
    public byte get(int i) {
        rangeCheck(i);
        return m_bytes[i];
    }
    
    public void set(int i, byte b) {
        rangeCheck(i);
        m_bytes[i] = b;
    }
    
    public int size() {
        return m_size;
    }
    
    public void add(byte b) {
        ensureCapacity(m_size+1);
        m_bytes[m_size++] = b;
    }
    
    public void add(byte[] b, int start, int len) {
        ensureCapacity(m_size+len);
        System.arraycopy(b,start,m_bytes,m_size,len);
        m_size += len;
    }
    
    public InputStream getAsInputStream() {
        return new ByteArrayInputStream(m_bytes,0,m_size);
    }
    
    public byte[] toArray() {
        byte[] b = new byte[m_size];
        System.arraycopy(m_bytes,0,b,0,m_size);
        return b;
    }
}
