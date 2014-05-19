/**
 * Copyright (c) 2014 Martin Stockhammer
 * See "LICENSE.txt" for licensing terms.
 */
package prefux;

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

	
	public abstract double getFrameRate();

	public abstract int getVisibleItemCount();

	public abstract double getDisplayX();

	public abstract double getDisplayY();

	public abstract double getScale();

	public abstract void setHighQuality(boolean quality);

	public abstract Visualization getVisualization();

	public abstract void repaint();
	
	public double getWidth();
	
	public double getHeight();

	public abstract void getAbsoluteCoordinate(Point2D m_anchor,
			Point2D m_anchor2);

	public abstract boolean isTranformInProgress();

	public abstract void zoomAbs(Point2D p, double zoom);

	public abstract void zoom(Point2D p, double zoom);

	public abstract void animatePanAndZoomToAbs(Point2D center, double scale,
			long duration);

	public abstract void panToAbs(Point2D center);

	


}