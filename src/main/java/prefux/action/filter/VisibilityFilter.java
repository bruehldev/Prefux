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
package prefux.action.filter;

import java.util.Iterator;

import prefux.Visualization;
import prefux.action.GroupAction;
import prefux.data.expression.OrPredicate;
import prefux.data.expression.Predicate;
import prefux.util.PrefuseLib;
import prefux.visual.VisualItem;
import prefux.visual.expression.VisiblePredicate;

/**
 * Filter Action that sets visible all items that meet a given Predicate
 * condition and sets all other items invisible.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class VisibilityFilter extends GroupAction {
    
    private Predicate m_filter;
    private Predicate m_predicate;
    
    /**
     * Create a new VisibilityFilter.
     * @param p the test predicate used to determine visibility
     */
    public VisibilityFilter(Predicate p) {
        setPredicate(p);
    }

    /**
     * Create a new VisibilityFilter.
     * @param group the data group to process
     * @param p the test predicate used to determine visibility
     */
    public VisibilityFilter(String group, Predicate p) {
        super(group);
        setPredicate(p);
    }

    /**
     * Create a new VisibilityFilter.
     * @param vis the Visualization to process
     * @param group the data group to process
     * @param p the test predicate used to determine visibility
     */
    public VisibilityFilter(Visualization vis, String group, Predicate p) {
        super(vis, group);
        setPredicate(p);
    }

    /**
     * Set the test predicate used to determine visibility.
     * @param p the test predicate to set
     */
    protected void setPredicate(Predicate p) {
        m_predicate = p;
        m_filter = new OrPredicate(p, VisiblePredicate.TRUE);
    }
    
    /**
     * @see prefux.action.Action#run(double)
     */
    public void run(double frac) {
        Iterator<VisualItem> items = m_vis.items(m_group, m_filter);
        while ( items.hasNext() ) {
            VisualItem item = (VisualItem)items.next();
            PrefuseLib.updateVisible(item, m_predicate.getBoolean(item));
        }
    }

} // end of class VisibilityAction
