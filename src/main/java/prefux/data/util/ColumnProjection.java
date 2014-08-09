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
package prefux.data.util;

import prefux.data.column.Column;
import prefux.data.event.ProjectionListener;

/**
 * Interface for filtering only a subset of a Table columns, computing
 * a projection of the available data fields. Used in conjunction with
 * CascadedTable instances to control what fields are inherited from
 * a parent table.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface ColumnProjection {

    /**
     * Determine if the given Column should be included.
     * @param col the Column to test
     * @param name the name of the column
     * @return true if the column passes the projection, false otherwise
     */
    public boolean include(Column col, String name);
    
    /**
     * Add a listener to this column projection
     * @param lstnr the listener to add
     */
    public void addProjectionListener(ProjectionListener lstnr);
    
    /**
     * Remove a listener from this column projection
     * @param lstnr the listener to remove
     */
    public void removeProjectionListener(ProjectionListener lstnr);
    
} // end of interface ColumnProjection
