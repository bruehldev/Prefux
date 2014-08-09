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
package prefux.activity;

/**
 * A pacing function that provides slow-in, slow-out animation, where the
 * animation begins at a slower rate, speeds up through the middle of the
 * animation, and then slows down again before stopping.
 * 
 * This is calculated by using an appropriately phased sigmoid function of
 * the form 1/(1+exp(-x)).
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class SlowInSlowOutPacer implements Pacer {
    
    /**
     * Pacing function providing slow-in, slow-out animation
     * @see prefux.activity.Pacer#pace(double)
     */
    public double pace(double f) {
        return ( f == 0.0 || f == 1.0 ? f : sigmoid(f) );
    }
    
    /**
     * Computes a normalized sigmoid
     * @param x input value in the interval [0,1]
     */
    private double sigmoid(double x) {
        x = 12.0*x - 6.0;
        return (1.0 / (1.0 + Math.exp(-1.0 * x)));
    }

} // end of class SlowInSlowOutPacer
