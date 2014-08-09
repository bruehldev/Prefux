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
 * Copyright (c) 2014 Martin Stockhammer
 * See "LICENSE.txt" for licensing terms.
 */
package prefux;

import prefux.controls.Control;
import prefux.data.expression.Predicate;
import prefux.data.util.Point2D;
import prefux.data.util.Rectangle2D;

public interface Display {



	/**
	 * Returns the filtering Predicate used to control what items are drawn
	 * by this display.
	 * @return the filtering {@link prefux.data.expression.Predicate}
	 */
	public abstract Predicate getPredicate();

	/**
	 * Reports damage to the Display within in the specified region.
	 * @param region the damaged region, in absolute coordinates
	 */
	public abstract void damageReport(Rectangle2D region);

	/**
	 * Reports damage to the entire Display.
	 */
	public abstract void damageReport();

	public abstract int getVisibleItemCount();

	public abstract double getDisplayX();

	public abstract double getDisplayY();

	public abstract double getScale();

	public abstract Visualization getVisualization();

	public abstract void repaint();
	
	public double getWidth();
	
	public double getHeight();

	public abstract Point2D getAbsoluteCoordinate(Point2D m_anchor);

	public abstract void zoomAbs(Point2D p, double zoom);

	public abstract void zoom(Point2D p, double zoom);



	public abstract void panToAbs(Point2D center);

	/**
	 * Registers a control listener
	 * @param cl
	 */
	public void addControlListener(Control cl);

    /**
     * Removes a registered ControlListener.
     * 
     * @param cl
     *            the listener to remove.
     */
    public void removeControlListener(Control cl);


}