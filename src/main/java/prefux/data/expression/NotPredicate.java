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

import prefux.data.Tuple;

/**
 * Predicate representing the negation of another predicate.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class NotPredicate extends AbstractPredicate {

    private Predicate m_predicate;
    
    /**
     * Create a new NotPredicate.
     * @param p the predicate to negate
     */
    public NotPredicate(Predicate p) {
        m_predicate = p;
    }
    
    /**
     * Get the negated predicate.
     * @return the negated predicate
     */
    public Predicate getPredicate() {
        return m_predicate;
    }
    
    /**
     * @see prefux.data.expression.Expression#getBoolean(prefux.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        return !m_predicate.getBoolean(t);
    }

    /**
     * @see prefux.data.expression.Expression#visit(prefux.data.expression.ExpressionVisitor)
     */
    public void visit(ExpressionVisitor v) {
        v.visitExpression(this);
        v.down();
        m_predicate.visit(v);
        v.up();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "NOT "+m_predicate.toString();
    }
    
    /**
     * @see prefux.data.expression.AbstractExpression#addChildListeners()
     */
    protected void addChildListeners() {
        m_predicate.addExpressionListener(this);
    }
    
    /**
     * @see prefux.data.expression.AbstractExpression#removeChildListeners()
     */
    protected void removeChildListeners() {
        m_predicate.removeExpressionListener(this);
    }
    
} // end of class NotPredicate