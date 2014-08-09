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
 * Default LiteralComparator implementation that uses the natural ordering
 * of all data types for comparing values. Object values will need to
 * implement the {@link java.lang.Comparable} interface.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DefaultLiteralComparator implements LiteralComparator {

    // maintain a singleton instance of this class
    private static DefaultLiteralComparator s_instance = null;
    
    /**
     * Returns an instance of this comparator.
     * @return a DefaultLiteralComparator
     */
    public static DefaultLiteralComparator getInstance() {
        if ( s_instance == null )
            s_instance = new DefaultLiteralComparator();
        return s_instance;
    }
    
    /**
     * @see prefux.util.collections.LiteralComparator#compare(byte, byte)
     */
    public int compare(byte x1, byte x2) {
        return ( x1 < x2 ? -1 : x1 > x2 ? 1 : 0 );
    }
    
    /**
     * @see prefux.util.collections.LiteralComparator#compare(int, int)
     */
    public int compare(int x1, int x2) {
        return ( x1 < x2 ? -1 : x1 > x2 ? 1 : 0 );
    }

    /**
     * @see prefux.util.collections.LiteralComparator#compare(long, long)
     */
    public int compare(long x1, long x2) {
        return ( x1 < x2 ? -1 : x1 > x2 ? 1 : 0 );
    }

    /**
     * @see prefux.util.collections.LiteralComparator#compare(float, float)
     */
    public int compare(float x1, float x2) {
        return Float.compare(x1, x2);
    }

    /**
     * @see prefux.util.collections.LiteralComparator#compare(double, double)
     */
    public int compare(double x1, double x2) {
        return Double.compare(x1, x2);
    }

    /**
     * @see prefux.util.collections.LiteralComparator#compare(boolean, boolean)
     */
    public int compare(boolean x1, boolean x2) {
        return ( x1 ? (x2 ? 0 : 1) : (x2 ? -1 : 0) );
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        if ( o1 == null ) {
            return ( o2 == null ? 0 : -1 );
        } else if ( o2 == null ) {
            return 1;
        }
        
        if ( o1 instanceof Comparable ) {
            return ((Comparable)o1).compareTo(o2);
        } else if ( o2 instanceof Comparable ) {
            return -1*((Comparable)o2).compareTo(o1);
        } else if ( o1 instanceof Boolean && o2 instanceof Boolean ) {
            // unfortunate hack necessary for Java 1.4 compatibility
            return compare(((Boolean)o1).booleanValue(), ((Boolean)o2).booleanValue());
        } else {
            throw new IllegalArgumentException("Incomparable arguments.");
        }
    }

} // end of class DefaultLiteralComparator
