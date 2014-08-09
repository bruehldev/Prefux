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

import prefux.data.DataTypeException;
import prefux.data.event.ColumnListener;

/**
 * Column implementation holding a single, constant value for all rows.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ConstantColumn extends AbstractColumn {

    private int m_size;

    /**
     * Create a new ConstantColumn.
     * @param type the data type of this column
     * @param defaultValue the default value used for all rows
     */
    public ConstantColumn(Class type, Object defaultValue) {
        super(type, defaultValue);
    }
    
    /**
     * @see prefux.data.column.Column#getRowCount()
     */
    public int getRowCount() {
        return m_size+1;
    }

    /**
     * @see prefux.data.column.Column#setMaximumRow(int)
     */
    public void setMaximumRow(int nrows) {
        m_size = nrows;
    }

    /**
     * @see prefux.data.column.Column#get(int)
     */
    public Object get(int row) {
        if ( row < 0 || row > m_size ) {
            throw new IllegalArgumentException("Row index out of bounds: "+row);
        }
        return super.m_defaultValue;
    }

    /**
     * Unsupported operation.
     * @see prefux.data.column.Column#set(java.lang.Object, int)
     */
    public void set(Object val, int row) throws DataTypeException {
        throw new UnsupportedOperationException(
                "Can't set values on a ConstantColumn");
    }

    /**
     * Returns false.
     * @see prefux.data.column.Column#canSet(java.lang.Class)
     */
    public boolean canSet(Class type) {
        return false;
    }    
    
    /**
     * Does nothing.
     * @see prefux.data.column.Column#addColumnListener(prefux.data.event.ColumnListener)
     */
    public void addColumnListener(ColumnListener listener) {
        return; // column can't change, so nothing to listen to
    }

    /**
     * Does nothing.
     * @see prefux.data.column.Column#removeColumnListener(prefux.data.event.ColumnListener)
     */
    public void removeColumnListener(ColumnListener listener) {
        return; // column can't change, so nothing to listen to
    }
    
} // end of class Constant Column
