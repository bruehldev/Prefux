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
package prefux.util;

import java.awt.BasicStroke;

import prefux.util.collections.IntObjectHashMap;

/**
 * Library maintaining a cache of drawing strokes and other useful stroke
 * computation routines.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class StrokeLib {

    private static final IntObjectHashMap strokeMap = new IntObjectHashMap();
    private static int misses = 0;
    private static int lookups = 0;

    /** Dash pattern for a dotted line */
    public static final float[] DOTS = new float[] { 1.0f, 2.0f };
    /** Dash pattern for regular uniform dashes */
    public static final float[] DASHES = new float[] { 5.0f, 5.0f };
    /** Dash pattern for longer uniform dashes */
    public static final float[] LONG_DASHES = new float[] { 10.0f, 10.0f };
    
    /**
     * Get a square capped, miter joined, non-dashed stroke of the given width.
     * @param width the requested stroke width
     * @return the stroke
     */
    public static BasicStroke getStroke(float width) {
        return getStroke(width,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER);
    }
    
    /**
     * Get a square capped, miter joined, dashed stroke with the given width
     * and dashing attributes.
     * @param width the requested stroke width
     * @param dashes an array describing the alternation pattern of
     * a dashed line. For example [5f, 3f] will create dashes of length 5
     * with spaces of length 3 between them. A null value indicates no
     * dashing.
     * @return the stroke
     * @see java.awt.BasicStroke
     */
    public static BasicStroke getStroke(float width, float[] dashes) {
        return getStroke(width, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
                         10.0f, dashes, 0f);
    }
    
    /**
     * Get a non-dashed stroke of the given width, cap, and join
     * @param width the requested stroke width
     * @param cap the requested cap type, one of
     * {@link java.awt.BasicStroke#CAP_BUTT},
     * {@link java.awt.BasicStroke#CAP_ROUND}, or
     * {@link java.awt.BasicStroke#CAP_SQUARE}
     * @param join the requested join type, one of
     * {@link java.awt.BasicStroke#JOIN_BEVEL},
     * {@link java.awt.BasicStroke#JOIN_MITER}, or
     * {@link java.awt.BasicStroke#JOIN_ROUND} 
     * @return the stroke
     */
    public static BasicStroke getStroke(float width, int cap, int join) {
        return getStroke(width, cap, join, 10.0f, null, 0f);
    }
    
    /**
     * Get a dashed stroke of the given width, cap, join, miter limit,
     * and dashing attributes.
     * @param width the requested stroke width
     * @param cap the requested cap type, one of
     * {@link java.awt.BasicStroke#CAP_BUTT},
     * {@link java.awt.BasicStroke#CAP_ROUND}, or
     * {@link java.awt.BasicStroke#CAP_SQUARE}
     * @param join the requested join type, one of
     * {@link java.awt.BasicStroke#JOIN_BEVEL},
     * {@link java.awt.BasicStroke#JOIN_MITER}, or
     * {@link java.awt.BasicStroke#JOIN_ROUND} 
     * @param miterLimit the miter limit at which to bevel miter joins
     * @param dashes an array describing the alternation pattern of
     * a dashed line. For example [5f, 3f] will create dashes of length 5
     * with spaces of length 3 between them. A null value indicates no
     * dashing.
     * @param dashPhase the phase or offset from which to begin the
     * dash pattern
     * @return the stroke
     * @see java.awt.BasicStroke
     */
    public static BasicStroke getStroke(float width, int cap, int join,
            float miterLimit, float[] dashes, float dashPhase)
    {
        int key = getStrokeKey(width,cap,join,miterLimit,dashes,dashPhase);
        BasicStroke s = null;
        if ( (s=(BasicStroke)strokeMap.get(key)) == null ) {
            s = new BasicStroke(width, cap, join, 
                                miterLimit, dashes, dashPhase);
            strokeMap.put(key, s);
            ++misses;
        }
        ++lookups;
        return s;
    }
    
    /**
     * Compute a hash-key for stroke storage and lookup.
     */
    protected static int getStrokeKey(float width, int cap, int join,
            float miterLimit, float[] dashes, float dashPhase)
    {
        int hash = Float.floatToIntBits(width);
        hash = hash * 31 + join;
        hash = hash * 31 + cap;
        hash = hash * 31 + Float.floatToIntBits(miterLimit);
        if (dashes != null) {
            hash = hash * 31 + Float.floatToIntBits(dashPhase);
            for (int i = 0; i < dashes.length; i++) {
                hash = hash * 31 + Float.floatToIntBits(dashes[i]);
            }
        }
        return hash;
    }
    
    /**
     * Get a stroke with the same properties as the given stroke, but with
     * a modified width value.
     * @param s the stroke to base the returned stroke on
     * @param width the desired width of the derived stroke
     * @return the derived Stroke
     */
    public static BasicStroke getDerivedStroke(BasicStroke s, float width) {
        if ( s.getLineWidth() == width ) {
            return s;
        } else {
            return getStroke(width*s.getLineWidth(), s.getEndCap(),
                    s.getLineJoin(), s.getMiterLimit(),
                    s.getDashArray(), s.getDashPhase());
        }
    }
    
    /**
     * Get the number of cache misses to the Stroke object cache.
     * @return the number of cache misses
     */
    public static int getCacheMissCount() {
        return misses;
    }
    
    /**
     * Get the number of cache lookups to the Stroke object cache.
     * @return the number of cache lookups
     */
    public static int getCacheLookupCount() {
        return lookups;
    }
    
    /**
     * Clear the Stroke object cache.
     */
    public static void clearCache() {
        strokeMap.clear();
    }    
    
} // end of class StrokeLib
