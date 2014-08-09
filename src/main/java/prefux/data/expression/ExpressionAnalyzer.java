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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Library class that computes some simple analyses of an expression. Each
 * analysis is computed using a visitor instance.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ExpressionAnalyzer {
    
    /**
     * Determine if an expression has a dependency on a data field.
     * @param expr the expression to analyze
     * @return true if the expression has at least one tuple data
     * field dependency.
     */
    public static boolean hasDependency(Expression expr) {
        ColumnCollector cc = new ColumnCollector(false);
        expr.visit(cc);
        return cc.getColumnCount() > 0;
    }
    
    /**
     * Get the set of data fields the expression is dependent upon.
     * @param expr the expression to analyze
     * @return a set of all data field names the expression references
     */
    public static Set getReferencedColumns(Expression expr) {
        ColumnCollector cc = new ColumnCollector(true);
        expr.visit(cc);
        return cc.getColumnSet();
    }
    
    /**
     * ExpressionVisitor that collects all referenced columns / data fields
     * in an Expression.
     */
    private static class ColumnCollector implements ExpressionVisitor {
        private boolean store;
        private Set m_cols;
        private int m_count;
        
        public ColumnCollector(boolean store) {
            this.store = store;
        }
        public int getColumnCount() {
            return m_count;
        }
        public Set getColumnSet() {
            if ( m_cols == null ) {
                return Collections.EMPTY_SET;
            } else {
                return m_cols;
            }
        }
        public void visitExpression(Expression expr) {
            if ( expr instanceof ColumnExpression ) {
                ++m_count;
                if ( store ) {
                    String field = ((ColumnExpression)expr).getColumnName();
                    if ( m_cols == null )
                        m_cols = new HashSet();
                    m_cols.add(field);
                }
                
            }
        }
        public void down() {
            // do nothing
        }
        public void up() {
            // do nothing
        }
    }
    
} // end of class ExpressionAnalyzer
