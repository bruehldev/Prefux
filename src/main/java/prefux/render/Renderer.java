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
package prefux.render;


import javafx.scene.Parent;
import prefux.data.util.Point2D;
import prefux.visual.VisualItem;


/**
 * Interface for rendering VisualItems, providing drawing as well as location
 * checking and bounding box routines.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @author alan newberger
 */
public interface Renderer {

    /**
     * Provides a default graphics context for renderers to do useful
     * things like compute string widths when an external graphics context
     * has not yet been provided.
     */
//    public static final Graphics2D DEFAULT_GRAPHICS = (Graphics2D)
//        new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();

    /**
     * Render item into a Parent context.
     * @param g the Parent node
     * @param item the visual item to draw
     */
    public void render(Parent g, VisualItem item);
    
    /**
     * Render item into a Parent context.
     * @param g the Parent node
     * @param item the visual item to draw
     * @param bind if false the binding to the visual item should be omitted
     */
    public void render(Parent g, VisualItem item, boolean bind);

    /**
     * Returns true if the Point is located inside the extents of the item.
     * This calculation matches against the exaxt item shape, and so is more
     * sensitive than just checking within a bounding box.
     * @param p the point to test for containment
     * @param item the item to test containment against
     * @return true if the point is contained within the the item, else false
     */
    public boolean locatePoint(Point2D p, VisualItem item);

    /**
     * Adds an additional style class to each rendered element.
     * 
     * @param style
     */
    public void addStyle(String style);
    

} // end of interface Renderer
