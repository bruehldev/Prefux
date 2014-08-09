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
package prefux.data.column;

import java.util.Arrays;

import prefux.data.DataReadOnlyException;
import prefux.data.DataTypeException;

/**
 * Column instance for sotring flaot values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FloatColumn extends AbstractColumn {

    private float[] m_values;
    private int     m_size;
    
    /**
     * Create a new empty FloatColumn. 
     */
    public FloatColumn() {
        this(0, 10, 0f);
    }
    
    /**
     * Create a new FloatColumn. 
     * @param nrows the initial size of the column
     */
    public FloatColumn(int nrows) {
        this(nrows, nrows, 0f);
    }
    
    /**
     * Create a new FloatColumn. 
     * @param nrows the initial size of the column
     * @param capacity the initial capacity of the column
     * @param defaultValue the default value for the column
     */
    public FloatColumn(int nrows, int capacity, float defaultValue) {
        super(float.class, new Float(defaultValue));
        if ( capacity < nrows ) {
            throw new IllegalArgumentException(
                "Capacity value can not be less than the row count.");
        }
        m_values = new float[capacity];
        Arrays.fill(m_values, defaultValue);
        m_size = nrows;
    }
    
    // ------------------------------------------------------------------------
    // Column Metadata
    
    /**
     * @see prefux.data.column.Column#getRowCount()
     */
    public int getRowCount() {
        return m_size;
    }
    
    /**
     * @see prefux.data.column.Column#setMaximumRow(int)
     */
    public void setMaximumRow(int nrows) {
        if ( nrows > m_values.length ) {
            int capacity = Math.max((3*m_values.length)/2 + 1, nrows);
            float[] values = new float[capacity];
            System.arraycopy(m_values, 0, values, 0, m_size);
            Arrays.fill(values, m_size, capacity,
                    ((Float)m_defaultValue).floatValue());
            m_values = values;
        }
        m_size = nrows;
    }

    // ------------------------------------------------------------------------
    // Data Access Methods
    
    /**
     * @see prefux.data.column.Column#get(int)
     */
    public Object get(int row) {
        return new Float(getFloat(row));
    }

    /**
     * @see prefux.data.column.Column#set(java.lang.Object, int)
     */
    public void set(Object val, int row) throws DataTypeException {
        if ( m_readOnly ) {
            throw new DataReadOnlyException();
        } else if ( val != null ) {
            if ( val instanceof Number ) {
                setFloat(((Number)val).floatValue(), row);
            } else if ( val instanceof String ) {
                setString((String)val, row);
            } else {
                throw new DataTypeException(val.getClass());
            }
        } else {
            throw new DataTypeException("Column does not accept null values");
        }
    }

    // ------------------------------------------------------------------------
    // Data Type Convenience Methods
    
    /**
     * @see prefux.data.column.AbstractColumn#getFloat(int)
     */
    public float getFloat(int row) throws DataTypeException {
        if ( row < 0 || row > m_size ) {
            throw new IllegalArgumentException("Row index out of bounds: "+row);
        }
        return m_values[row];
    }

    /**
     * @see prefux.data.column.AbstractColumn#setFloat(float, int)
     */
    public void setFloat(float val, int row) throws DataTypeException {
        if ( m_readOnly ) {
            throw new DataReadOnlyException();
        } else if ( row < 0 || row >= m_size ) {
            throw new IllegalArgumentException("Row index out of bounds: "+row);
        }
        // get the previous value
        float prev = m_values[row];
        
        // exit early if no change
        if ( prev == val ) return;
        
        // set the new value
        m_values[row] = val;
        
        // fire a change event
        fireColumnEvent(row, prev);
    }
    
//    /**
//     * @see prefux.data.column.AbstractColumn#getString(int)
//     */
//    public String getString(int row) throws DataTypeException {
//        return String.valueOf(getFloat(row));
//    }
//
//    /**
//     * @see prefux.data.column.AbstractColumn#setString(java.lang.String, int)
//     */
//    public void setString(String val, int row) throws DataTypeException {
//        setFloat(Float.parseFloat(val), row);
//    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.data.column.Column#getInt(int)
     */
    public int getInt(int row) throws DataTypeException {
        return (int)getFloat(row);
    }
    
    /**
     * @see prefux.data.column.Column#getLong(int)
     */
    public long getLong(int row) throws DataTypeException {
        return (long)getFloat(row);
    }
    
    /**
     * @see prefux.data.column.Column#getDouble(int)
     */
    public double getDouble(int row) throws DataTypeException {
        return getFloat(row);
    }

} // end of class FloatColumn
