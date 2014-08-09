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

import java.util.HashSet;

import prefux.data.column.Column;

/**
 * ColumnProjection instance that includes or excludes columns based on
 * the column name.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class NamedColumnProjection extends AbstractColumnProjection {

    private HashSet m_names;
    private boolean m_include;

    /**
     * Create a new NamedColumnProjection.
     * @param name the name to filter on
     * @param include true to include the given names (and exclude all others),
     * false to exclude them (and include all others)
     */
    public NamedColumnProjection(String name, boolean include) {
        m_names = new HashSet();
        m_names.add(name);
        m_include = include;
    }
    
    /**
     * Create a new NamedColumnProjection.
     * @param names the names to filter on
     * @param include true to include the given names (and exclude all others),
     * false to exclude them (and include all others)
     */
    public NamedColumnProjection(String[] names, boolean include) {
        m_names = new HashSet();
        for ( int i=0; i<names.length; ++i )
            m_names.add(names[i]);
        m_include = include;
    }
    
    /**
     * Add a column name to this projection.
     * @param name the column name to add
     */
    public void addName(String name) {
        m_names.add(name);
    }
    
    /**
     * Remove a column name from this projection
     * @param name the column name to remove
     * @return true if the name was succesffuly removed, false otherwise
     */
    public boolean removeName(String name) {
        return m_names.remove(name);
    }
    
    /**
     * @see prefux.data.util.ColumnProjection#include(prefux.data.column.Column, java.lang.String)
     */
    public boolean include(Column col, String name) {
        return !(m_include ^ m_names.contains(name));
    }

} // end of class NamedColumnProjection
