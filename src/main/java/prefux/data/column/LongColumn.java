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
 * Column implementation for storing long values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class LongColumn extends AbstractColumn {

    private long[] m_values;
    private int    m_size;
    
    /**
     * Create a new empty LongColumn. 
     */
    public LongColumn() {
        this(0, 10, 0L);
    }

    /**
     * Create a new LongColumn. 
     * @param nrows the initial size of the column
     */
    public LongColumn(int nrows) {
        this(nrows, nrows, 0L);
    }
    
    /**
     * Create a new LongColumn. 
     * @param nrows the initial size of the column
     * @param capacity the initial capacity of the column
     * @param defaultValue the default value for the column
     */
    public LongColumn(int nrows, int capacity, long defaultValue) {
        super(long.class, new Long(defaultValue));
        if ( capacity < nrows ) {
            throw new IllegalArgumentException(
                "Capacity value can not be less than the row count.");
        }
        m_values = new long[capacity];
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
            long[] values = new long[capacity];
            System.arraycopy(m_values, 0, values, 0, m_size);
            Arrays.fill(values, m_size, capacity,
                    ((Long)m_defaultValue).longValue());
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
        return new Long(getLong(row));
    }

    /**
     * @see prefux.data.column.Column#set(java.lang.Object, int)
     */
    public void set(Object val, int row) throws DataTypeException {
        if ( m_readOnly ) {
            throw new DataReadOnlyException();
        } else if ( val != null ) {
            if ( val instanceof Number ) {
                setLong(((Number)val).longValue(), row);
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
     * @see prefux.data.column.AbstractColumn#getLong(int)
     */
    public long getLong(int row) throws DataTypeException {
        if ( row < 0 || row > m_size ) {
            throw new IllegalArgumentException("Row index out of bounds: "+row);
        }
        return m_values[row];
    }

    /**
     * @see prefux.data.column.AbstractColumn#setLong(long, int)
     */
    public void setLong(long val, int row) throws DataTypeException {
        if ( m_readOnly ) {
            throw new DataReadOnlyException();
        } else if ( row < 0 || row >= m_size ) {
            throw new IllegalArgumentException("Row index out of bounds: "+row);
        }
        // get the previous value
        long prev = m_values[row];
        
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
//        return String.valueOf(getLong(row));
//    }
//
//    /**
//     * @see prefux.data.column.AbstractColumn#setString(java.lang.String, int)
//     */
//    public void setString(String val, int row) throws DataTypeException {
//        setLong(Long.parseLong(val), row);
//    }

    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.data.column.Column#getInt(int)
     */
    public int getInt(int row) throws DataTypeException {
        return (int)getLong(row);
    }
    
    /**
     * @see prefux.data.column.Column#getFloat(int)
     */
    public float getFloat(int row) throws DataTypeException {
        return getLong(row);
    }
    
    /**
     * @see prefux.data.column.Column#getDouble(int)
     */
    public double getDouble(int row) throws DataTypeException {
        return getLong(row);
    }
    
} // end of class LongColumn
