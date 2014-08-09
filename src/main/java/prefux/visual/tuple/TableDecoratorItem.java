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
package prefux.visual.tuple;

import prefux.data.Graph;
import prefux.visual.DecoratorItem;
import prefux.visual.VisualItem;
import prefux.visual.VisualTable;

/**
 * DecoratorItem implementation that uses data values from a backing
 * VisualTable.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TableDecoratorItem extends TableVisualItem 
    implements DecoratorItem
{   
    /**
     * Initialize a new TableDecoratorItem for the given table and row. This
     * method is used by the appropriate TupleManager instance, and should
     * not be called directly by client code, unless by a client-supplied
     * custom TupleManager.
     * @param table the data Table
     * @param graph ignored by this class
     * @param row the table row index
     */
    protected void init(VisualTable table, Graph graph, int row) {
        m_table = table;
        m_row = m_table.isValidRow(row) ? row : -1;
    }
    
    /**
     * @see prefux.visual.DecoratorItem#getDecoratedItem()
     */
    public VisualItem getDecoratedItem() {
        VisualTable vt = (VisualTable)getTable();
        int prow = vt.getParentRow(getRow());
        return (VisualItem)vt.getParentTable().getTuple(prow);
    }

} // end of class TableDecoratorItem
