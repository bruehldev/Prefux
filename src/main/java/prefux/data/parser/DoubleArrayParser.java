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
package prefux.data.parser;

import java.util.StringTokenizer;

/**
 * DataParser instance the parses an array of double values from a text string.
 * Values are expected to be comma separated and can be within brackets,
 * parentheses, or curly braces.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DoubleArrayParser implements DataParser {
    
    /**
     * Returns double[].class.
     * @see prefux.data.parser.DataParser#getType()
     */
    public Class getType() {
        return double[].class;
    }
    
    /**
     * @see prefux.data.parser.DataParser#format(java.lang.Object)
     */
    public String format(Object value) {
        if ( value == null ) return null;
        if ( !(value instanceof double[]) )
            throw new IllegalArgumentException(
              "This class can only format Objects of type double[].");
        
        double[] values = (double[])value;
        StringBuffer sbuf = new StringBuffer();
        sbuf.append('[');
        for ( int i=0; i<values.length; ++i ) {
            if ( i > 0 ) sbuf.append(", ");
            sbuf.append(values[i]);
        }
        sbuf.append(']');
        return sbuf.toString();
    }
    
    /**
     * @see prefux.data.parser.DataParser#canParse(java.lang.String)
     */
    public boolean canParse(String text) {
        try {
            StringTokenizer st = new StringTokenizer(text, "\"[](){}, ");
            while ( st.hasMoreTokens() ) {
                Double.parseDouble(st.nextToken());
            }
            return true;
        } catch ( NumberFormatException e ) {
            return false;
        }
    }
    
    /**
     * Parse an int array from a text string.
     * @param text the text string to parse
     * @return the parsed integer array
     * @throws DataParseException if an error occurs during parsing
     */
    public Object parse(String text) throws DataParseException {
        try {
            StringTokenizer st = new StringTokenizer(text, "\"[](){}, ");
            double[] array = new double[st.countTokens()];
            for ( int i=0; st.hasMoreTokens(); ++i ) {
                String tok = st.nextToken();
                array[i] = Double.parseDouble(tok);
            }
            return array;
        } catch ( NumberFormatException e ) {
            throw new DataParseException(e);
        }
    }
    
} // end of class DoubleArrayParser
