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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

import prefux.data.DataReadOnlyException;
import prefux.data.DataTypeException;

/**
 * Column implementation for storing arbitrary Object values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ObjectColumn extends AbstractColumn {

    private Object[] m_values;
    private int      m_size;
    
    /**
     * Create a new empty ObjectColumn. The type is assumed to be Object.
     */
    public ObjectColumn() {
        this(Object.class);
    }
    
    /**
     * Create a new ObjectColumn.
     * @param type the data type of Objects in this column 
     */
    public ObjectColumn(Class type) {
        this(type, 0, 10, null);
    }
    
    /**
     * Create a new ObjectColumn. The type is assumed to be Object. 
     * @param nrows the initial size of the column
     */
    public ObjectColumn(int nrows) {
        this(Object.class, nrows, nrows, null);
    }
    
    /**
     * Create a new ObjectColumn.
     * @param type the data type of Objects in this column 
     * @param nrows the initial size of the column
     */
    public ObjectColumn(Class type, int nrows) {
        this(type, nrows, nrows, null);
    }
    
    /**
     * Create a new ObjectColumn.
     * @param type the data type of Objects in this column 
     * @param nrows the initial size of the column
     * @param capacity the initial capacity of the column
     * @param defaultValue the default value for the column. If this value
     * is cloneable, it will be cloned when assigned as defaultValue, otherwise
     * the input reference will be used for every default value.
     */
    public ObjectColumn(Class type, int nrows, int capacity, Object defaultValue) {
        super(type, defaultValue);
        if ( capacity < nrows ) {
            throw new IllegalArgumentException(
                "Capacity value can not be less than the row count.");
        }
        m_values = new Object[capacity];
        try {
            // since Object's clone method is protected, we default to
            // using reflection to create clones.
            Cloneable def = (Cloneable)defaultValue;
            Method m = def.getClass().getMethod("clone", (Class[])null);
            for ( int i=0; i<capacity; ++i ) {
                m_values[i] = m.invoke(m_defaultValue, (Object[])null);
            }
        } catch ( Exception e ) {
            if ( defaultValue != null ) {
                Logger.getLogger(getClass().getName()).fine(
                    "Default value of type \"" + 
                    defaultValue.getClass().getName() + "\" is not " +
                    "cloneable. Using Object reference directly.");
            }
            Arrays.fill(m_values, defaultValue);
        }
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
            Object[] values = new Object[capacity];
            System.arraycopy(m_values, 0, values, 0, m_size);
            try {
                // since Object's clone method is protected, we default to
                // using reflection to create clones.
                Cloneable def = (Cloneable)m_defaultValue;
                Method m = def.getClass().getMethod("clone", (Class[])null);
                for ( int i=m_size; i<capacity; ++i ) {
                    values[i] = m.invoke(m_defaultValue, (Object[])null);
                }
            } catch ( Exception e ) {
                Arrays.fill(values, m_size, capacity, m_defaultValue);
            }
            m_values = values;
        }
        m_size = nrows;
    }

    // ------------------------------------------------------------------------
    // Data Access Methods
    
    public void revertToDefault(int row) {
        try {
            // since Object's clone method is protected, we default to
            // using reflection to create clones.
            Cloneable def = (Cloneable)m_defaultValue;
            Method m = def.getClass().getMethod("clone", (Class[])null);
            set(m.invoke(m_defaultValue, (Object[])null), row);
        } catch ( Exception e ) {
            set(m_defaultValue, row);
        }
    }
    
    /**
     * Get the data value at the specified row
     * @param row the row from which to retrieve the value
     * @return the data value
     */
    public Object get(int row) {
        if ( row < 0 || row > m_size ) {
            throw new IllegalArgumentException(
                "Row index out of bounds: "+row);
        }
        return m_values[row];
    }
        
    /**
     * Set the data value at the specified row
     * @param val the value to set
     * @param row the row at which to set the value
     */
    public void set(Object val, int row) {
        if ( m_readOnly ) {
            throw new DataReadOnlyException();
        } else if ( row < 0 || row > m_size ) {
            throw new IllegalArgumentException(
                "Row index out of bounds: "+row);
        } else if ( val == null || canSet(val.getClass()) ) {
            // get the previous value
            Object prev = m_values[row];
            
            // exit early if no change
            // do we trust .equals() here? for now, no.
            if ( prev == val ) return;
            
            // set the new value
            m_values[row] = val;
            
            // fire a change event
            fireColumnEvent(row, prev);
        } else {
            throw new DataTypeException(val.getClass());
        }
    }
    
} // end of class ObjectColumn
