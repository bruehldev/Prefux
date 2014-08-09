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
package prefux.util.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.filechooser.FileFilter;

/**
 * A simple file filter for a particular file extension.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class SimpleFileFilter extends FileFilter {
    
    private ArrayList exts = new ArrayList();
    private String desc;
    private Object data;
    
    /**
     * Create a new SimpleFileFilter.
     * @param ext the file extension
     * @param desc a description of the file type
     */
    public SimpleFileFilter(String ext, String desc) {
        addExtension(ext);
        this.desc = desc;
    }
    
    /**
     * Create a new SimpleFileFilter.
     * @param ext the file extension
     * @param desc a description of the file type
     * @param data user-provided attached object
     */
    public SimpleFileFilter(String ext, String desc, Object data) {
        addExtension(ext);
        this.desc = desc;
        this.data = data;
    }
    
    /**
     * Add a file extension to this file filter.
     * @param ext the file extension to add
     */
    public void addExtension(String ext) {
        exts.add(ext.toLowerCase());
    }
    
    /**
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
        if ( f == null )
            return false;
        if ( f.isDirectory() )
            return true;
        String extension = IOLib.getExtension(f);
        if ( extension == null ) return false;

        for ( Iterator iter = exts.iterator(); iter.hasNext(); ) {
            String ext = (String)iter.next();
            if ( ext.equalsIgnoreCase(extension) )
                return true;
        }
        return false;
    }
    
    /**
     * Get a user-provided attached object.
     * @return the user-provided attached object
     */
    public Object getUserData() {
        return data;
    }
    
    /**
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        return desc;
    }
    
    /**
     * Get the first file extension associated with this filter.
     * @return the first file extension associated with this filter
     */
    public String getExtension() {
        return (String)exts.get(0);
    }
    
} // end of class SimpleFileFilter
