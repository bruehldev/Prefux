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
import prefux.util.TypeLib;

/**
 * Abstarct base class for a Literal Expression that evaluates to a
 * constant value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class Literal extends AbstractExpression {

    /**
     * Evaluate the given tuple and data field and return the
     * result as a new Literal instance.
     * @param t the Tuple
     * @param field the data field to lookup
     * @return a new Literal expression containing the
     * value of the Tuple's data field
     */
    public static Literal getLiteral(Tuple t, String field) {
        Class type = t.getColumnType(field);
        if ( type == int.class )
        {
            return new NumericLiteral(t.getInt(field));
        }
        else if ( type == long.class )
        {
            return new NumericLiteral(t.getLong(field));
        }
        else if ( type == float.class )
        {
            return new NumericLiteral(t.getFloat(field));
        }
        else if ( type == double.class )
        {
            return new NumericLiteral(t.getDouble(field));
        }
        else if ( type == boolean.class )
        {
            return new BooleanLiteral(t.getBoolean(field));
        }
        else
        {
            return new ObjectLiteral(t.get(field));
        }
    }
    
    /**
     * Return the given object as a new Literal instance.
     * @param val the object value
     * @return a new Literal expression containing the
     * object value. The type is assumed to be the
     * value's concrete runtime type.
     */
    public static Literal getLiteral(Object val) {
        return getLiteral(val, val.getClass());
    }
    
    /**
     * Return the given object as a new Literal instance.
     * @param val the object value
     * @param type the type the literal should take
     * @return a new Literal expression containing the
     * object value
     */
    public static Literal getLiteral(Object val, Class type) {
        if ( TypeLib.isNumericType(type) )
        {
            return new NumericLiteral(val);
        }
        else if ( type == boolean.class )
        {
            return new BooleanLiteral(((Boolean)val).booleanValue());
        }
        else
        {
            if ( type.isInstance(val) ) {
                return new ObjectLiteral(val);
            } else {
                throw new IllegalArgumentException("Object does "
                        + "not match the provided Class type.");
            }
        }
    }
    
} // end of abstarct class Literal
