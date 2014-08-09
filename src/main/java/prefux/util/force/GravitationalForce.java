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

/**
 * Represents a constant gravitational force, like the pull of gravity
 * for an object on the Earth (F = mg). The force experienced by a
 * given item is calculated as the product of a GravitationalConstant 
 * parameter and the mass of the item.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GravitationalForce extends AbstractForce {

    private static final String[] pnames
        = { "GravitationalConstant", "Direction" };
    
    public static final int GRAVITATIONAL_CONST = 0;
    public static final int DIRECTION = 1;
    
    public static final double DEFAULT_FORCE_CONSTANT = 1E-4f;
    public static final double DEFAULT_MIN_FORCE_CONSTANT = 1E-5f;
    public static final double DEFAULT_MAX_FORCE_CONSTANT = 1E-3f;
    
    public static final double DEFAULT_DIRECTION = (double)-Math.PI/2;
    public static final double DEFAULT_MIN_DIRECTION = (double)-Math.PI;
    public static final double DEFAULT_MAX_DIRECTION = (double)Math.PI;
    
    /**
     * Create a new GravitationForce.
     * @param forceConstant the gravitational constant to use
     * @param direction the direction in which gravity should act,
     * in radians.
     */
    public GravitationalForce(double forceConstant, double direction) {
        params = new double[] { forceConstant, direction };
        minValues = new double[] 
            { DEFAULT_MIN_FORCE_CONSTANT, DEFAULT_MIN_DIRECTION };
        maxValues = new double[] 
            { DEFAULT_MAX_FORCE_CONSTANT, DEFAULT_MAX_DIRECTION };
    }
    
    /**
     * Create a new GravitationalForce with default gravitational
     * constant and direction.
     */
    public GravitationalForce() {
        this(DEFAULT_FORCE_CONSTANT, DEFAULT_DIRECTION);
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
        double theta = params[DIRECTION];
        double coeff = params[GRAVITATIONAL_CONST]*item.mass;
        
        item.force[0] += Math.cos(theta)*coeff;
        item.force[1] += Math.sin(theta)*coeff;
    }

} // end of class GravitationalForce
