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
package prefux.data.event;

import java.util.EventListener;

import prefux.data.column.Column;

/**
 * Listener interface for monitoring changes to a data column.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface ColumnListener extends EventListener {

    /**
     * Notification that a data column has changed.
     * @param src the column that has changed
     * @param type One of {@link EventConstants#INSERT},
     * {@link EventConstants#DELETE}, or {@link EventConstants#UPDATE}.
     * @param start the first column index that has been changed
     * @param end the last column index that has been changed
     */
    public void columnChanged(Column src, int type, int start, int end);
    
    /**
     * Notification that a data column has changed.
     * @param src the column that has changed
     * @param idx the column row index that has changed
     * @param prev the previous value at the given location
     */
    public void columnChanged(Column src, int idx, int prev);
    
    /**
     * Notification that a data column has changed.
     * @param src the column that has changed
     * @param idx the column row index that has changed
     * @param prev the previous value at the given location
     */
    public void columnChanged(Column src, int idx, long prev);
    
    /**
     * Notification that a data column has changed.
     * @param src the column that has changed
     * @param idx the column row index that has changed
     * @param prev the previous value at the given location
     */
    public void columnChanged(Column src, int idx, float prev);
    
    /**
     * Notification that a data column has changed.
     * @param src the column that has changed
     * @param idx the column row index that has changed
     * @param prev the previous value at the given location
     */
    public void columnChanged(Column src, int idx, double prev);
    
    /**
     * Notification that a data column has changed.
     * @param src the column that has changed
     * @param idx the column row index that has changed
     * @param prev the previous value at the given location
     */
    public void columnChanged(Column src, int idx, boolean prev);
    
    /**
     * Notification that a data column has changed.
     * @param src the column that has changed
     * @param idx the column row index that has changed
     * @param prev the previous value at the given location
     */
    public void columnChanged(Column src, int idx, Object prev);
    
} // end of interface ColumnListener
