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
import prefux.util.TypeLib;

/**
 * Literal expression of a numeric value. 
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class NumericLiteral extends Literal {

    private final Number m_number;
    private final Class  m_type;
    
    // ------------------------------------------------------------------------
    // Constructors
    
    /**
     * Create a new integer NumericLiteral.
     * @param x the literal numeric value
     */
    public NumericLiteral(int x) {
        m_number = new Integer(x);
        m_type = int.class;
    }

    /**
     * Create a new long NumericLiteral.
     * @param x the literal numeric value
     */
    public NumericLiteral(long x) {
        m_number = new Long(x);
        m_type = long.class;
    }
    
    /**
     * Create a new float NumericLiteral.
     * @param x the literal numeric value
     */
    public NumericLiteral(float x) {
        m_number = new Float(x);
        m_type = float.class;
    }
    
    /**
     * Create a new double NumericLiteral.
     * @param x the literal numeric value
     */
    public NumericLiteral(double x) {
        m_number = new Double(x);
        m_type = double.class;
    }
    
    /**
     * Create a new NumericLiteral.
     * @param x the literal numeric value, must be an instance of 
     * {@link java.lang.Number}, otherwise an exception will be thrown.
     */
    public NumericLiteral(Object x) {
        if ( x instanceof Number ) {
            m_number = (Number)x;
            m_type = TypeLib.getPrimitiveType(m_number.getClass());
        } else {
            throw new IllegalArgumentException("Invalid type!");
        }
    }

    // ------------------------------------------------------------------------
    // Expression Interface    
    
    /**
     * @see prefux.data.expression.Expression#getType(prefux.data.Schema)
     */
    public Class getType(Schema s) {
        return m_type;
    }

    /**
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        return m_number;
    }

    /**
     * @see prefux.data.expression.Expression#getInt(prefux.data.Tuple)
     */
    public int getInt(Tuple t) {
        return m_number.intValue();
    }

    /**
     * @see prefux.data.expression.Expression#getLong(prefux.data.Tuple)
     */
    public long getLong(Tuple t) {
        return m_number.longValue();
    }

    /**
     * @see prefux.data.expression.Expression#getFloat(prefux.data.Tuple)
     */
    public float getFloat(Tuple t) {
        return m_number.floatValue();
    }

    /**
     * @see prefux.data.expression.Expression#getDouble(prefux.data.Tuple)
     */
    public double getDouble(Tuple t) {
        return m_number.doubleValue();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return m_number.toString();
    }
    
} // end of class NumericLiteral
