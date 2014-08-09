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

/**
 * Expression instance that returns the value stored in a Tuple data field.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ColumnExpression extends AbstractExpression implements Predicate {

    protected final String m_field;
    
    /**
     * Create a new ColumnExpression.
     * @param field the column / data field name to use
     */
    public ColumnExpression(String field) {
        m_field = field;
    }
    
    /**
     * Get the column / data field name used by this expression.
     * @return the column / data field name
     */
    public String getColumnName() {
        return m_field;
    }
    
    // ------------------------------------------------------------------------
    // Expression Interface
    
    /**
     * @see prefux.data.expression.Expression#getType(prefux.data.Schema)
     */
    public Class getType(Schema s) {
        return s.getColumnType(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        return t.get(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getInt(prefux.data.Tuple)
     */
    public int getInt(Tuple t) {
        return t.getInt(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getLong(prefux.data.Tuple)
     */
    public long getLong(Tuple t) {
        return t.getLong(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getFloat(prefux.data.Tuple)
     */
    public float getFloat(Tuple t) {
        return t.getFloat(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getDouble(prefux.data.Tuple)
     */
    public double getDouble(Tuple t) {
        return t.getDouble(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getBoolean(prefux.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        return t.getBoolean(m_field);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "["+m_field+"]";
    }
    
} // end of class ColumnExpression
