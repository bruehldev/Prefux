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
import prefux.data.util.Point2D;
import prefux.visual.NodeItem;
import prefux.visual.VisualItem;
import prefux.visual.expression.StartVisiblePredicate;

/**
 * Layout Action that sets the positions for newly collapsed or newly
 * expanded nodes of a tree. This action updates positions such that
 * nodes flow out from their parents or collapse back into their parents
 * upon animated transitions.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class CollapsedSubtreeLayout extends Layout {

    private int m_orientation;
    private Point2D m_point = new Point2D();
    
    /**
     * Create a new CollapsedSubtreeLayout. By default, nodes will collapse
     * to the center point of their parents.
     * @param group the data group to layout (only newly collapsed or newly
     * expanded items will be considered, as determined by their current
     * visibility settings).
     */
    public CollapsedSubtreeLayout(String group) {
        this(group, Constants.ORIENT_CENTER);
    }
    
    /**
     * Create a new CollapsedSubtreeLayout.
     * @param group the data group to layout (only newly collapsed or newly
     * expanded items will be considered, as determined by their current
     * visibility settings).
     * @param orientation the layout orientation, determining which point
     * nodes will collapse/expand from. Valid values are
     * {@link prefux.Constants#ORIENT_CENTER},
     * {@link prefux.Constants#ORIENT_LEFT_RIGHT},
     * {@link prefux.Constants#ORIENT_RIGHT_LEFT},
     * {@link prefux.Constants#ORIENT_TOP_BOTTOM}, and
     * {@link prefux.Constants#ORIENT_BOTTOM_TOP}.
     */
    public CollapsedSubtreeLayout(String group, int orientation) {
        super(group);
        m_orientation = orientation;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Get the layout orientation, determining which point nodes will collapse
     * or exapnd from. Valid values are
     * {@link prefux.Constants#ORIENT_CENTER},
     * {@link prefux.Constants#ORIENT_LEFT_RIGHT},
     * {@link prefux.Constants#ORIENT_RIGHT_LEFT},
     * {@link prefux.Constants#ORIENT_TOP_BOTTOM}, and
     * {@link prefux.Constants#ORIENT_BOTTOM_TOP}.
     * @return the layout orientation
     */
    public int getOrientation() {
        return m_orientation;
    }
    
    /**
     * Set the layout orientation, determining which point nodes will collapse
     * or exapnd from. Valid values are
     * {@link prefux.Constants#ORIENT_CENTER},
     * {@link prefux.Constants#ORIENT_LEFT_RIGHT},
     * {@link prefux.Constants#ORIENT_RIGHT_LEFT},
     * {@link prefux.Constants#ORIENT_TOP_BOTTOM}, and
     * {@link prefux.Constants#ORIENT_BOTTOM_TOP}.
     * @return the layout orientation to use
     */
    public void setOrientation(int orientation) {
        if ( orientation < 0 || orientation >= Constants.ORIENTATION_COUNT )
            throw new IllegalArgumentException(
                "Unrecognized orientation value: "+orientation);
        m_orientation = orientation;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.action.Action#run(double)
     */
    public void run(double frac) {
        // handle newly expanded subtrees - ensure they emerge from
        // a visible ancestor node
        Iterator items = m_vis.visibleItems(m_group);
        while ( items.hasNext() ) {
            VisualItem item = (VisualItem) items.next();
            if ( item instanceof NodeItem && !item.isStartVisible() ) {
                NodeItem n = (NodeItem)item;
                Point2D p = getPoint(n, true);
                n.setStartX(p.getX());
                n.setStartY(p.getY());
            }
        }
        
        // handle newly collapsed nodes - ensure they collapse to
        // the greatest visible ancestor node
        items = m_vis.items(m_group, StartVisiblePredicate.TRUE);
        while ( items.hasNext() ) {
            VisualItem item = (VisualItem) items.next();
            if ( item instanceof NodeItem && !item.isEndVisible() ) {
                NodeItem n = (NodeItem)item;
                Point2D p = getPoint(n, false);
                n.setStartX(n.getEndX());
                n.setStartY(n.getEndY());
                n.setEndX(p.getX());
                n.setEndY(p.getY());
            }
        }

    }

    private Point2D getPoint(NodeItem n, boolean start) {
        // find the visible ancestor
        NodeItem p = (NodeItem)n.getParent();
        if ( start )
            for (; p!=null && !p.isStartVisible(); p=(NodeItem)p.getParent());
        else
            for (; p!=null && !p.isEndVisible(); p=(NodeItem)p.getParent());
        if ( p == null ) {
            m_point = new Point2D(n.getX(), n.getY());
            return m_point;
        }
        
        // get the vanishing/appearing point
        double x = start ? p.getStartX() : p.getEndX();
        double y = start ? p.getStartY() : p.getEndY();
        Rectangle2D b = p.getBounds();
        switch ( m_orientation ) {
        case Constants.ORIENT_LEFT_RIGHT:
            m_point= new Point2D(x+b.getWidth(), y);
            break;
        case Constants.ORIENT_RIGHT_LEFT:
            m_point= new Point2D(x-b.getWidth(), y);
            break;
        case Constants.ORIENT_TOP_BOTTOM:
            m_point= new Point2D(x, y+b.getHeight());
            break;
        case Constants.ORIENT_BOTTOM_TOP:
            m_point= new Point2D(x, y-b.getHeight());
            break;
        case Constants.ORIENT_CENTER:
            m_point= new Point2D(x, y);
            break;
        }
        return m_point;
    }

} // end of class CollapsedSubtreeLayout
