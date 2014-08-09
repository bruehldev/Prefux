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
import prefux.data.tuple.TupleSet;
import prefux.visual.VisualItem;

/**
 * Layout action that positions visual items along a circle. By default,
 * items are sorted in the order in which they iterated over.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class CircleLayout extends Layout {
    
    private double m_radius; // radius of the circle layout
    
    /**
     * Create a CircleLayout; the radius of the circle layout will be computed
     * automatically based on the display size.
     * @param group the data group to layout
     */
    public CircleLayout(String group) {
        super(group);
    }
    
    /**
     * Create a CircleLayout; use the specified radius for the the circle layout,
     * regardless of the display size.
     * @param group the data group to layout
     * @param radius the radius of the circle layout.
     */
    public CircleLayout(String group, double radius) {
        super(group);
        m_radius = radius;
    }
    
    /**
     * Return the radius of the layout circle.
     * @return the circle radius
     */
    public double getRadius() {
        return m_radius;
    }

    /**
     * Set the radius of the layout circle.
     * @param radius the circle radius to use
     */
    public void setRadius(double radius) {
        m_radius = radius;
    }
    
    /**
     * @see prefux.action.Action#run(double)
     */
    public void run(double frac) {
        TupleSet ts = m_vis.getGroup(m_group); 
        
        int nn = ts.getTupleCount();
        
        Rectangle2D r = getLayoutBounds();  
        double height = r.getHeight();
        double width = r.getWidth();
        double cx = r.getMinX()+r.getWidth()/2.0;
        double cy = r.getMinY()+r.getHeight()/2.0;

        double radius = m_radius;
        if (radius <= 0) {
            radius = 0.45 * (height < width ? height : width);
        }

        Iterator items = ts.tuples();
        for (int i=0; items.hasNext(); i++) {
            VisualItem n = (VisualItem)items.next();
            double angle = (2*Math.PI*i) / nn;
            double x = Math.cos(angle)*radius + cx;
            double y = Math.sin(angle)*radius + cy;
            setX(n, null, x);
            setY(n, null, y);
        }
    }

} // end of class CircleLayout
