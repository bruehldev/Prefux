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
 * Abstract base class for force functions in a force simulation. This
 * skeletal version provides support for storing and retrieving double-valued
 * parameters of the force function. Subclasses should use the protected
 * field <code>params</code> to store parameter values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractForce implements Force {

    protected double[] params;
    protected double[] minValues;
    protected double[] maxValues;

    /**
     * Initialize this force function. This default implementation does nothing.
     * Subclasses should override this method with any needed initialization.
     * @param fsim the encompassing ForceSimulator
     */
    public void init(ForceSimulator fsim) {
        // do nothing.
    }

    /**
     * @see prefux.util.force.Force#getParameterCount()
     */
    public int getParameterCount() {
        return ( params == null ? 0 : params.length );
    }

    /**
     * @see prefux.util.force.Force#getParameter(int)
     */
    public double getParameter(int i) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return params[i];
        }
    }
    
    /**
     * @see prefux.util.force.Force#getMinValue(int)
     */
    public double getMinValue(int i) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return minValues[i];
        }
    }
    
    /**
     * @see prefux.util.force.Force#getMaxValue(int)
     */
    public double getMaxValue(int i) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return maxValues[i];
        }
    }
    
    /**
     * @see prefux.util.force.Force#getParameterName(int)
     */
    public String getParameterName(int i) {
        String[] pnames = getParameterNames();
        if ( i < 0 || pnames == null || i >= pnames.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return pnames[i];
        }
    }

    /**
     * @see prefux.util.force.Force#setParameter(int, double)
     */
    public void setParameter(int i, double val) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            params[i] = val;
        }
    }
    
    /**
     * @see prefux.util.force.Force#setMinValue(int, double)
     */
    public void setMinValue(int i, double val) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            minValues[i] = val;
        }
    }
    
    /**
     * @see prefux.util.force.Force#setMaxValue(int, double)
     */
    public void setMaxValue(int i, double val) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            maxValues[i] = val;
        }
    }
    
    protected abstract String[] getParameterNames();
    
    /**
     * Returns false.
     * @see prefux.util.force.Force#isItemForce()
     */
    public boolean isItemForce() {
        return false;
    }
    
    /**
     * Returns false.
     * @see prefux.util.force.Force#isSpringForce()
     */
    public boolean isSpringForce() {
        return false;
    }
    
    /**
     * Throws an UnsupportedOperationException.
     * @see prefux.util.force.Force#getForce(prefux.util.force.ForceItem)
     */
    public void getForce(ForceItem item) {
        throw new UnsupportedOperationException(
            "This class does not support this operation");
    }
    
    /**
     * Throws an UnsupportedOperationException.
     * @see prefux.util.force.Force#getForce(prefux.util.force.Spring)
     */
    public void getForce(Spring spring) {
        throw new UnsupportedOperationException(
            "This class does not support this operation");
    }
    
} // end of abstract class AbstractForce
