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
 * TableReader for Comma Separated Value (CSV) files. CSV files list each row of
 * a table on a line, separating each data column by a line. Typically the first
 * line of the file is a header row indicating the names of each data column.
 *
 * For a more in-depth description of the CSV format, please see this
 * <a href="http://www.creativyst.com/Doc/Articles/CSV/CSV01.htm">
 * CSV reference web page</a>.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class CSVTableReader extends AbstractTextTableReader {

    private char delimiter;

    /**
     * Create a new CSVTableReader.
     */
    public CSVTableReader() {
        super();
        delimiter = ',';
    }

    public CSVTableReader(char delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Create a new CSVTableReader.
     *
     * @param parserFactory the ParserFactory to use for parsing text strings
     * into table values.
     */
    public CSVTableReader(ParserFactory parserFactory) {
        super(parserFactory);
        delimiter = ',';
    }

    public CSVTableReader(char delimiter, ParserFactory parserFactory) {
        super(parserFactory);
        this.delimiter = delimiter;
    }

    /**
     * @see prefux.data.io.AbstractTextTableReader#read(java.io.InputStream,
     * prefux.data.io.TableReadListener)
     */
    public void read(InputStream is, TableReadListener trl)
            throws IOException, DataParseException {
        String line;
        StringBuffer sbuf = new StringBuffer();

        boolean inRecord = false;
        int inQuote = 0;
        int lineno = 0;
        int col = 0;

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            // increment the line number
            ++lineno;

            // extract the character array for quicker processing
            char[] c = line.toCharArray();
            int last = c.length - 1;

            // iterate through current line
            for (int i = 0; i <= last; ++i) {
                if (!inRecord) {
                    // not currently processing a record
                    if (Character.isWhitespace(c[i])) {
                        continue;
                    } else if (c[i] == '\"') {
                        inRecord = true;
                        inQuote = 1;
                    } else if (c[i] == delimiter) {
                        String s = sbuf.toString().trim();
                        trl.readValue(lineno, ++col, s);
                        sbuf.delete(0, sbuf.length());
                    } else {
                        inRecord = true;
                        sbuf.append(c[i]);
                    }
                } else {
                    // in the midst of a record
                    if (inQuote == 1) {
                        if (c[i] == '\"' && (i == last || c[i + 1] != '\"')) {
                            // end of quotation
                            inQuote = 2;
                        } else if (c[i] == '\"') {
                            // double quote so skip one ahead
                            sbuf.append(c[i++]);
                        } else {
                            sbuf.append(c[i]);
                        }
                    } else {
                        if (Character.isWhitespace(c[i])) {
                            sbuf.append(c[i]);
                        } else if (c[i] != delimiter && inQuote == 2) {
                            throw new IllegalStateException(
                                    "Invalid data format. "
                                    + "Error at line " + lineno + ", col " + i);
                        } else if (c[i] != delimiter) {
                            sbuf.append(c[i]);
                        } else {
                            String s = sbuf.toString().trim();
                            trl.readValue(lineno, ++col, s);
                            sbuf.delete(0, sbuf.length());
                            inQuote = 0;
                            inRecord = false;
                        }
                    }
                }
            }
            if (inQuote != 1) {
                String s = sbuf.toString().trim();
                trl.readValue(lineno, ++col, s);
                sbuf.delete(0, sbuf.length());
                inQuote = 0;
                inRecord = false;
            }
            if (!inRecord && col > 0) {
                col = 0;
            }
        }
    }
} // end of class CSVTableReader
