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
package prefux.util.ui;

import javax.swing.BoundedRangeModel;

/**
 * BoundedRangeModel that additionally supports a mapping between the integer
 * range used by interface components and a richer range of values, such
 * as numbers or arbitrary objects.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @see javax.swing.BoundedRangeModel
 */
public interface ValuedRangeModel extends BoundedRangeModel {

    /**
     * Get the minimum value backing the range model. This is
     * the absolute minimum value possible for the range span.
     * @return the minimum value
     */
    public Object getMinValue();

    /**
     * Get the maximum value backing the range model. This is
     * the absolute maximum value possible for the range span.
     * @return the maximum value
     */
    public Object getMaxValue();
    
    /**
     * Get the value at the low point of the range span.
     * @return the lowest value of the current range
     */
    public Object getLowValue();

    /**
     * Get the value at the high point of the range span.
     * @return the highest value of the current range
     */
    public Object getHighValue();
    
} // end of interface ValuedRangeModel
