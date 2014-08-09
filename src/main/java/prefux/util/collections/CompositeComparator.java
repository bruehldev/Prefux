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

import java.util.Comparator;

/**
 * Comparator that makes comparison using an ordered list of
 * individual comparators;
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class CompositeComparator<T> implements Comparator<T> {

    private static final int INCREMENT = 2;
    private Comparator<T>[] m_cmp;
    private int m_rev = 1;
    private int m_size = 0;

    /**
     * Creates an empty CompositeComparator with the given capacity.
     * @param size the starting capacity of this comparator
     */
    public CompositeComparator(int size) {
        this(size, false);
    }
    
    /**
     * Creates an empty CompositeComparator with the given capacity.
     * @param size the starting capacity of this comparator
     * @param reverse when true, reverses the sort order of the included
     * comparators, when false, objects are sorted as usual
     */
    @SuppressWarnings("unchecked")
	public CompositeComparator(int size, boolean reverse) {
        m_cmp = new Comparator[size];
        m_rev = reverse ? -1 : 1;
    }
    
    /**
     * Creates a new CompositeComparator.
     * @param cmp the constituent comparators of this composite
     */
    public CompositeComparator(Comparator<T>[] cmp) {
        this(cmp, false);
    }
    
    /**
     * Creates a new CompositeComparator.
     * @param cmp the constituent comparators of this composite
     * @param reverse when true, reverses the sort order of the included
     * comparators, when false, objects are sorted as usual
     */
    public CompositeComparator(Comparator<T>[] cmp, boolean reverse) {
        this(cmp.length, reverse);
        System.arraycopy(cmp, 0, m_cmp, 0, cmp.length);
        m_size = cmp.length;
    }
    
    /**
     * Adds an additional comparator to this composite.
     * @param c the Comparator to add
     */
    public void add(Comparator<T> c) {
        if ( c == null ) return;
        if ( m_cmp.length == m_size ) {
            @SuppressWarnings("unchecked")
			Comparator<T>[] cmp = new Comparator[m_size+INCREMENT];
            System.arraycopy(m_cmp, 0, cmp, 0, m_size);
            m_cmp = cmp;
        }
        m_cmp[m_size++] = c;
    }
    
    /**
     * Removes a comparator from this composite.
     * @param c the Comparator to remove
     * @return true if the comparator was successfully removed,
     * false otherwise
     */
    public boolean remove(Comparator<T> c) {
        for ( int i=0; i<m_size; ++i ) {
            if ( m_cmp[i].equals(c) ) {
                System.arraycopy(m_cmp, i+1, m_cmp, i, m_size-i);
                --m_size;
                return true;
            }
        }
        return false;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(T o1, T o2) {
        for ( int i=0; i<m_cmp.length; ++i ) {
            int c = m_cmp[i].compare(o1, o2);
            if ( c != 0 ) {
                return m_rev*c;
            }
        }
        return 0;
    }

} // end of class CompositeComparator
