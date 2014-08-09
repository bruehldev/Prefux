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
package prefux.util.force;

import java.awt.geom.Line2D;

/**
 * Uses a gravitational force model to act as a "wall". Can be used to
 * construct line segments which either attract or repel items.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class WallForce extends AbstractForce {

    private static String[] pnames = new String[] { "GravitationalConstant" };
    
    public static final double DEFAULT_GRAV_CONSTANT = -0.1f;
    public static final double DEFAULT_MIN_GRAV_CONSTANT = -1.0f;
    public static final double DEFAULT_MAX_GRAV_CONSTANT = 1.0f;
    public static final int GRAVITATIONAL_CONST = 0;
    
    private double x1, y1, x2, y2;
    private double dx, dy;
    
    /**
     * Create a new WallForce.
     * @param gravConst the gravitational constant of the wall
     * @param x1 the first x-coordinate of the wall
     * @param y1 the first y-coordinate of the wall
     * @param x2 the second x-coordinate of the wall
     * @param y2 the second y-coordinate of the wall
     */
    public WallForce(double gravConst, 
        double x1, double y1, double x2, double y2) 
    {
        params = new double[] { gravConst };
        minValues = new double[] { DEFAULT_MIN_GRAV_CONSTANT };
        maxValues = new double[] { DEFAULT_MAX_GRAV_CONSTANT };
        
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        dx = x2-x1;
        dy = y2-y1;
        double r = (double)Math.sqrt(dx*dx+dy*dy);
        if ( dx != 0.0 ) dx /= r;
        if ( dy != 0.0 ) dy /= r;
    }
    
    /**
     * Create a new WallForce with default gravitational constant.
     * @param x1 the first x-coordinate of the wall
     * @param y1 the first y-coordinate of the wall
     * @param x2 the second x-coordinate of the wall
     * @param y2 the second y-coordinate of the wall
     */
    public WallForce(double x1, double y1, double x2, double y2) {
        this(DEFAULT_GRAV_CONSTANT,x1,y1,x2,y2);
    }
    
    /**
     * Returns true.
     * @see prefux.util.force.Force#isItemForce()
     */
    public boolean isItemForce() {
        return true;
    }
    
    /**
     * @see prefux.util.force.AbstractForce#getParameterNames()
     */
    protected String[] getParameterNames() {
        return pnames;
    }
    
    /**
     * @see prefux.util.force.Force#getForce(prefux.util.force.ForceItem)
     */
    public void getForce(ForceItem item) {
        double[] n = item.location;
        int ccw = Line2D.relativeCCW(x1,y1,x2,y2,n[0],n[1]);
        double r = (double)Line2D.ptSegDist(x1,y1,x2,y2,n[0],n[1]);
        if ( r == 0.0 ) r = (double)Math.random() / 100.0f;
        double v = params[GRAVITATIONAL_CONST]*item.mass / (r*r*r);
        if ( n[0] >= Math.min(x1,x2) && n[0] <= Math.max(x1,x2) )
            item.force[1] += ccw*v*dx;
        if ( n[1] >= Math.min(y1,y2) && n[1] <= Math.max(y1,y2) )
            item.force[0] += -1*ccw*v*dy;
    }

} // end of class WallForce
