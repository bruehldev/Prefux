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

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Maintains a breadth-first-search queue as well as depth labels.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class Queue {

    // TODO: create an optimized implementation of this class
    
    private LinkedList m_list = new LinkedList();
    private HashMap    m_map  = new HashMap();
    
    public void clear() {
        m_list.clear();
        m_map.clear();
    }
    
    public boolean isEmpty() {
        return m_list.isEmpty();
    }
    
    public void add(Object o, int depth) {
        m_list.add(o);
        visit(o, depth);
    }
    
    public int getDepth(Object o) {
        Integer d = (Integer)m_map.get(o);
        return ( d==null ? -1 : d.intValue() );
    }
    
    public void visit(Object o, int depth) {
        m_map.put(o, new Integer(depth));
    }
    
    public Object removeFirst() {
        return m_list.removeFirst();
    }
    
    public Object removeLast() {
        return m_list.removeLast();
    }
    
} // end of class Queue
