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
package prefux.action.assignment;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prefux.Constants;
import prefux.data.Tuple;
import prefux.data.tuple.TupleSet;
import prefux.util.MathLib;
import prefux.util.PrefuseLib;
import prefux.visual.NodeItem;
import prefux.visual.VisualItem;

/**
 * <p>
 * Assignment action that assigns the degree in / out / both of node items.
 * </p>
 * 
 * <p>
 * The data field will be assumed to be nominal, and shapes will be assigned to
 * unique values in the order they are encountered. Note that if the number of
 * unique values is greater than {@link prefux.Constants#SHAPE_COUNT} (when no
 * palette is given) or the length of a specified palette, then duplicate shapes
 * will start being assigned.
 * </p>
 * 
 * <p>
 * This Action only sets the shape field of the VisualItem. For this value to
 * have an effect, a renderer instance that takes this shape value into account
 * must be used (e.g., {@link prefux.render.ShapeRenderer}).
 * </p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class NodeDegreeSizeAction extends SizeAction {

	private static final Logger log = LogManager.getLogger(NodeDegreeSizeAction.class);

	private boolean in = true;
	private boolean out = true;

	protected static final double NO_SIZE = Double.NaN;

	protected double m_minSize = 1;
	protected double m_sizeRange;
	protected int m_scale = Constants.LINEAR_SCALE;
	protected int m_bins = Constants.CONTINUOUS;

	protected boolean m_inferBounds = true;
	protected boolean m_inferRange = true;
	protected boolean m_is2DArea = true;
	protected double[] m_dist;

	protected int m_tempScale;

	/**
	 * Create a new DataSizeAction.
	 * 
	 * @param group
	 *            the data group to process
	 */
	public NodeDegreeSizeAction(String group) {
		super(group, NO_SIZE);
	}

	/**
	 * Create a new DataSizeAction.
	 * 
	 * @param group
	 *            the data group to process
	 */
	public NodeDegreeSizeAction(String group, boolean in, boolean out) {
		super(group, NO_SIZE);
		if (!(in || out)) {
			throw new IllegalArgumentException(
			        "in and out cannot be both false");
		}
		this.in = in;
		this.out = out;
	}

	/**
	 * Create a new DataSizeAction.
	 * 
	 * @param group
	 *            the data group to process
	 * @param bins
	 *            the number of discrete size values to use
	 */
	public NodeDegreeSizeAction(String group, int bins, boolean in, boolean out) {
		this(group, bins, Constants.LINEAR_SCALE, in, out);
	}

	/**
	 * Create a new DataSizeAction.
	 * 
	 * @param group
	 *            the data group to process
	 * @param bins
	 *            the number of discrete size values to use
	 * @param scale
	 *            the scale type to use. One of
	 *            {@link prefux.Constants#LINEAR_SCALE},
	 *            {@link prefux.Constants#LOG_SCALE},
	 *            {@link prefux.Constants#SQRT_SCALE}, or
	 *            {@link prefux.Constants#QUANTILE_SCALE}. If a quantile scale
	 *            is used, the number of bins must be greater than zero.
	 */
	public NodeDegreeSizeAction(String group, int bins, int scale, boolean in,
	        boolean out) {
		super(group, NO_SIZE);
		setScale(scale);
		setBinCount(bins);
		if (!(in || out)) {
			throw new IllegalArgumentException(
			        "in and out cannot be both false");
		}
		this.in = in;
		this.out = out;
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the scale type used for encoding size values from the data.
	 * 
	 * @return the scale type. One of {@link prefux.Constants#LINEAR_SCALE},
	 *         {@link prefux.Constants#LOG_SCALE},
	 *         {@link prefux.Constants#SQRT_SCALE},
	 *         {@link prefux.Constants#QUANTILE_SCALE}.
	 */
	public int getScale() {
		return m_scale;
	}

	/**
	 * Set the scale (linear, square root, or log) to use for encoding size
	 * values from the data.
	 * 
	 * @param scale
	 *            the scale type to use. This value should be one of
	 *            {@link prefux.Constants#LINEAR_SCALE},
	 *            {@link prefux.Constants#SQRT_SCALE},
	 *            {@link prefux.Constants#LOG_SCALE},
	 *            {@link prefux.Constants#QUANTILE_SCALE}. If
	 *            {@link prefux.Constants#QUANTILE_SCALE} is used, the number of
	 *            bins to use must also be specified to a value greater than
	 *            zero using the {@link #setBinCount(int)} method.
	 */
	public void setScale(int scale) {
		if (scale < 0 || scale >= Constants.SCALE_COUNT)
			throw new IllegalArgumentException("Unrecognized scale value: "
			        + scale);
		m_scale = scale;
	}

	/**
	 * Returns the number of "bins" or distinct categories of sizes
	 * 
	 * @return the number of bins.
	 */
	public int getBinCount() {
		return m_bins;
	}

	/**
	 * Sets the number of "bins" or distinct categories of sizes
	 * 
	 * @param count
	 *            the number of bins to set. The value
	 *            {@link Constants#CONTINUOUS} indicates not to use any binning.
	 *            If the scale type set using the {@link #setScale(int)} method
	 *            is {@link Constants#QUANTILE_SCALE}, the bin count
	 *            <strong>must</strong> be greater than zero.
	 */
	public void setBinCount(int count) {
		if (m_scale == Constants.QUANTILE_SCALE && count <= 0) {
			throw new IllegalArgumentException(
			        "The quantile scale can not be used without binning. "
			                + "Use a bin value greater than zero.");
		}
		m_bins = count;
	}

	/**
	 * Indicates if the size values set by this function represent 2D areas.
	 * That is, if the size is a 2D area or a 1D length. The size value will be
	 * scaled appropriately to facilitate better perception of size differences.
	 * 
	 * @return true if this instance is configured for area sizes, false for
	 *         length sizes.
	 * @see prefux.util.PrefuseLib#getSize2D(double)
	 */
	public boolean is2DArea() {
		return m_is2DArea;
	}

	/**
	 * Sets if the size values set by this function represent 2D areas. That is,
	 * if the size is a 2D area or a 1D length. The size value will be scaled
	 * appropriately to facilitate better perception of size differences.
	 * 
	 * @param isArea
	 *            true to configure this instance for area sizes, false for
	 *            length sizes
	 * @see prefux.util.PrefuseLib#getSize2D(double)
	 */
	public void setIs2DArea(boolean isArea) {
		m_is2DArea = isArea;
	}

	/**
	 * Gets the size assigned to the lowest-valued data items, typically 1.0.
	 * 
	 * @return the size for the lowest-valued data items
	 */
	public double getMinimumSize() {
		return m_minSize;
	}

	/**
	 * Sets the size assigned to the lowest-valued data items. By default, this
	 * value is 1.0.
	 * 
	 * @param size
	 *            the new size for the lowest-valued data items
	 */
	public void setMinimumSize(double size) {
		if (Double.isInfinite(size) || Double.isNaN(size) || size <= 0) {
			throw new IllegalArgumentException("Minimum size value must be a "
			        + "finite number greater than zero.");
		}

		if (m_inferRange) {
			m_sizeRange += m_minSize - size;
		}
		m_minSize = size;
	}

	/**
	 * Gets the maximum size value that will be assigned by this action. By
	 * default, the maximum size value is determined automatically from the
	 * data, faithfully representing the scale differences between data values.
	 * However, this can sometimes result in very large differences. For
	 * example, if the minimum data value is 1.0 and the largest is 200.0, the
	 * largest items will be 200 times larger than the smallest. While accurate,
	 * this may not result in the most readable display. To correct these cases,
	 * use the {@link #setMaximumSize(double)} method to manually set the range
	 * of allowed sizes.
	 * 
	 * @return the current maximum size. For the returned value to accurately
	 *         reflect the size range used by this action, either the action
	 *         must have already been run (allowing the value to be
	 *         automatically computed) or the maximum size must have been
	 *         explicitly set.
	 */
	public double getMaximumSize() {
		return m_minSize + m_sizeRange;
	}

	/**
	 * Set the maximum size value that will be assigned by this action. By
	 * default, the maximum size value is determined automatically from the
	 * data, faithfully representing the scale differences between data values.
	 * However, this can sometimes result in very large differences. For
	 * example, if the minimum data value is 1.0 and the largest is 200.0, the
	 * largest items will be 200 times larger than the smallest. While accurate,
	 * this may not result in the most readable display. To correct these cases,
	 * use the {@link #setMaximumSize(double)} method to manually set the range
	 * of allowed sizes.
	 * 
	 * @param maxSize
	 *            the maximum size to use. If this value is less than or equal
	 *            to zero, infinite, or not a number (NaN) then the input value
	 *            will be ignored and instead automatic inference of the size
	 *            range will be performed.
	 */
	public void setMaximumSize(double maxSize) {
		if (Double.isInfinite(maxSize) || Double.isNaN(maxSize) || maxSize <= 0) {
			m_inferRange = true;
		} else {
			m_inferRange = false;
			m_sizeRange = maxSize - m_minSize;
		}
	}

	/**
	 * This operation is not supported by the DataSizeAction type. Calling this
	 * method will result in a thrown exception.
	 * 
	 * @see prefux.action.assignment.SizeAction#setDefaultSize(double)
	 * @throws UnsupportedOperationException
	 */
	public void setDefaultSize(double defaultSize) {
		throw new UnsupportedOperationException();
	}

	// ------------------------------------------------------------------------

	private double[] getDegreeValues(TupleSet ts) {
		double[] values = new double[ts.getTupleCount()];
		Iterator<? extends Tuple> it = ts.tuples();
		int i = 0;
		while (it.hasNext()) {
			Tuple vo = it.next();
			if (vo instanceof NodeItem) {
				if (in && out) {
					values[i] += ((NodeItem) vo).getDegree();
				} else if (in) {
					values[i] += ((NodeItem) vo).getInDegree();
				} else if (out) {
					values[i] += ((NodeItem) vo).getOutDegree();
				}
			}
		}
		return values;
	}

	double min(TupleSet ts) {
		double[] values = getDegreeValues(ts);
		double min = Double.MAX_VALUE;
		for (double val : values) {
			if (val < min) {
				min = val;
			}
		}
		return min;
	}

	double max(TupleSet ts) {
		double[] values = getDegreeValues(ts);
		double max = Double.MIN_VALUE;
		for (double val : values) {
			if (val > max) {
				max = val;
			}
		}
		return max;
	}

	/**
	 * @see prefux.action.EncoderAction#setup()
	 */
	protected void setup() {
		TupleSet ts = m_vis.getGroup(m_group);

		// cache the scale value in case it gets changed due to error
		m_tempScale = m_scale;

		if (m_inferBounds) {
			if (m_scale == Constants.QUANTILE_SCALE && m_bins > 0) {
				double[] values = getDegreeValues(ts);
				m_dist = MathLib.quantiles(m_bins, values);
			} else {
				// check for non-binned quantile scale error
				if (m_scale == Constants.QUANTILE_SCALE) {
					log.warn(
					                "Can't use quantile scale with no binning. "
					                        + "Defaulting to linear scale. Set the bin value "
					                        + "greater than zero to use a quantile scale.");
					m_scale = Constants.LINEAR_SCALE;
				}
				m_dist = new double[2];
				m_dist[0] = min(ts);
				m_dist[1] = max(ts);
			}
			if (m_inferRange) {
				if (m_dist[0] == 0) // Avoid division by 0
					m_sizeRange = m_dist[m_dist.length - 1] - m_minSize;
				else
					m_sizeRange = m_dist[m_dist.length - 1] / m_dist[0]
					        - m_minSize;
			}
		}
	}

	/**
	 * @see prefux.action.EncoderAction#finish()
	 */
	protected void finish() {
		// reset scale in case it needed to be changed due to errors
		m_scale = m_tempScale;
	}

	/**
	 * @see prefux.action.assignment.SizeAction#getSize(prefux.visual.VisualItem)
	 */
	public double getSize(final VisualItem vitem) {
		// check for any cascaded rules first
		double size = super.getSize(vitem);
		if (!Double.isNaN(size)) {
			return size;
		}

		if (vitem instanceof NodeItem) {
			NodeItem item = (NodeItem) vitem;
			// otherwise perform data-driven assignment
			double v;
			if (isIn() && isOut()) {
				v = item.getDegree();
			} else if (isIn()) {
				v = item.getInDegree();
			} else {
				v = item.getOutDegree();
			}
			double f = MathLib.interp(m_scale, v, m_dist);
			if (m_bins < 1) {
				// continuous scale
				v = m_minSize + f * m_sizeRange;
			} else {
				// binned sizes
				int bin = f < 1.0 ? (int) (f * m_bins) : m_bins - 1;
				v = m_minSize + bin * (m_sizeRange / (m_bins - 1));
			}
			// return the size value. if this action is configured to return
			// 2-dimensional sizes (ie area rather than length) then the
			// size value is appropriately scaled first
			return m_is2DArea ? PrefuseLib.getSize2D(v) : v;
		} else {
			log.warn("NodeDegreeSize for item that is no node "+vitem);
			return vitem.getSize();
		}
	}

	public boolean isIn() {
		return in;
	}

	public boolean isOut() {
		return out;
	}



} // end of class DataShapeAction
