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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import prefux.data.Graph;

/**
 * interface for classes that read in Graph or Tree data from a particular
 * file format.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface GraphReader {

    /**
     * Read in a graph from the file at the given location. Though
     * not required by this interface, the String is typically resolved
     * using the {@link prefux.util.io.IOLib#streamFromString(String)} method,
     * allowing URLs, classpath references, and files on the file system
     * to be accessed.
     * @param location the location to read the graph from
     * @return the loaded Graph
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Graph readGraph(String location) throws DataIOException;
    
    /**
     * Read in a graph from the given URL.
     * @param url the url to read the graph from
     * @return the loaded Graph
     * @throws IOException
     */
    public Graph readGraph(URL url) throws DataIOException;
    
    /**
     * Read in a graph from the given File.
     * @param f the file to read the graph from
     * @return the loaded Graph
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Graph readGraph(File f) throws DataIOException;
    
    /**
     * Read in a graph from the given InputStream.
     * @param is the InputStream to read the graph from
     * @return the loaded Graph
     * @throws IOException
     */
    public Graph readGraph(InputStream is) throws DataIOException;

} // end of interface GraphReader
