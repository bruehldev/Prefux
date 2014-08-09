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
 * Renderer that does nothing, causing an item to be rendered "into
 * the void". Possibly useful for items that must exist and have a spatial
 * location but should otherwise be invisible and non-interactive (e.g.,
 * invisible end-points for visible edges).
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class NullRenderer implements Renderer {

    
    
    /**
     * @see prefux.render.Renderer#setBounds(prefux.visual.VisualItem)
     */
    public void setBounds(VisualItem item) {
        item.setBounds(item.getX(), item.getY(), 0, 0);
    }

	@Override
	public void render(Parent g, VisualItem item) {
		// Do nothing
		
	}

	@Override
	public void render(Parent g, VisualItem item, boolean bind) {
		// Do nothing
		
	}

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		return false;
	}

	@Override
	public void addStyle(String style) {
		// TODO Auto-generated method stub
		
	}



} // end of class NullRenderer
