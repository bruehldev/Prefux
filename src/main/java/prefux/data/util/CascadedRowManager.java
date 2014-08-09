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

/**
 * RowManager instance that additionally takes into account tables which
 * inherit from a parent table but can also have their own, dedicated
 * columns.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class CascadedRowManager extends FilteredRowManager {
    
    /**
     * Create a new CascadedRowManager.
     * @param table the table to manage
     */
    public CascadedRowManager(Table table) {
        super(table);
    }
    
    /**
     * @see prefux.data.util.RowManager#getColumnRow(int, int)
     */
    public int getColumnRow(int row, int col) {
        if ( !isValidRow(row) )
            return -1;
        else if ( col >= ((CascadedTable)getTable()).getLocalColumnCount() )
            return ((CascadedTable)m_table).getParentTable()
                        .getColumnRow(getParentRow(row), col);
        else
            return row;
    }
    
    /**
     * @see prefux.data.util.RowManager#getTableRow(int, int)
     */
    public int getTableRow(int columnRow, int col) {
        int row;
        if ( col < ((CascadedTable)getTable()).getLocalColumnCount() ) {
            row = columnRow;
        } else {
            row = getChildRow(columnRow);
        }
        return isValidRow(row) ? row : -1;
    }
    
} // end of class CascadedRowManager
