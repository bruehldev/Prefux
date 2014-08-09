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
package prefux.action.animate;

import java.util.Iterator;

import prefux.action.GroupAction;
import prefux.util.ColorLib;
import prefux.visual.VisualItem;
import prefux.visual.expression.StartVisiblePredicate;

/**
 * Animator that interpolates the visibility status of VisualItems. Items
 * not currently visible but with end visibility true are faded in, while
 * items currently visible but with end visibility false are faded out and
 * finally set to not visible.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class VisibilityAnimator extends GroupAction {
    
    /**
     * Create a new VisibilityAnimator that processes all data groups.
     */
    public VisibilityAnimator() {
        super();
    }
    
    /**
     * Create a new VisibilityAnimator that processes the specified group.
     * @param group the data group to process.
     */
    public VisibilityAnimator(String group) {
        super(group);
    }
    
    /**
     * @see prefux.action.GroupAction#run(double)
     */
    public void run(double frac) {
        if ( frac == 0.0 ) {
            setup();
        } else if ( frac == 1.0 ) {
            finish();
        }
    }
    
    private void setup() {
        // handle fade-in nodes
        Iterator<VisualItem> items = m_vis.visibleItems(m_group);
        while ( items.hasNext() ) {
            VisualItem item = items.next();
            if ( !item.isStartVisible() ) {
                item.setStartFillColor(
                        ColorLib.setAlpha(item.getEndFillColor(),0));
                item.setStartStrokeColor(
                        ColorLib.setAlpha(item.getEndStrokeColor(),0));
                item.setStartTextColor(
                        ColorLib.setAlpha(item.getEndTextColor(),0));
            }
        }
        
        // handle fade-out nodes
        items = m_vis.items(m_group, StartVisiblePredicate.TRUE);
        while ( items.hasNext() ) {
            VisualItem item = items.next();
            if ( !item.isEndVisible() ) {
                // fade-out case
                item.setVisible(true);
                item.setEndFillColor(
                        ColorLib.setAlpha(item.getStartFillColor(),0));
                item.setEndStrokeColor(
                        ColorLib.setAlpha(item.getStartStrokeColor(),0));
                item.setEndTextColor(
                        ColorLib.setAlpha(item.getStartTextColor(),0));
            }
        }
    }
    
    private void finish() {
        // set faded-out nodes to permanently invisible
        Iterator<VisualItem> items = m_vis.items(m_group, StartVisiblePredicate.TRUE);
        while ( items.hasNext() ) {
            VisualItem item = items.next();
            if ( !item.isEndVisible() ) {
                item.setVisible(false);
                item.setStartVisible(false);
            }
        }
        
        // set faded-in nodes to permanently visible
        items = m_vis.visibleItems(m_group);
        while ( items.hasNext() ) {
            VisualItem item = items.next();
            if ( !item.isStartVisible() ) {
                item.setStartVisible(true);
                item.setStartFillColor(item.getEndFillColor());
                item.setStartTextColor(item.getEndTextColor());
                item.setStartStrokeColor(item.getEndStrokeColor());
            }
        }
    }

} // end of class VisibilityAnimator
