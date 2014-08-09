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
import java.util.Random;

import javafx.geometry.Rectangle2D;
import prefux.visual.VisualItem;


/**
 * Performs a random layout of items within the layout bounds.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class RandomLayout extends Layout {

    private Random r = new Random(12345678L);
    
    /**
     * Create a new RandomLayout that processes all items.
     */
    public RandomLayout() {
        super();
    }
    
    /**
     * Create a new RandomLayout.
     * @param group the data group to layout
     */
    public RandomLayout(String group) {
        super(group);
    }

    /**
     * Set the seed value for the random number generator.
     * @param seed the random seed value
     */
    public void setRandomSeed(long seed) {
        r.setSeed(seed);
    }
    
    /**
     * @see prefux.action.Action#run(double)
     */
    public void run(double frac) {
        Rectangle2D b = getLayoutBounds();
        double x, y;
        double w = b.getWidth();
        double h = b.getHeight();
        Iterator iter = getVisualization().visibleItems(m_group);
        while ( iter.hasNext() ) {
            VisualItem item = (VisualItem)iter.next();
            x = (int)(b.getMinX() + r.nextDouble()*w);
            y = (int)(b.getMinY() + r.nextDouble()*h);
            setX(item,null,x);
            setY(item,null,y);
        }
    }

} // end of class RandomLayout
