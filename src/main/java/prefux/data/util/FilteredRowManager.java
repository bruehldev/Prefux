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
package prefux.data.util;

import prefux.data.CascadedTable;
import prefux.data.Table;
import prefux.data.column.IntColumn;
import prefux.util.collections.IntIntSortedMap;
import prefux.util.collections.IntIntTreeMap;

/**
 * RowManager that additionally manages mappings between the managed
 * rows and those of a parent table.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FilteredRowManager extends RowManager {

    protected IntColumn       m_childToParent;
    protected IntIntSortedMap m_parentToChild;
    
    /**
     * Create a new FilteredRowManager.
     * @param table the table to manage
     */
    public FilteredRowManager(Table table) {
        super(table);
        m_childToParent = new IntColumn(table.getRowCount());
        m_parentToChild = new IntIntTreeMap(false);
        clear();
    }
    
    /**
     * @see prefux.data.util.RowManager#clear()
     */
    public void clear() {
        super.clear();
        m_parentToChild.clear();
        for ( int i=0; i<m_childToParent.getRowCount(); ++i ) {
            m_childToParent.setInt(-1, i);
        }
    }
    
    /**
     * Add a new row backed by the given parent row.
     * @param parentRow the backing parent row
     * @return the index of the newly added row
     */
    public int addRow(int parentRow) {
        int r = super.addRow();
        put(r, parentRow);
        return r;
    }
    
    /**
     * @see prefux.data.util.RowManager#releaseRow(int)
     */
    public boolean releaseRow(int row) {
        if ( super.releaseRow(row) ) {
            remove(row);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @see prefux.data.util.RowManager#getColumnRow(int, int)
     */
    public int getColumnRow(int row, int col) {
        return ((CascadedTable)m_table).getParentTable()
                    .getColumnRow(getParentRow(row), col);
    }
    
    /**
     * @see prefux.data.util.RowManager#getTableRow(int, int)
     */
    public int getTableRow(int columnRow, int col) {
        return getChildRow(columnRow);
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Given a row managed by this manager, return the corresponding row
     * in the parent table.
     * @param childRow a row managed by this manager
     * @return the parent table row
     */
    public int getParentRow(int childRow) {
        if ( childRow >= m_childToParent.getRowCount() ) {
            return -1;
        } else {
            return m_childToParent.getInt(childRow);
        }
    }

    /**
     * Given a row in the parent table, return the corresponding row managed
     * by this manager.
     * @param parentRow a row in the parent table
     * @return the managed row corresponding to the parent row
     */
    public int getChildRow(int parentRow) {
        int val = m_parentToChild.get(parentRow);
        return ( val == Integer.MIN_VALUE ? -1 : val );
    }
    
    /**
     * Add a mapping between the given managed row and parent row.
     * @param childRow a row managed by this manager
     * @param parentRow a row in the parent table
     */
    public void put(int childRow, int parentRow) {
        // ensure capacity of IntColumn
        if ( childRow >= m_childToParent.getRowCount() )
            m_childToParent.setMaximumRow(childRow+1);
        
        // add mapping
        m_childToParent.setInt(parentRow, childRow);
        m_parentToChild.put(parentRow, childRow);
    }
    
    /**
     * Remove a mapping between the given managed row and the corresponding
     * parent row.
     * @param childRow a row managed by this manager
     */
    public void remove(int childRow) {
        int parentRow = m_childToParent.getInt(childRow);
        m_childToParent.setInt(-1, childRow);
        m_parentToChild.remove(parentRow);
    }

} // end of class FilteredRowManager
