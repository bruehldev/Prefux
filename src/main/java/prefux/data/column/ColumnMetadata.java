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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import prefux.data.Table;
import prefux.data.event.ColumnListener;
import prefux.data.util.Index;
import prefux.util.DataLib;
import prefux.util.TypeLib;
import prefux.util.collections.DefaultLiteralComparator;

/**
 * ColumnMetadata stores computed metadata and statistics for a singe column
 * instance. They are created automatically by Table instances and are
 * retrieved using the {@link prefux.data.Table#getMetadata(String)} method.
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ColumnMetadata implements ColumnListener {

	// TODO consider refactor. is non-dynamic mode needed? pass Column reference in?
	
    private Table   m_table;
    private String  m_field;
    private boolean m_dynamic;
    private boolean m_init;

    private Comparator m_cmp;
    
    private Object m_default;
    private int m_min;
    private int m_max;
    private int m_median;
    private int m_unique;
    private Double m_mean;
    private Double m_stdev;
    private Double m_sum;
    private Object[] m_ordinalA;
    private Map m_ordinalM;
    
    // ------------------------------------------------------------------------
    
    /**
     * Creates a new ColumnMetadata instance.
     * @param table the backing table
     * @param column the name of the column to store metadata for
     */
    public ColumnMetadata(Table table, String column) {
        this(table, column, DefaultLiteralComparator.getInstance(), true);
    }

    /**
     * Creates a new ColumnMetadata instance.
     * @param table the backing table
     * @param column the name of the column to store metadata for
     * @param cmp a Comparator that determines the default sort order for
     * values in the column
     * @param dynamic indicates if this ColumnMetadata should react to
     * changes in the underlying table values. If true, computed values
     * stored in this metadata object will be invalidated when updates to
     * the column data occur.
     */
    public ColumnMetadata(Table table, String column, 
            Comparator cmp, boolean dynamic)
    {
        m_table = table;
        m_field = column;
        m_cmp = cmp;
        m_dynamic = dynamic;
    }
    
    /**
     * Dispose of this metadata, freeing any resources and unregistering any
     * listeners.
     */
    public void dispose() {
        m_table.getColumn(m_field).removeColumnListener(this);
    }

    // ------------------------------------------------------------------------
    
    private void clearCachedValues() {
        m_min    = -1;
        m_max    = -1;
        m_median = -1;
        m_unique = -1;
        m_mean   = null;
        m_stdev  = null;
        m_sum    = null;
        m_ordinalA = null;
        m_ordinalM = null;
    }
    
    /**
     * Re-calculates all the metadata and statistics maintained by this object.
     */
    public void calculateValues() {
        clearCachedValues();
        boolean dyn = m_dynamic;
        m_dynamic = true;
        getMinimumRow();
        getMaximumRow();
        getMedianRow();
        getUniqueCount();
        if ( TypeLib.isNumericType(m_table.getColumnType(m_field)) ) {
            getMean();
            getDeviation();
            getSum();
        }
        getOrdinalArray();
        getOrdinalMap();
        m_init = true;
        m_dynamic = dyn;
    }
    
    private void accessCheck() {
        if ( m_init ) return;
        
        if ( m_dynamic ) {
          clearCachedValues();
          m_table.getColumn(m_field).addColumnListener(this);
        } else {
          calculateValues();
        }
        m_init = true;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Returns the comparator used to determine column values' sort order.
     * @return the Comparator
     */
    public Comparator getComparator() {
        return m_cmp;
    }
    
    /**
     * Sets the comparator used to detemine column values' sort order.
     * @param c the Comparator to use
     */
    public void setComparator(Comparator c) {
        m_cmp = c;
        clearCachedValues();
    }
    
    /**
     * Get the columns' default value.
     * @return the column's default value
     */
    public Object getDefaultValue() {
        return m_default;
    }
    
    /**
     * Get the row index of the minimum column value. If there are multiple
     * minima, only one is returned.
     * @return the row index of the minimum column value.
     */
    public int getMinimumRow() {
        accessCheck();
        if ( m_min == -1 && m_dynamic ) {
            Index idx = m_table.getIndex(m_field);
            if ( idx != null ) {
                m_min = idx.minimum();
            } else {
                m_min = DataLib.min(m_table.tuples(), m_field, m_cmp).getRow();
            }
        }
        return m_min;
    }

    /**
     * Get the row index of the maximum column value. If there are multiple
     * maxima, only one is returned.
     * @return the row index of the maximum column value.
     */
    public int getMaximumRow() {
        accessCheck();
        if ( m_max == -1 && m_dynamic ) {
            Index idx = m_table.getIndex(m_field);
            if ( idx != null ) {
                m_max = idx.maximum();
            } else {
                m_max = DataLib.max(m_table.tuples(), m_field, m_cmp).getRow();
            }
        }
        return m_max;
    }
    
    /**
     * Get the row index of the median column value.
     * @return the row index of the median column value
     */
    public int getMedianRow() {
        accessCheck();
        if ( m_median == -1 && m_dynamic ) {
            Index idx = m_table.getIndex(m_field);
            if ( idx != null ) {
                m_max = idx.median();
            } else {
                m_median = DataLib.median(
                                m_table.tuples(), m_field, m_cmp).getRow();
            }
        }
        return m_median;
    }
    
    /**
     * Get the number of unique values in the column.
     * @return the number of unique values in the column
     */
    public int getUniqueCount() {
        accessCheck();
        if ( m_unique == -1 && m_dynamic ) {
            Index idx = m_table.getIndex(m_field);
            if ( idx != null ) {
                m_unique = idx.uniqueCount();
            } else {
                m_unique = DataLib.uniqueCount(m_table.tuples(), m_field);
            }
        }
        return m_unique;
    }
    
    /**
     * Get the mean value of numeric values in the column. If this column
     * does not contain numeric values, this method will result in an
     * exception being thrown.
     * @return the mean of numeric values in the column
     */
    public double getMean() {
        accessCheck();
        if ( m_mean == null && m_dynamic ) {
            m_mean = new Double(DataLib.mean(m_table.tuples(), m_field));
        }
        return m_mean.doubleValue();
    }
    
    /**
     * Get the standard deviation of numeric values in the column. If this column
     * does not contain numeric values, this method will result in an
     * exception being thrown.
     * @return the standard deviation of numeric values in the column
     */
    public double getDeviation() {
        accessCheck();
        if ( m_stdev == null && m_dynamic ) {
            m_stdev = new Double(
                    DataLib.deviation(m_table.tuples(), m_field, getMean()));
        }
        return m_stdev.doubleValue();
    }
    
    /**
     * Get the sum of numeric values in the column. If this column
     * does not contain numeric values, this method will result in an
     * exception being thrown.
     * @return the sum of numeric values in the column
     */
    public double getSum() {
        accessCheck();
        if ( m_sum == null && m_dynamic ) {
            m_sum = new Double(DataLib.sum(m_table.tuples(), m_field));
        }
        return m_sum.doubleValue();
    }
    
    /**
     * Get an array of all unique column values, in sorted order.
     * @return an array of all unique column values, in sorted order.
     */
    public Object[] getOrdinalArray() {
        accessCheck();
        if ( m_ordinalA == null && m_dynamic ) {
            m_ordinalA = DataLib.ordinalArray(m_table.tuples(),m_field,m_cmp);
        }
        return m_ordinalA;
    }
    
    /**
     * Get a map between all unique column values and their integer index
     * in the sort order of those values. For example, the minimum value
     * maps to 0, the next greater value to 1, etc.
     * @return a map between all unique column values and their integer index
     * in the values' sort order
     */
    public Map getOrdinalMap() {
        accessCheck();
        if ( m_ordinalM == null && m_dynamic ) {
            Object[] a = getOrdinalArray();
            m_ordinalM = new HashMap();
            for ( int i=0; i<a.length; ++i )
                m_ordinalM.put(a[i], new Integer(i));
            //m_ordinalM = DataLib.ordinalMap(m_table.tuples(), m_field, m_cmp);
        }
        return m_ordinalM;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, int, int)
     */
    public void columnChanged(Column src, int type, int start, int end) {
        clearCachedValues();
    }
    
    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, boolean)
     */
    public void columnChanged(Column src, int idx, boolean prev) {
        columnChanged(src, 0, idx, idx);
    }

    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, double)
     */
    public void columnChanged(Column src, int idx, double prev) {
        columnChanged(src, 0, idx, idx);
    }

    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, float)
     */
    public void columnChanged(Column src, int idx, float prev) {
        columnChanged(src, 0, idx, idx);
    }
    
    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, int)
     */
    public void columnChanged(Column src, int idx, int prev) {
        columnChanged(src, 0, idx, idx);
    }

    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, long)
     */
    public void columnChanged(Column src, int idx, long prev) {
        columnChanged(src, 0, idx, idx);
    }

    /**
     * @see prefux.data.event.ColumnListener#columnChanged(prefux.data.column.Column, int, java.lang.Object)
     */
    public void columnChanged(Column src, int idx, Object prev) {
        columnChanged(src, 0, idx, idx);
    }
    
} // end of class ColumnMetadata
