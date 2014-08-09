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
 * Force function that computes the force acting on ForceItems due to a
 * given Spring.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class SpringForce extends AbstractForce {

    private static String[] pnames 
        = new String[] { "SpringCoefficient", "DefaultSpringLength" };
    
    public static final double DEFAULT_SPRING_COEFF = 1E-4f;
    public static final double DEFAULT_MAX_SPRING_COEFF = 1E-3f;
    public static final double DEFAULT_MIN_SPRING_COEFF = 1E-5f;
    public static final double DEFAULT_SPRING_LENGTH = 50;
    public static final double DEFAULT_MIN_SPRING_LENGTH = 0;
    public static final double DEFAULT_MAX_SPRING_LENGTH = 200;
    public static final int SPRING_COEFF = 0;
    public static final int SPRING_LENGTH = 1;
    
    /**
     * Create a new SpringForce.
     * @param springCoeff the default spring co-efficient to use. This will
     * be used if the spring's own co-efficient is less than zero.
     * @param defaultLength the default spring length to use. This will
     * be used if the spring's own length is less than zero.
     */
    public SpringForce(double springCoeff, double defaultLength) {
        params = new double[] { springCoeff, defaultLength };
        minValues = new double[] 
            { DEFAULT_MIN_SPRING_COEFF, DEFAULT_MIN_SPRING_LENGTH };
        maxValues = new double[] 
            { DEFAULT_MAX_SPRING_COEFF, DEFAULT_MAX_SPRING_LENGTH };
    }
    
    /**
     * Constructs a new SpringForce instance with default parameters.
     */
    public SpringForce() {
        this(DEFAULT_SPRING_COEFF, DEFAULT_SPRING_LENGTH);
    }

    /**
     * Returns true.
     * @see prefux.util.force.Force#isSpringForce()
     */
    public boolean isSpringForce() {
        return true;
    }
    
    /**
     * @see prefux.util.force.AbstractForce#getParameterNames()
     */
    protected String[] getParameterNames() {
        return pnames;
    } 
    
    /**
     * Calculates the force vector acting on the items due to the given spring.
     * @param s the Spring for which to compute the force
     * @see prefux.util.force.Force#getForce(prefux.util.force.Spring)
     */
    public void getForce(Spring s) {
        ForceItem item1 = s.item1;
        ForceItem item2 = s.item2;
        double length = (s.length < 0 ? params[SPRING_LENGTH] : s.length);
        double x1 = item1.location[0], y1 = item1.location[1];
        double x2 = item2.location[0], y2 = item2.location[1];
        double dx = x2-x1, dy = y2-y1;
        double r  = (double)Math.sqrt(dx*dx+dy*dy);
        if ( r == 0.0 ) {
            dx = ((double)Math.random()-0.5f) / 50.0f;
            dy = ((double)Math.random()-0.5f) / 50.0f;
            r  = (double)Math.sqrt(dx*dx+dy*dy);
        }
        double d  = r-length;
        double coeff = (s.coeff < 0 ? params[SPRING_COEFF] : s.coeff)*d/r;
        item1.force[0] += coeff*dx;
        item1.force[1] += coeff*dy;
        item2.force[0] += -coeff*dx;
        item2.force[1] += -coeff*dy;
    }
    
} // end of class SpringForce
