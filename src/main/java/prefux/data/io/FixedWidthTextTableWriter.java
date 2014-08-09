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
import prefux.util.TypeLib;
import prefux.util.collections.IntIterator;

/**
 * TableWriter for fixed-width text files, that encode one row of table
 * data per line use a fixed number of characters for each data column.
 * Writing such tables requires use of a schema description that describes
 * the fixed-widths for each individual column.
 * The {@link prefux.data.io.FixedWidthTextTableSchema} class provides
 * this functionality. A schema description must be written separately into
 * a different file.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FixedWidthTextTableWriter extends AbstractTableWriter {

	// the schema description
	private FixedWidthTextTableSchema m_schema;
	
	/**
	 * Creates a new FixedWidthTextTableWriter using the given schema.
	 * @param schema the schema description of the fixed-width text column lengths
	 */
	public FixedWidthTextTableWriter(FixedWidthTextTableSchema schema) {
		m_schema = schema;
	}
	
	/**
	 * Creates a new FixedWidthTextTableWriter using the schema at
	 * the given location.
	 * @param location a location string (filename, URL, or resource 
	 * locator) for the schema description of the fixed-width text column lengths
	 * @throws DataIOException if an IO exception occurs while loading the schema
	 */
	public FixedWidthTextTableWriter(String location) throws DataIOException {
		this(FixedWidthTextTableSchema.load(location));
	}
	
	// ------------------------------------------------------------------------    
    
    /**
     * Get the schema description describing the data columns' fixed widths
     * @return the fixed-width table schema description
     */
    public FixedWidthTextTableSchema getFixedWidthSchema() {
        return m_schema;
    }

    /**
     * Set the schema description describing the data columns' fixed widths
     * @param schema the fixed-width table schema description
     */
    public void setFixedWidthSchema(FixedWidthTextTableSchema schema) {
        m_schema = schema;
    }
    
	// ------------------------------------------------------------------------

    /**
     * @see prefux.data.io.TableWriter#writeTable(prefux.data.Table, java.io.OutputStream)
     */
    public void writeTable(Table table, OutputStream os) throws DataIOException {
        try {            
            // get print stream
            PrintStream out = new PrintStream(new BufferedOutputStream(os));
            
            // build array of column padding
            char[] pad = new char[table.getColumnCount()];
            boolean[] pre = new boolean[table.getColumnCount()];
            for (int i=0; i<table.getColumnCount(); ++i ) {
            	Class type = table.getColumnType(i);
            	pre[i] = TypeLib.isNumericType(type);
            	pad[i] = pre[i] ? '0' : ' ';
            }
            
            // write out data
            for ( IntIterator rows = table.rows(); rows.hasNext(); ) {
                int row = rows.nextInt();
                for ( int i=0; i<table.getColumnCount(); ++i ) {
                	out.print(pack(table.getString(row, i), 
                				   m_schema.getColumnLength(i),
                				   pre[i], pad[i]));
                }
                out.println();
            }
            
            // finish up
            out.flush();
        } catch ( Exception e ) {
            throw new DataIOException(e);
        }
    }
    
    /**
     * Pads or truncates a string as necessary to fit within the column length.
     */
    private static String pack(String value, int len, boolean prepend, char pad) {
    	int vlen = value.length();
    	if (vlen < len) {
    		StringBuffer sbuf = new StringBuffer();
    		if (prepend) sbuf.append(value);
    		for (int i=len; i<vlen; ++i)
    			sbuf.append(pad);
    		if (!prepend) sbuf.append(value);
    		return sbuf.toString();
    	} else {
    		return value.substring(0, len);
    	}
    }

} // end of class FixedWidthTextTableWriter
