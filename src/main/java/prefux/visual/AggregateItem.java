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
/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.visual;

import java.util.Iterator;

import prefux.data.expression.Predicate;

/**
 * VisualItem that represents an aggregation of one or more other VisualItems.
 * AggregateItems include methods adding and removing items from the aggregate
 * collection, and are backed by an {@link AggregateTable} instance.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface AggregateItem extends VisualItem {
    
    /**
     * Get the size of this AggregateItem, the number of visual items
     * contained in the aggregation.
     * @return the aggregate size
     */
    public int getAggregateSize();
    
    /**
     * Indicates is a given VisualItem is contained in the aggregation.
     * @param item the VisualItem to check for containment
     * @return true if the given item is contained in this aggregate,
     * false otherwise.
     */
    public boolean containsItem(VisualItem item);
    
    /**
     * Add a VisualItem to this aggregate.
     * @param item the item to add
     */
    public void addItem(VisualItem item);
    
    /**
     * Remove a VisualItem from this aggregate.
     * @param item the item to remove
     */
    public void removeItem(VisualItem item);
    
    /**
     * Remove all items contained in this aggregate.
     */
    public void removeAllItems();
    
    /**
     * Get an iterator over all the items contained in this aggregate.
     * @return an iterator over the items in this aggregate
     */
    public Iterator<? extends VisualItem> items();
    
    /**
     * Get a filtered iterator over all the items contained in this aggregate.
     * @param filter a Predicate instance indicating the filter criteria
     * @return an iterator over the items in this aggregate
     */
    public Iterator<? extends VisualItem> items(Predicate filter);
    
} // end of interface AggregateItem
