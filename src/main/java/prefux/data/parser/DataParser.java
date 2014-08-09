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
 * Interface for data parsers, which parse data values from text Strings
 * and generated formatted text Strings for data values.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface DataParser {
    
    /**
     * Get the data type for the values parsed by this parser.
     * @return the parsed data type for this parser as a Java Class instance
     */
    public Class getType();
    
    
    /**
     * Get a String representation for the given value.
     * @param value the object value to format
     * @return a formatted String representing the input value
     */
    public String format(Object value);
    
    /**
     * Indicates if the given text string can be successfully parsed by
     * this parser.
     * @param text the text string to check for parsability
     * @return true if the string can be successfully parsed into this
     * parser's data type, false otherwise
     */
    public boolean canParse(String text);
    
    /**
     * Parse the given text string to a data value.
     * @param text the text string to parse
     * @return the parsed data value, which will be an instance of the
     * Class returned by the {@link #getType()} method
     * @throws DataParseException if an error occurs during parsing
     */
    public Object parse(String text) throws DataParseException; 
   
} // end of interface DataParser
