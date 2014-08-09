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

import prefux.data.Schema;
import prefux.data.Tuple;
import prefux.data.event.ExpressionListener;

/**
 * <p>An Expression is an arbitrary function that takes a single Tuple as an
 * argument. Expressions support both Object-valued and primitive-valued
 * (int, long, float, double, boolean) evaluation methods. The appropriate
 * method to call depends on the particular Expression implementation.
 * A {@link #getType(Schema)} method provides mechanism for determining the
 * return type of a given Expression instance. A {@link Predicate} is an
 * Expression which is guaranteed to support the {@link #getBoolean(Tuple)}
 * method, is often used to filter tuples.</p>
 * 
 * <p>Expressions also support a listener interface, allowing clients to
 * monitor changes to expressions, namely rearrangements or modification
 * of contained sub-expressions. The Expression interface also supports
 * visitors, which can be used to visit every sub-expression in an expression
 * tree.</p>
 * 
 * <p>Using the various Expression implementations in the
 * {@link prefux.data.expression.Expression} package, clients can
 * programatically construct a tree of expressions for use as complex
 * query predicates or as functions for computing a derived data column
 * (see {@link prefux.data.Table#addColumn(String, Expression)}. Often it is
 * more convenient to write expressions in the prefux expression language,
 * a SQL-like data manipulation language, and compile the Expression tree
 * using the {@link prefux.data.expression.parser.ExpressionParser}. The
 * documentation for the ExpressionParser class includes a full reference
 * for the textual expression language.</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface Expression {

    /**
     * Returns the type that this expression evaluates to when tuples
     * with the given Schema are provided as input.
     */
    public Class getType(Schema s);
    
    /**
     * Passes the visitor through this expression and any sub expressions
     * @param v the ExpressionVisitor
     */
    public void visit(ExpressionVisitor v);
    
    /**
     * Evaluate the Expression on the given input Tuple.
     * @param t the input Tuple
     * @return the Expression return value, as an Object
     */
    public Object get(Tuple t);
    
    /**
     * Evaluate the Expression on the given input Tuple.
     * @param t the input Tuple
     * @return the Expression return value, as an int
     */
    public int getInt(Tuple t);
    
    /**
     * Evaluate the Expression on the given input Tuple.
     * @param t the input Tuple
     * @return the Expression return value, as a long
     */
    public long getLong(Tuple t);
    
    /**
     * Evaluate the Expression on the given input Tuple.
     * @param t the input Tuple
     * @return the Expression return value, as a float
     */
    public float getFloat(Tuple t);

    /**
     * Evaluate the Expression on the given input Tuple.
     * @param t the input Tuple
     * @return the Expression return value, as a double
     */
    public double getDouble(Tuple t);

    /**
     * Evaluate the Expression on the given input Tuple.
     * @param t the input Tuple
     * @return the Expression return value, as a boolean
     */
    public boolean getBoolean(Tuple t);
    
    /**
     * Add a listener to this Expression.
     * @param lstnr the expression listener to add
     */
    public void addExpressionListener(ExpressionListener lstnr);
    
    /**
     * Remove a listener to this Expression.
     * @param lstnr the expression listener to remove
     */
    public void removeExpressionListener(ExpressionListener lstnr);
    
} // end of interface Expression
