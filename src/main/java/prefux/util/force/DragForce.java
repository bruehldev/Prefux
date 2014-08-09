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
 * Implements a viscosity/drag force to help stabilize items.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DragForce extends AbstractForce {

    private static String[] pnames = new String[] { "DragCoefficient" };
    
    public static final double DEFAULT_DRAG_COEFF = 0.01;
    public static final double DEFAULT_MIN_DRAG_COEFF = 0.0;
    public static final double DEFAULT_MAX_DRAG_COEFF = 0.1;
    public static final int DRAG_COEFF = 0;

    /**
     * Create a new DragForce.
     * @param dragCoeff the drag co-efficient
     */
    public DragForce(double dragCoeff) {
        params = new double[] { dragCoeff };
        minValues = new double[] { DEFAULT_MIN_DRAG_COEFF };
        maxValues = new double[] { DEFAULT_MAX_DRAG_COEFF };
    }

    /**
     * Create a new DragForce with default drag co-efficient.
     */
    public DragForce() {
        this(DEFAULT_DRAG_COEFF);
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
        item.force[0] -= params[DRAG_COEFF]*item.velocity[0];
        item.force[1] -= params[DRAG_COEFF]*item.velocity[1];
    }

} // end of class DragForce
