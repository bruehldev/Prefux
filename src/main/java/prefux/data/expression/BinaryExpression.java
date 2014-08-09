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

/**
 * Abstract base class for Expression implementations that maintain two
 * sub-expressions. These are referred to as the left expression (the first
 * one) and the right expression (the second one).
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class BinaryExpression extends AbstractExpression {

    protected int m_op;
    protected Expression m_left;
    protected Expression m_right;
    
    /**
     * Create a new BinaryExpression.
     * @param operation int value indicating the operation to be
     * performed on the subtrees. The actual range of acceptable values
     * are determined by subclasses.
     * @param minOp the minimum legal operation code value
     * @param maxOp the maximum legal operation code value
     * @param left the left sub-expression
     * @param right the right sub-expression
     */
    protected BinaryExpression(int operation, int minOp, int maxOp,
            Expression left, Expression right)
    {
        // operation check
        if ( operation < minOp || operation > maxOp ) {
            throw new IllegalArgumentException(
                "Unknown operation type: " + operation);
        }
        // null check;
        if ( left == null || right == null ) {
            throw new IllegalArgumentException(
                    "Expressions must be non-null.");
        }
        this.m_op = operation;
        this.m_left = left;
        this.m_right = right;
    }
    
    /**
     * Get the left sub-expression.
     * @return the left sub-expression
     */
    public Expression getLeftExpression() {
        return m_left;
    }

    /**
     * Get the right sub-expression.
     * @return the right sub-expression
     */
    public Expression getRightExpression() {
        return m_right;
    }
    
    /**
     * Set the left sub-expression. Any listeners will be notified.
     * @param e the left sub-expression to use
     */
    public void setLeftExpression(Expression e) {
        m_left.removeExpressionListener(this);
        m_left = e;
        if ( hasListeners() ) e.addExpressionListener(this);
        fireExpressionChange();
    }
    
    /**
     * Set the right sub-expression. Any listeners will be notified.
     * @param e the right sub-expression to use
     */
    public void setRightExpression(Expression e) {
        m_right.removeExpressionListener(this);
        m_right = e;
        if ( hasListeners() ) e.addExpressionListener(this);
        fireExpressionChange();
    }
    
    /**
     * Get the operation code for this expression. The meaning of this
     * code is left for subclasses to determine.
     * @return the operation code
     */
    public int getOperation() {
        return m_op;
    }
    
    /**
     * @see prefux.data.expression.Expression#visit(prefux.data.expression.ExpressionVisitor)
     */
    public void visit(ExpressionVisitor v) {
        v.visitExpression(this);
        v.down(); m_left.visit(v);  v.up();
        v.down(); m_right.visit(v); v.up();
    }
    
    /**
     * @see prefux.data.expression.AbstractExpression#addChildListeners()
     */
    protected void addChildListeners() {
        m_left.addExpressionListener(this);
        m_right.addExpressionListener(this);
    }
    
    /**
     * @see prefux.data.expression.AbstractExpression#removeChildListeners()
     */
    protected void removeChildListeners() {
        m_left.removeExpressionListener(this);
        m_right.removeExpressionListener(this);
    }

} // end of abstract class BinaryExpression
