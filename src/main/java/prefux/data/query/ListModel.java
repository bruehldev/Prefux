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
package prefux.data.query;

import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import prefux.util.collections.CopyOnWriteArrayList;

/**
 * List data model supporting both data modeling and selection management.
 * Though generally useful, this has been designed particularly to support
 * dynamic queries.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ListModel extends DefaultListSelectionModel
    implements MutableComboBoxModel
{
    private ArrayList m_items = new ArrayList();
    private CopyOnWriteArrayList m_lstnrs = new CopyOnWriteArrayList();
    
    /**
     * Create an empty ListModel.
     */
    public ListModel() {
        // do nothing
    }
    
    /**
     * Create a ListModel with the provided items.
     * @param items the items for the data model.
     */
    public ListModel(final Object[] items) {
        for ( int i=0; i<items.length; ++i )
            m_items.add(items[i]);
    }
    
    // --------------------------------------------------------------------
    
    /**
     * Indicates if the ListModel currently has multiple selections.
     * @return true if there are multiple selections, false otherwise
     */
    private boolean isMultipleSelection() {
        return getMaxSelectionIndex()-getMinSelectionIndex() > 0;
    }
    
    /**
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    public Object getSelectedItem() {
        int idx = getMinSelectionIndex();
        return ( idx == -1 ? null : m_items.get(idx) );
    }
    
    /**
     * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
     */
    public void setSelectedItem(Object item) {
        int idx = m_items.indexOf(item);
        if ( idx < 0 ) return;
        
        if ( !isMultipleSelection() && idx == getMinSelectionIndex() )
            return;
        
        super.setSelectionInterval(idx,idx);
        fireDataEvent(this,ListDataEvent.CONTENTS_CHANGED,-1,-1);
    }
    
    /**
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return m_items.size();
    }
    
    /**
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int idx) {
        return m_items.get(idx);
    }
    
    /**
     * @see javax.swing.MutableComboBoxModel#addElement(java.lang.Object)
     */
    public void addElement(Object item) {
        m_items.add(item);
        int sz = m_items.size()-1;
        fireDataEvent(this,ListDataEvent.INTERVAL_ADDED,sz,sz);
        if ( sz >= 0 && isSelectionEmpty() && item != null )
            setSelectedItem(item);
    }
    
    /**
     * @see javax.swing.MutableComboBoxModel#insertElementAt(java.lang.Object, int)
     */
    public void insertElementAt(Object item, int idx) {
        m_items.add(idx, item);
        fireDataEvent(this,ListDataEvent.INTERVAL_ADDED,idx,idx);
    }
    
    /**
     * @see javax.swing.MutableComboBoxModel#removeElement(java.lang.Object)
     */
    public void removeElement(Object item) {
        int idx = m_items.indexOf(item);
        if ( idx >= 0 )
            removeElementAt(idx);
    }
    
    /**
     * @see javax.swing.MutableComboBoxModel#removeElementAt(int)
     */
    public void removeElementAt(int idx) {
        if ( !isMultipleSelection() && idx == getMinSelectionIndex() ) {
            int sidx = ( idx==0 ? getSize()==1 ? -1 : idx+1 : idx-1 );
            Object sel = ( sidx == -1 ? null : m_items.get(sidx) );
            setSelectedItem(sel);
        }
    
        m_items.remove(idx);
        fireDataEvent(this,ListDataEvent.INTERVAL_REMOVED,idx,idx);
    }
    
    // --------------------------------------------------------------------
    // List Data Listeners
    
    /**
     * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
     */
    public void addListDataListener(ListDataListener l) {
        if ( !m_lstnrs.contains(l) )
            m_lstnrs.add(l);
    }
    
    /**
     * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
     */
    public void removeListDataListener(ListDataListener l) {
        m_lstnrs.remove(l);
    }
    
    /**
     * Fires a change notification in response to changes in the ListModel.
     */
    protected void fireDataEvent(Object src, int type, int idx0, int idx1) {
        Object[] lstnrs = m_lstnrs.getArray();
        if ( lstnrs.length > 0 ) {
            ListDataEvent e = new ListDataEvent(src, type, idx0, idx1);
            for ( int i=0; i<lstnrs.length; ++i ) {
                ((ListDataListener)lstnrs[i]).contentsChanged(e);
            }
        }
    }

} // end of class ListModel
