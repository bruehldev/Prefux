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

/**
 * DataParser instance that "parses" a String value from a text string, this
 * is the default fallback parser, which simply returns the string value
 * to be parsed.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class StringParser implements DataParser {
    
    /**
     * Returns String.class.
     * @see prefux.data.parser.DataParser#getType()
     */
    public Class getType() {
        return String.class;
    }
    
    /**
     * @see prefux.data.parser.DataParser#format(java.lang.Object)
     */
    public String format(Object value) {
        if ( value == null ) return null;
        if ( !(value instanceof String) )
            throw new IllegalArgumentException(
              "This class can only format Objects of type String.");
        return (String)value;
    }
    
    /**
     * @see prefux.data.parser.DataParser#canParse(java.lang.String)
     */
    public boolean canParse(String text) {
        return true;
    }
    
    /**
     * @see prefux.data.parser.DataParser#parse(java.lang.String)
     */
    public Object parse(String text) throws DataParseException {
        return text;
    }
    
    /**
     * Simply returns the input string.
     * @param text the text string to "parse"
     * @return the input text string
     * @throws DataParseException never actually throws an exception
     */
    public String parseString(String text) throws DataParseException {
        return text;
    }
    
} // end of class StringParser
