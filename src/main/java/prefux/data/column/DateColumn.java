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
import java.util.Date;

import prefux.data.DataReadOnlyException;
import prefux.data.DataTypeException;
import prefux.util.TimeLib;

/**
 * Column implementation for storing Date values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DateColumn extends AbstractColumn {

    private long[] m_values;
    private int    m_size;
    
    /**
     * Create a new empty DateColumn. 
     */
    public DateColumn() {
        this(Date.class, 0, 10, 0L);
    }

    /**
     * Create a new DateColumn. 
     * @param nrows the initial size of the column
     */
    public DateColumn(int nrows) {
        this(Date.class, nrows, nrows, 0L);
    }
    
    /**
     * Create a new DateColumn.
     * @param type the exact data type (must be an instance or 
     * subclass of java.util.Date) 
     * @param nrows the initial size of the column
     */
    public DateColumn(Class type, int nrows) {
        this(type, nrows, nrows, 0L);
    }
    
    /**
     * Create a new DateColumn.
     * @param type the exact data type (must be an instance or 
     * subclass of java.util.Date)
     * @param nrows the initial size of the column
     * @param capacity the initial capacity of the column
     * @param defaultValue the default value for the column
     */
    public DateColumn(Class type, int nrows, int capacity, long defaultValue) {
        super(type, TimeLib.getDate(type, defaultValue));
        if ( !Date.class.isAssignableFrom(type) ) {
            throw new IllegalArgumentException("Column type must be an "
                + "instance or subclass of java.util.Date.");
        }
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
                    ((Date)m_defaultValue).getTime());
            m_values = values;
        }
        m_size = nrows;
    }
    
    /**
     * Indicates if the set method can be called without
     * an exception being thrown for the given type.
     * @param type the Class of the data type to check
     * @return true if the type is supported by this column, false otherwise
     */
    public boolean canSet(Class type) {
        if ( type == null ) return false;
        
        if ( Number.class.isAssignableFrom(type) ||
             String.class.isAssignableFrom(type) )
        {
            return true;
        } else {
            return m_columnType.isAssignableFrom(type);
        }
    }
    
    // ------------------------------------------------------------------------
    // Data Access Methods
    
    /**
     * @see prefux.data.column.Column#get(int)
     */
    public Object get(int row) {
        return TimeLib.getDate(m_columnType, getLong(row));
    }

    /**
     * @see prefux.data.column.Column#set(java.lang.Object, int)
     */
    public void set(Object val, int row) throws DataTypeException {
        if ( m_readOnly ) {
            throw new DataReadOnlyException();
        } else if ( val != null ) {
            if ( val instanceof Date ) {
                setLong(((Date)val).getTime(), row);
            } else if ( val instanceof Number ) {
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

    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.data.column.Column#getDouble(int)
     */
    public double getDouble(int row) throws DataTypeException {
        return getLong(row);
    }
    
} // end of class DateColumn
