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
package prefux.action.layout;

import java.util.Iterator;

import javafx.geometry.Rectangle2D;
import prefux.Constants;
import prefux.data.Table;
import prefux.render.PolygonRenderer;
import prefux.visual.VisualItem;

/**
 * Layout Action that updates the outlines of polygons in a stacked line chart,
 * properly setting the coordinates of "collapsed" stacks.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class CollapsedStackLayout extends Layout {

    private String    m_polyField;
    private int       m_orientation = Constants.ORIENT_BOTTOM_TOP;
    private boolean   m_horiz = false;
    private boolean   m_top = false;
    
    /**
     * Create a new CollapsedStackLayout. The polygon field is assumed to be
     * {@link prefux.render.PolygonRenderer#POLYGON}.
     * @param group the data group to layout
     */
    public CollapsedStackLayout(String group) {
        this(group, PolygonRenderer.POLYGON);
    }
    
    /**
     * Create a new CollapsedStackLayout.
     * @param group the data group to layout
     * @param field the data field from which to lookup the polygons
     */
    public CollapsedStackLayout(String group, String field) {
        super(group);
        m_polyField = field;
    }
    
    /**
     * Returns the orientation of this layout. One of
     * {@link Constants#ORIENT_BOTTOM_TOP} (to grow bottom-up),
     * {@link Constants#ORIENT_TOP_BOTTOM} (to grow top-down),
     * {@link Constants#ORIENT_LEFT_RIGHT} (to grow left-right), or
     * {@link Constants#ORIENT_RIGHT_LEFT} (to grow right-left).
     * @return the orientation of this layout
     */
    public int getOrientation() {
        return m_orientation;
    }
    
    /**
     * Sets the orientation of this layout. Must be one of
     * {@link Constants#ORIENT_BOTTOM_TOP} (to grow bottom-up),
     * {@link Constants#ORIENT_TOP_BOTTOM} (to grow top-down),
     * {@link Constants#ORIENT_LEFT_RIGHT} (to grow left-right), or
     * {@link Constants#ORIENT_RIGHT_LEFT} (to grow right-left).
     * @param orient the desired orientation of this layout
     * @throws IllegalArgumentException if the orientation value
     * is not a valid value
     */
    public void setOrientation(int orient) {
        if ( orient != Constants.ORIENT_TOP_BOTTOM &&
             orient != Constants.ORIENT_BOTTOM_TOP &&
             orient != Constants.ORIENT_LEFT_RIGHT &&
             orient != Constants.ORIENT_RIGHT_LEFT) {
            throw new IllegalArgumentException(
                    "Invalid orientation value: "+orient);
        }
        m_orientation = orient;
        m_horiz = (m_orientation == Constants.ORIENT_LEFT_RIGHT ||
                   m_orientation == Constants.ORIENT_RIGHT_LEFT);
        m_top   = (m_orientation == Constants.ORIENT_TOP_BOTTOM ||
                   m_orientation == Constants.ORIENT_LEFT_RIGHT);
    }
    
    /**
     * @see prefux.action.Action#run(double)
     */
    public void run(double frac) {
        VisualItem lastItem = null;
        
        Rectangle2D bounds = getLayoutBounds();
        double floor = (double)
            (m_horiz ? (m_top?bounds.getMaxX():bounds.getMinX())
                     : (m_top?bounds.getMinY():bounds.getMaxY()));
        int bias = (m_horiz ? 0 : 1);
        
        // TODO: generalize this -- we want tuplesReversed available for general sets
        Iterator iter = ((Table)m_vis.getGroup(m_group)).tuplesReversed();
        while ( iter.hasNext() ) {
            VisualItem item = (VisualItem)iter.next();
            boolean prev = item.isStartVisible();
            boolean cur = item.isVisible();
            
            if ( !prev && cur ) {
                // newly visible, update contour
                double[] f = (double[])item.get(m_polyField);
                if ( f == null ) continue;
                
                if ( lastItem == null ) {
                    // no previous items, smash values to the floor
                    for ( int i=0; i<f.length; i+=2 )
                        f[i+bias] = floor;
                } else {
                    // previous visible item, smash values to the
                    // visible item's contour
                    double[] l = (double[])lastItem.get(m_polyField);
                    for ( int i=0; i<f.length/2; i+=2 )
                        f[i+bias] = f[f.length-2-i+bias]
                                  = l[i+bias];
                }
            } else if ( prev && cur ) {
                // this item was previously visible, remember it
                lastItem = item;
            }
        }
    }
    
} // end of class CollapsedStackAction
