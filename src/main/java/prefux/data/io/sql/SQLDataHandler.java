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
package prefux.data.io.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import prefux.data.Table;

/**
 * Interface for taking a value in a SQL ResultSet and translating it into
 * a Java data value for use in a prefux Table.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface SQLDataHandler {

    /**
     * Process a data value from a ResultSet, translating it into a
     * Java data value and storing it in a Table.
     * @param t the Table in which to store the result value
     * @param trow the Table row to add to
     * @param rset the ResultSet to read the SQL value from, assumed
     * to be set to the desired row
     * @param rcol the column index of the data value in the row set.
     * This is also used to look up the column name, which is used
     * to access the correct data field of the Table.
     * @throws SQLException if an error occurs accessing the ResultSet
     */
    public void process(Table t, int trow, ResultSet rset, int rcol)
        throws SQLException;
    
    /**
     * Return the Java data type for the given data field name and
     * its sql data type.
     * @param columnName the name of data field / column
     * @param sqlType the field's sql data type, one of the constants
     * in the {@link java.sql.Types} class.
     * @return the Java Class data type
     */
    public Class getDataType(String columnName, int sqlType);
    
} // end of interface SQLDataValueHandler
