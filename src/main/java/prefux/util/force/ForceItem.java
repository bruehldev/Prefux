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
 * Represents a point particle in a force simulation, maintaining values for
 * mass, forces, velocity, and position.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ForceItem implements Cloneable {
    
    /**
     * Create a new ForceItem.
     */
    public ForceItem() {
        mass = 1.0;
        force = new double[] { 0., 0. };
        velocity = new double[] { 0., 0. };
        location = new double[] { 0., 0. };
        plocation = new double[] { 0., 0. };
        k = new double[4][2];
        l = new double[4][2];
    }
    
    /**
     * Clone a ForceItem.
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        ForceItem item = new ForceItem();
        item.mass = this.mass;
        System.arraycopy(force,0,item.force,0,2);
        System.arraycopy(velocity,0,item.velocity,0,2);
        System.arraycopy(location,0,item.location,0,2);
        System.arraycopy(plocation,0,item.plocation,0,2);
        for ( int i=0; i<k.length; ++i ) {
            System.arraycopy(k[i],0,item.k[i],0,2);
            System.arraycopy(l[i],0,item.l[i],0,2);
        }
        return item;
    }
    
    /** The mass value of this ForceItem. */
    public double   mass;
    /** The values of the forces acting on this ForceItem. */
    public double[] force;
    /** The velocity values of this ForceItem. */
    public double[] velocity;
    /** The location values of this ForceItem. */
    public double[] location;
    /** The previous location values of this ForceItem. */
    public double[] plocation;
    /** Temporary variables for Runge-Kutta integration */
    public double[][] k;
    /** Temporary variables for Runge-Kutta integration */
    public double[][] l;
    
    /**
     * Checks a ForceItem to make sure its values are all valid numbers
     * (i.e., not NaNs).
     * @param item the item to check
     * @return true if all the values are valid, false otherwise
     */
    public static final boolean isValid(ForceItem item) {
        return
          !( Double.isNaN(item.location[0])  || Double.isNaN(item.location[1])  || 
             Double.isNaN(item.plocation[0]) || Double.isNaN(item.plocation[1]) ||
             Double.isNaN(item.velocity[0])  || Double.isNaN(item.velocity[1])  ||
             Double.isNaN(item.force[0])     || Double.isNaN(item.force[1]) );
    }
    
} // end of class ForceItem
