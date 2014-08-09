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
package prefux.util;

import prefux.data.Tuple;
import prefux.data.expression.Expression;
import prefux.data.expression.IfExpression;
import prefux.data.expression.ObjectLiteral;
import prefux.data.expression.Predicate;

/**
 * A chain of Predicates and associated values, maintain a large
 * if-statement structure for looking up values based on a Predicate
 * condition. 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class PredicateChain {
    
    private Expression m_head = new ObjectLiteral(null);
    private IfExpression m_tail = null;
    
    /**
     * Return the backing predicate chain as an Expression instance.
     * @return the predicate chain, either an IfExpression or
     * the single terminal default Expression instance.
     */
    public Expression getExpression() {
        return m_head;
    }
    
    /**
     * Evaluate the predicate chain for the given Tuple.
     * @param t the Tuple
     * @return the object associated with the first Predicate
     * that successfully matches the Tuple.
     */
    public Object get(Tuple t) {
        return m_head.get(t);
    }
    
    /**
     * Add a new rule to the end of the chain, associating a Predicate
     * condition with an Object value.
     * @param p the Predicate condition
     * @param val the associated Object value
     */
    public void add(Predicate p, Object val) {
        if ( m_tail == null ) {
            m_tail = new IfExpression(p, new ObjectLiteral(val), m_head);
            m_head = m_tail;
        } else {
            IfExpression ie = new IfExpression(p, new ObjectLiteral(val),
                                               m_tail.getElseExpression());
            m_tail.setElseExpression(ie);
            m_tail = ie;
        }
    }
    
    /**
     * Remove rules using the given predicate from this predicate chain.
     * This method will not remove rules in which this predicate is used
     * within a composite of clauses, such as an AND or OR. It only removes
     * rules using this predicate as the top-level trigger.
     * @param p the predicate to remove from the chain
     * @return true if a rule was successfully removed, false otherwise
     */
    public boolean remove(Predicate p) {
        if ( p == null ) return false;
        
        IfExpression prev = null;
        Expression expr = m_head;
        while ( expr instanceof IfExpression ) {
            IfExpression ifex = (IfExpression)expr;
            Predicate test = (Predicate)ifex.getTestPredicate();
            if ( p.equals(test) ) {
                Expression elseex = ifex.getElseExpression();
                ifex.setElseExpression(new ObjectLiteral(null));
                if ( prev != null ) {
                    prev.setElseExpression(elseex);
                    if ( ifex == m_tail )
                        m_tail = prev;
                } else {
                    m_head = elseex;
                    if ( ifex == m_tail )
                        m_tail = null;
                }
                return true;
            } else {
                prev = ifex;
                expr = ifex.getElseExpression();
            }
        }
        return false;
    }
    
    /**
     * Remove all rules from the predicate chain.
     */
    public void clear() {
        m_head = new ObjectLiteral(null);
        m_tail = null;
    }
    
} // end of class PredicateChain
