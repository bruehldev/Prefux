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
package prefux.util;

import javafx.scene.text.Font;
import prefux.util.collections.IntObjectHashMap;

/**
 * Library maintaining a cache of fonts and other useful font computation
 * routines.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FontLib {

    private static final IntObjectHashMap fontMap = new IntObjectHashMap();
    private static int misses = 0;
    private static int lookups = 0;
    
    
    /**
     * Get a Font instance with the given font family name, style, and size
     * @param name the font name. Any font installed on your system should
     * be valid. Common examples include "Arial", "Verdana", "Tahoma",
     * "Times New Roman", "Georgia", and "Courier New".
     * @param style the font style, such as bold or italics. This field
     * uses the same style values as the Java {@link java.awt.Font} class.
     * @param size the size, in points, of the font
     * @return the requested Font instance
     */
    public static Font getFont(String name, int style, double size) {
        int isize = (int)Math.floor(size);
        return getFont(name, style, isize);
    }
    
    /**
     * Get a Font instance with the given font family name, style, and size
     * @param name the font name. Any font installed on your system should
     * be valid. Common examples include "Arial", "Verdana", "Tahoma",
     * "Times New Roman", "Georgia", and "Courier New".
     * @param style the font style, such as bold or italics. This field
     * uses the same style values as the Java {@link java.awt.Font} class.
     * @param size the size, in points, of the font
     * @return the requested Font instance
     */
    public static Font getFont(String name, int size) {
        int key = (name.hashCode()<<8)+(size<<2);
        Font f = null;
        if ( (f=(Font)fontMap.get(key)) == null ) {
            f = new Font(name, size);
            fontMap.put(key, f);
            misses++;
        }
        lookups++;
        return f;
    }
    
    /**
     * Get the number of cache misses to the Font object cache.
     * @return the number of cache misses
     */
    public static int getCacheMissCount() {
        return misses;
    }
    
    /**
     * Get the number of cache lookups to the Font object cache.
     * @return the number of cache lookups
     */
    public static int getCacheLookupCount() {
        return lookups;
    }
    
    /**
     * Clear the Font object cache.
     */
    public static void clearCache() {
        fontMap.clear();
    }
    
    /**
     * Interpolate between two font instances. Font sizes are interpolated
     * linearly. If the interpolation fraction is under 0.5, the face and
     * style of the starting font are used, otherwise the face and style of
     * the second font are applied.
     * @param f1 the starting font
     * @param f2 the target font
     * @param frac a fraction between 0 and 1.0 controlling the interpolation
     * amount.
     * @return an interpolated Font instance
     */
    public static Font getIntermediateFont(Font f1, Font f2, double frac) {
        String name;
        int size;
        String style;
        if ( frac < 0.5 ) {
            name  = f1.getName();
            style = f1.getStyle();
        } else {
            name  = f2.getName();
            style = f2.getStyle();
        }
        size = (int)Math.round(frac*f2.getSize()+(1-frac)*f1.getSize());
        return getFont(name,size);
    }
    
} // end of class FontLib
