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
package prefux.util.collections;

/**
 * Abstract LiteralIterator implementation that supports an iteration over
 * int values. Subclasses need only implement the {@link #nextInt()} method.
 * The {@link #nextLong()}, {@link #nextFloat()}, and {@link #nextDouble()}
 * methods all simply cast the output of {@link #nextInt()}. The
 * {@link #next()} method simply wraps the output of {@link #nextInt()} in
 * an {@link java.lang.Integer} object.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class IntIterator extends AbstractLiteralIterator {

    /**
     * @see java.util.Iterator#next()
     */
    public Object next() {
        return new Integer(nextInt());
    }

    /**
     * @see prefux.util.collections.LiteralIterator#isDoubleSupported()
     */
    public boolean isDoubleSupported() {
        return true;
    }

    /**
     * @see prefux.util.collections.LiteralIterator#isFloatSupported()
     */
    public boolean isFloatSupported() {
        return true;
    }

    /**
     * @see prefux.util.collections.LiteralIterator#isIntSupported()
     */
    public boolean isIntSupported() {
        return true;
    }

    /**
     * @see prefux.util.collections.LiteralIterator#isLongSupported()
     */
    public boolean isLongSupported() {
        return true;
    }

    /**
     * @see prefux.util.collections.LiteralIterator#nextDouble()
     */
    public double nextDouble() {
        return nextInt();
    }

    /**
     * @see prefux.util.collections.LiteralIterator#nextFloat()
     */
    public float nextFloat() {
        return nextInt();
    }

    /**
     * @see prefux.util.collections.LiteralIterator#nextLong()
     */
    public long nextLong() {
        return nextInt();
    }

    /**
     * @see prefux.util.collections.LiteralIterator#nextInt()
     */
    public abstract int nextInt();
    
} // end of abstract class IntIterator
