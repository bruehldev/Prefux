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

import java.util.ArrayList;

/**
 * Represents a spring in a force simulation.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class Spring {
    private static SpringFactory s_factory = new SpringFactory();
    
    /**
     * Retrieve the SpringFactory instance, which serves as an object pool
     * for Spring instances.
     * @return the Spring Factory
     */
    public static SpringFactory getFactory() {
        return s_factory;
    }
    
    /**
     * Create a new Spring instance
     * @param fi1 the first ForceItem endpoint
     * @param fi2 the second ForceItem endpoint
     * @param d the spring tension co-efficient
     * @param e the spring's resting length
     */
    public Spring(ForceItem fi1, ForceItem fi2, double d, double e) {
        item1 = fi1;
        item2 = fi2;
        coeff = d;
        length = e;
    }
    
    /** The first ForceItem endpoint */
    public ForceItem item1;
    /** The second ForceItem endpoint */
    public ForceItem item2;
    /** The spring's resting length */
    public double length;
    /** The spring tension co-efficient */
    public double coeff;
    
    /**
     * The SpringFactory is responsible for generating Spring instances
     * and maintaining an object pool of Springs to reduce garbage collection
     * overheads while force simulations are running.
     */
    public static final class SpringFactory {
        private int maxSprings = 10000;
        private ArrayList springs = new ArrayList();
        
        /**
         * Get a Spring instance and set it to the given parameters.
         */
        public Spring getSpring(ForceItem f1, ForceItem f2, double d, double e) {
            if ( springs.size() > 0 ) {
                Spring s = (Spring)springs.remove(springs.size()-1);
                s.item1 = f1;
                s.item2 = f2;
                s.coeff = d;
                s.length = e;
                return s;
            } else {
                return new Spring(f1,f2,d,e);
            }
        }
        /**
         * Reclaim a Spring into the object pool.
         */
        public void reclaim(Spring s) {
            s.item1 = null;
            s.item2 = null;
            if ( springs.size() < maxSprings )
                springs.add(s);
        }
    } // end of inner class SpringFactory

} // end of class Spring
