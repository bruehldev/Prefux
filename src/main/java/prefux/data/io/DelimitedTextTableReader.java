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
package prefux.data.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import prefux.data.parser.DataParseException;
import prefux.data.parser.ParserFactory;

/**
 * TableReader for delimited text files, such as tab-delimited or
 * pipe-delimited text files. Such files typically list one row of table
 * data per line of the file, using a designated character such as a tab
 * (\t) or pipe (|) to demarcate different data columns. This class
 * allows you to select any regular expression as the column
 * delimiter.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DelimitedTextTableReader extends AbstractTextTableReader {

    private String m_delim;
   
    /**
     * Create a new DelimitedTextTableReader for reading tab-delimited files
     * using a default parser factory.
     */
    public DelimitedTextTableReader() {
        this("\t");
    }
    
    /**
     * Create a new DelimitedTextTableReader for reading tab-delimited files.
     * @param parserFactory the ParserFactory to use for parsing text strings
     * into table values.
     */
    public DelimitedTextTableReader(ParserFactory parserFactory) {
        this("\t", parserFactory);
    }
    
    /**
     * Create a new DelimitedTextTableReader using a default parser factory.
     * @param delimiterRegex a regular expression string indicating the
     * delimiter to use to separate column values
     */
    public DelimitedTextTableReader(String delimiterRegex) {
        m_delim = delimiterRegex;
    }
    
    /**
     * Create a new DelimitedTextTableReader.
     * @param delimiterRegex a regular expression string indicating the
     * delimiter to use to separate column values
     * @param pf the ParserFactory to use for parsing text strings
     * into table values.
     */
    public DelimitedTextTableReader(String delimiterRegex, ParserFactory pf) {
        super(pf);
        m_delim = delimiterRegex;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.data.io.AbstractTextTableReader#read(java.io.InputStream, prefux.data.io.TableReadListener)
     */
    protected void read(InputStream is, TableReadListener trl)
            throws IOException, DataParseException
    {
        String line;
        int lineno   = 0;
        
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ( (line=br.readLine()) != null ) {
            // increment the line number
            ++lineno;
            
            // split on tab character
            String[] cols = line.split(m_delim);
            for ( int i=0; i<cols.length; ++i ) {
                trl.readValue(lineno, i+1, cols[i]);
            }
        }
    }

} // end of class DelimitedTextTableReader
