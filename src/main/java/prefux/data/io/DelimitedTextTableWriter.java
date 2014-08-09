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

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import prefux.data.Table;
import prefux.util.collections.IntIterator;

/**
 * TableWriter that writes out a delimited text table, using a designated
 * character string to demarcate data columns. By default, a header row
 * containing the column names is included in the output.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DelimitedTextTableWriter extends AbstractTableWriter {

    private String  m_delim;
    private boolean m_printHeader;
    
    /**
     * Create a new DelimitedTextTableWriter that writes tab-delimited
     * text files.
     */
    public DelimitedTextTableWriter() {
        this("\t");
    }
    
    /**
     * Create a new DelimitedTextTableWriter.
     * @param delimiter the delimiter string to use between columns
     */
    public DelimitedTextTableWriter(String delimiter) {
        this(delimiter, true);
    }
    
    /**
     * Create a new DelimitedTextTableWriter.
     * @param delimiter the delimiter string to use between columns
     * @param printHeader indicates if a header row should be printed
     */
    public DelimitedTextTableWriter(String delimiter, boolean printHeader) {
        m_delim = delimiter;
        m_printHeader = printHeader;
    }

    // ------------------------------------------------------------------------    
    
    /**
     * Get the delimiter used to separate data fields.
     * @return the delimiter string
     */
    public String getDelimiter() {
        return m_delim;
    }

    /**
     * Set the delimiter used to separate data fields.
     * @param delimiter the delimiter string
     */
    public void setDelimeter(String delimiter) {
        m_delim = delimiter;
    }

    /**
     * Indicates if this writer will write a header row with the column names.
     * @return true if a header row will be printed, false otherwise
     */
    public boolean isPrintHeader() {
        return m_printHeader;
    }

    /**
     * Sets if this writer will write a header row with the column names.
     * @param printHeader true to print a header row, false otherwise
     */
    public void setPrintHeader(boolean printHeader) {
        m_printHeader = printHeader;
    }    
    
    // ------------------------------------------------------------------------

    /**
     * @see prefux.data.io.TableWriter#writeTable(prefux.data.Table, java.io.OutputStream)
     */
    public void writeTable(Table table, OutputStream os) throws DataIOException {
        try {            
            // get print stream
            PrintStream out = new PrintStream(new BufferedOutputStream(os));
            
            // write out header row
            if ( m_printHeader ) {
                for ( int i=0; i<table.getColumnCount(); ++i ) {
                    if ( i>0 ) out.print(m_delim);
                    out.print(table.getColumnName(i));
                }
                out.println();
            }
            
            // write out data
            for ( IntIterator rows = table.rows(); rows.hasNext(); ) {
                int row = rows.nextInt();
                for ( int i=0; i<table.getColumnCount(); ++i ) {
                    if ( i>0 ) out.print(m_delim);
                    out.print(table.getString(row, table.getColumnName(i)));
                }
                out.println();
            }
            
            // finish up
            out.flush();
        } catch ( Exception e ) {
            throw new DataIOException(e);
        }
    }

} // end of class DelimitedTextTableWriter
