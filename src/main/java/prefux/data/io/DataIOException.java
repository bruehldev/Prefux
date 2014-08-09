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

/**
 * Exception indicating an error occurred during reading or writing data.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DataIOException extends Exception {

    /**
     * Create a new DataIOException.
     */
    public DataIOException() {
        super();
    }

    /**
     * Create a new DataIOException.
     * @param message a descriptive error message
     */
    public DataIOException(String message) {
        super(message);
    }

    /**
     * Create a new DataIOException.
     * @param message a descriptive error message
     * @param cause a Throwable (e.g., error or exception) that was the cause
     * for this exception being thrown
     */
    public DataIOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new DataIOException.
     * @param cause a Throwable (e.g., error or exception) that was the cause
     * for this exception being thrown
     */
    public DataIOException(Throwable cause) {
        super(cause);
    }
    
} // end of class DataIOException
