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
package prefux.action;

import java.util.Iterator;

import prefux.Visualization;
import prefux.data.expression.Predicate;
import prefux.visual.VisualItem;
import prefux.visual.expression.VisiblePredicate;

/**
 * An Action that processes VisualItems one item at a time. By default,
 * it only processes items that are visible. Use the
 * {@link #setFilterPredicate(Predicate)} method
 * to change the filtering criteria.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class ItemAction extends GroupAction {
    
    /** A reference to filtering predicate for this Action */
    protected Predicate m_predicate;
    
    /**
     * Create a new ItemAction that processes all groups.
     * @see Visualization#ALL_ITEMS
     */
    public ItemAction() {
        this((Visualization)null);
    }
    
    /**
     * Create a new ItemAction that processes all groups.
     * @param vis the {@link prefux.Visualization} to process
     * @see Visualization#ALL_ITEMS
     */
    public ItemAction(Visualization vis) {
        this(vis, Visualization.ALL_ITEMS);
    }
    
    /**
     * Create a new ItemAction that processes the specified group.
     * @param group the name of the group to process
     */
    public ItemAction(String group) {
        this(null, group);
    }
    
    /**
     * Create a new ItemAction that processes the specified group.
     * @param group the name of the group to process
     * @param filter the filtering {@link prefux.data.expression.Predicate}
     */
    public ItemAction(String group, Predicate filter) {
        this(null, group, filter);
    }
    
    /**
     * Create a new ItemAction that processes the specified group.
     * @param vis the {@link prefux.Visualization} to process
     * @param group the name of the group to process
     */
    public ItemAction(Visualization vis, String group) {
        this(vis, group, VisiblePredicate.TRUE);
    }

    /**
     * Create a new ItemAction that processes the specified group.
     * @param vis the {@link prefux.Visualization} to process
     * @param group the name of the group to process
     * @param filter the filtering {@link prefux.data.expression.Predicate}
     */
    public ItemAction(Visualization vis, String group, Predicate filter) {
        super(vis, group);
        m_predicate = filter;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Returns the filtering predicate used by this Action.
     * @return the filtering {@link prefux.data.expression.Predicate}
     */
    public Predicate getFilterPredicate() {
        return m_predicate;
    }

    /**
     * Sets the filtering predicate used by this Action.
     * @param filter the filtering {@link prefux.data.expression.Predicate}
     * to use
     */
    public void setFilterPredicate(Predicate filter) {
        m_predicate = filter;
    }
    
    /**
     * @see prefux.action.Action#run(double)
     */
    public void run(double frac) {
        Iterator<VisualItem> items = getVisualization().items(m_group, m_predicate);
        while ( items.hasNext() ) {
            process(items.next(), frac);
        }
    }
    
    /**
     * Processes an individual item.
     * @param item the VisualItem to process
     * @param frac the fraction of elapsed duration time
     */
    public abstract void process(VisualItem item, double frac);

} // end of class ItemAction
