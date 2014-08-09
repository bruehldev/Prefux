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

import java.util.Comparator;

import prefux.data.Table;
import prefux.data.column.Column;
import prefux.data.event.ColumnListener;
import prefux.data.event.EventConstants;
import prefux.data.event.TableListener;
import prefux.util.collections.BooleanIntSortedMap;
import prefux.util.collections.DoubleIntSortedMap;
import prefux.util.collections.FloatIntSortedMap;
import prefux.util.collections.IncompatibleComparatorException;
import prefux.util.collections.IntIntSortedMap;
import prefux.util.collections.IntIterator;
import prefux.util.collections.IntSortedMap;
import prefux.util.collections.LongIntSortedMap;
import prefux.util.collections.ObjectIntSortedMap;
import prefux.util.collections.SortedMapFactory;

/**
 * Index instance that uses red-black trees to provide an index
 * over a column of data.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TreeIndex implements Index, ColumnListener, TableListener {

    protected Table           m_table;
    protected RowManager      m_rows;
    protected Column          m_col;
    protected IntSortedMap    m_index;
    protected boolean         m_reindex;
    protected int             m_colidx;
    
    /**
     * Create a new TreeIndex.
     * @param t the Table containing the data column to index
     * @param rows the RowManager of the Table
     * @param col the Column instance to index
     * @param cmp the Comparator to use to sort data values
     * @throws IncompatibleComparatorException if the comparator is not
     * compatible with the column's data type
     */
    public TreeIndex(Table t, RowManager rows, Column col, Comparator cmp)
        throws IncompatibleComparatorException
    {
        m_table = t;
        m_rows = rows;
        m_col = col;
        
        m_index = SortedMapFactory.getMap(col.getColumnType(), cmp, false);
        index();
        
        m_col.addColumnListener(this);
        m_table.addTableListener(this);
    }
    
    /**
     * @see prefux.data.util.Index#dispose()
     */
    public void dispose() {
        m_col.removeColumnListener(this);
        m_table.removeTableListener(this);
    }
    
    /**
     * @see prefux.data.util.Index#getComparator()
     */
    public Comparator getComparator() {
        return m_index.comparator();
    }
    
    /**
     * @see prefux.data.util.Index#size()
     */
    public int size() {
        return m_index.size();
    }
    
    private int getColumnIndex() {
        if ( !(m_table.getColumn(m_colidx) == m_col) ) {
            m_colidx = m_table.getColumnNumber(m_col);
        }
        return m_colidx;
    }
    
    // ------------------------------------------------------------------------
    // Index Update Methods

    /**
     * @see prefux.data.util.Index#index()
     */
    public void index() {
        m_index.clear();
 
        // iterate over all valid values, adding them to the index
        int idx = getColumnIndex();
        m_colidx = idx;
        IntIterator rows = m_rows.rows();
        
        if ( m_index instanceof IntIntSortedMap )
        {
            IntIntSortedMap map = (IntIntSortedMap)m_index;
            while ( rows.hasNext() ) {
                int r = rows.nextInt();
                map.put(m_col.getInt(m_table.getColumnRow(r,idx)), r);
            }
        }
        else if ( m_index instanceof LongIntSortedMap )
        {
            LongIntSortedMap map = (LongIntSortedMap)m_index;
            while ( rows.hasNext() ) {
                int r = rows.nextInt();
                map.put(m_col.getLong(m_table.getColumnRow(r,idx)), r);
            }
        }
        else if ( m_index instanceof FloatIntSortedMap )
        {
            FloatIntSortedMap map = (FloatIntSortedMap)m_index;
            while ( rows.hasNext() ) {
                int r = rows.nextInt();
                map.put(m_col.getFloat(m_table.getColumnRow(r,idx)), r);
            }
        }
        else if ( m_index instanceof DoubleIntSortedMap )
        {
            DoubleIntSortedMap map = (DoubleIntSortedMap)m_index;
            while ( rows.hasNext() ) {
                int r = rows.nextInt();
                map.put(m_col.getDouble(m_table.getColumnRow(r,idx)), r);
            }
        }
        else if ( m_index instanceof BooleanIntSortedMap )
        {
            BooleanIntSortedMap map = (BooleanIntSortedMap)m_index;
            while ( rows.hasNext() ) {
                int r = rows.nextInt();
                map.put(m_col.getBoolean(m_table.getColumnRow(r,idx)), r);
            }
        }
        else if ( m_index instanceof ObjectIntSortedMap )
        {
            ObjectIntSortedMap map = (ObjectIntSortedMap)m_index;
            while ( rows.hasNext() ) {
                int r = rows.nextInt();
                map.put(m_col.get(m_table.getColumnRow(r,idx)), r);
            }
        }
        else {
            throw new IllegalStateException();
        }
        
        m_reindex = false;
    }

    // ------------------------------------------------------------------------
    // Listener Methods
    
    /**
     * @see prefux.data.event.TableListener#tableChanged(prefux.data.Table, int, int, int, int)
     */
    public void tableChanged(Table t, int start, int end, int col, int type) {
        if ( type == EventConstants.UPDATE || t != m_table 
              || col != EventConstants.ALL_COLUMNS )
            return;
        
        boolean insert = (type==EventConstants.INSERT);
        for ( int r=start; r<=end; ++r )
            rowChanged(r, insert);
    }
    
    private void rowChanged(int row, boolean insert) {
        // make sure we access the right column value
        int crow = m_rows.getColumnRow(row, getColumnIndex());
        
        if ( m_index instanceof IntIntSortedMap )
        {
            IntIntSortedMap map = (IntIntSortedMap)m_index;
            int key = m_col.getInt(row);
            if ( insert )
                map.put(key, row);
            else
                map.remove(key, row);
        }
        else if ( m_index instanceof LongIntSortedMap )
        {
            LongIntSortedMap map = (LongIntSortedMap)m_index;
            long key = m_col.getLong(crow);
            if ( insert )
                map.put(key, row);
            else
                map.remove(key, row);
        }
        else if ( m_index instanceof FloatIntSortedMap )
        {
            FloatIntSortedMap map = (FloatIntSortedMap)m_index;
            float key = m_col.getFloat(crow);
            if ( insert )
                map.put(key, row);
            else
                map.remove(key, row);
        }
        else if ( m_index instanceof DoubleIntSortedMap )
        {
            DoubleIntSortedMap map = (DoubleIntSortedMap)m_index;
            double key = m_col.getDouble(crow);
            if ( insert )
                map.put(key, row);
            else
                map.remove(key, row);
        }
        else if ( m_index instanceof BooleanIntSortedMap )
        {
            BooleanIntSortedMap map = (BooleanIntSortedMap)m_index;
            boolean key = m_col.getBoolean(crow);
            if ( insert )
                map.put(key, row);
            else
                map.remove(key, row);
        }
        else if ( m_index instanceof ObjectIntSortedMap )
        {
            ObjectIntSortedMap map = (ObjectIntSortedMap)m_index;
            Object key = m_col.get(crow);
            if ( insert )
                map.put(key, row);
            else
                map.remove(key, row);
        }
        else {
            throw new IllegalStateException();
        }        
    }

    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, int, int)
     */
    public void columnChanged(Column src, int type, int start, int end) {
        m_reindex = true;
    }    
    
    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, boolean)
     */
    public void columnChanged(Column src, int idx, boolean prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if ( row < 0 ) return; // invalid row value
        ((BooleanIntSortedMap)m_index).remove(prev, row);
        ((BooleanIntSortedMap)m_index).put(src.getBoolean(idx), row);
    }

    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, int)
     */
    public void columnChanged(Column src, int idx, int prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if ( row < 0 ) return; // invalid row value
        ((IntIntSortedMap)m_index).remove(prev, row);
        ((IntIntSortedMap)m_index).put(src.getInt(idx), row);
    }
    
    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, long)
     */
    public void columnChanged(Column src, int idx, long prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if ( row < 0 ) return; // invalid row value
        ((LongIntSortedMap)m_index).remove(prev, row);
        ((LongIntSortedMap)m_index).put(src.getLong(idx), row);
    }
    
    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, float)
     */
    public void columnChanged(Column src, int idx, float prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if ( row < 0 ) return; // invalid row value
        ((FloatIntSortedMap)m_index).remove(prev, row);
        ((FloatIntSortedMap)m_index).put(src.getFloat(idx), row);
    }
    
    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, double)
     */
    public void columnChanged(Column src, int idx, double prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if ( row < 0 ) return; // invalid row value
        ((DoubleIntSortedMap)m_index).remove(prev, row);
        ((DoubleIntSortedMap)m_index).put(src.getDouble(idx), row);
    }

    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, java.lang.Object)
     */
    public void columnChanged(Column src, int idx, Object prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if ( row < 0 ) return; // invalid row value
        ((ObjectIntSortedMap)m_index).remove(prev, row);
        ((ObjectIntSortedMap)m_index).put(src.get(idx), row);
    }

    // ------------------------------------------------------------------------
    // Retrieval Methods
    
    /**
     * @see prefux.data.util.Index#minimum()
     */
    public int minimum() {
        return m_index.getMinimum();
    }
    
    /**
     * @see prefux.data.util.Index#maximum()
     */
    public int maximum() {
        return m_index.getMaximum();
    }
    
    /**
     * @see prefux.data.util.Index#median()
     */
    public int median() {
        return m_index.getMedian();
    }
    
    /**
     * @see prefux.data.util.Index#uniqueCount()
     */
    public int uniqueCount() {
        return m_index.getUniqueCount();
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.data.util.Index#allRows(int)
     */
    public IntIterator allRows(int type) {
        boolean ascending = (type & Index.TYPE_ASCENDING) > 0;
        return m_index.valueIterator(ascending);
    }
    
    /**
     * @see prefux.data.util.Index#rows(java.lang.Object, java.lang.Object, int)
     */
    public IntIterator rows(Object lo, Object hi, int type) {
        if ( !(m_index instanceof ObjectIntSortedMap) )
            throw new IllegalStateException();

        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        
        if ( lo == null ) lo = ObjectIntSortedMap.MIN_KEY;
        if ( hi == null ) hi = ObjectIntSortedMap.MAX_KEY;
        
        ObjectIntSortedMap index = (ObjectIntSortedMap)m_index;
        if ( reverse ) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }
    
    /**
     * @see prefux.data.util.Index#rows(int, int, int)
     */
    public IntIterator rows(int lo, int hi, int type) {
        if ( !(m_index instanceof IntIntSortedMap) )
            throw new IllegalStateException();

        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        
        IntIntSortedMap index = (IntIntSortedMap)m_index;
        if ( reverse ) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }
    
    /**
     * @see prefux.data.util.Index#rows(long, long, int)
     */
    public IntIterator rows(long lo, long hi, int type) {
        if ( !(m_index instanceof LongIntSortedMap) )
            throw new IllegalStateException();
        
        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        
        LongIntSortedMap index = (LongIntSortedMap)m_index;
        if ( reverse ) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }
    
    /**
     * @see prefux.data.util.Index#rows(float, float, int)
     */
    public IntIterator rows(float lo, float hi, int type) {
        if ( !(m_index instanceof FloatIntSortedMap) )
            throw new IllegalStateException();
        
        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        
        FloatIntSortedMap index = (FloatIntSortedMap)m_index;
        if ( reverse ) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }
    
    /**
     * @see prefux.data.util.Index#rows(double, double, int)
     */
    public IntIterator rows(double lo, double hi, int type) {
        if ( !(m_index instanceof DoubleIntSortedMap) )
            throw new IllegalStateException();
        
        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        
        DoubleIntSortedMap index = (DoubleIntSortedMap)m_index;
        if ( reverse ) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }

    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.data.util.Index#rows(int)
     */
    public IntIterator rows(int val) {
        return rows(val, val, Index.TYPE_AII);
    }
    
    /**
     * @see prefux.data.util.Index#rows(long)
     */
    public IntIterator rows(long val) {
        return rows(val, val, Index.TYPE_AII);
    }
    
    /**
     * @see prefux.data.util.Index#rows(float)
     */
    public IntIterator rows(float val) {
        return rows(val, val, Index.TYPE_AII);
    }
    
    /**
     * @see prefux.data.util.Index#rows(double)
     */
    public IntIterator rows(double val) {
        return rows(val, val, Index.TYPE_AII);
    }
    
    /**
     * @see prefux.data.util.Index#rows(boolean)
     */
    public IntIterator rows(boolean val) {
        if ( !(m_index instanceof BooleanIntSortedMap) )
            throw new IllegalStateException();
        
        BooleanIntSortedMap index = (BooleanIntSortedMap)m_index;
        return index.valueRangeIterator(val, true, val, true);
    }
    
    /**
     * @see prefux.data.util.Index#rows(java.lang.Object)
     */
    public IntIterator rows(Object val) {
        return rows(val, val, Index.TYPE_AII);
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.data.util.Index#get(double)
     */
    public int get(double x) {
        DoubleIntSortedMap index = (DoubleIntSortedMap)m_index;
        return index.get(x);
    }

    /**
     * @see prefux.data.util.Index#get(float)
     */
    public int get(float x) {
        FloatIntSortedMap index = (FloatIntSortedMap)m_index;
        return index.get(x);
    }

    /**
     * @see prefux.data.util.Index#get(int)
     */
    public int get(int x) {
        IntIntSortedMap index = (IntIntSortedMap)m_index;
        return index.get(x);
    }

    /**
     * @see prefux.data.util.Index#get(long)
     */
    public int get(long x) {
        LongIntSortedMap index = (LongIntSortedMap)m_index;
        return index.get(x);
    }

    /**
     * @see prefux.data.util.Index#get(java.lang.Object)
     */
    public int get(Object x) {
        ObjectIntSortedMap index = (ObjectIntSortedMap)m_index;
        return index.get(x);
    }

} // end of class ColumnIndex
