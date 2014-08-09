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
package prefux.data.expression;

import java.util.Iterator;

import prefux.data.Tuple;

/**
 * Predicate representing an "xor" clause of sub-predicates.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class XorPredicate extends CompositePredicate {

    /**
     * Create an empty XorPredicate. Empty XorPredicates return false
     * by default.
     */
    public XorPredicate() {
    }
    
    /**
     * Create a new XorPredicate.
     * @param p1 the sole clause of this predicate
     */
    public XorPredicate(Predicate p1) {
        add(p1);
    }
    
    /**
     * Create a new XorPredicate.
     * @param p1 the first clause of this predicate
     * @param p2 the second clause of this predicate
     */
    public XorPredicate(Predicate p1, Predicate p2) {
        super(p1, p2);
    }
    
    /**
     * @see prefux.data.expression.Expression#getBoolean(prefux.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        if ( m_clauses.size() == 0 )
            return false;
        
        boolean val = false;
        Iterator iter = m_clauses.iterator();
        if ( iter.hasNext() ) {
            val = ((Predicate)iter.next()).getBoolean(t);
        }
        while ( iter.hasNext() ) {
            val ^= ((Predicate)iter.next()).getBoolean(t);
        }
        return val;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ( size() == 0 ? "FALSE" : toString("XOR") );
    }

} // end of class XorPredicate
