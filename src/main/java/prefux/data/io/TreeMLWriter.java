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
/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.data.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import prefux.data.Graph;
import prefux.data.Node;
import prefux.data.Schema;
import prefux.util.io.XMLWriter;

/**
 * GraphWriter instance that writes a tree file formatted using the
 * TreeML file format. TreeML is an XML format originally created for
 * the 2003 InfoVis conference contest. A DTD (Document Type Definition) for
 * TreeML is
 * <a href="http://www.nomencurator.org/InfoVis2003/download/treeml.dtd">
 *  available online</a>.
 * 
 * <p>The GraphML spec only supports the data types <code>Int</code>,
 * <code>Long</code>, <code>Float</code>, <code>Real</code> (double),
 * <code>Boolean</code>, <code>String</code>, and <code>Date</code>.
 * An exception will be thrown if a data type outside these allowed
 * types is encountered.</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TreeMLWriter extends AbstractGraphWriter {

    /**
     * String tokens used in the TreeML format.
     */
    public interface Tokens extends TreeMLReader.Tokens {}
    
    /**
     * Map containing legal data types and their names in the GraphML spec
     */
    private static final HashMap TYPES = new HashMap();
    static {
        TYPES.put(int.class, Tokens.INT);
        TYPES.put(long.class, Tokens.LONG);
        TYPES.put(float.class, Tokens.FLOAT);
        TYPES.put(double.class, Tokens.REAL);
        TYPES.put(boolean.class, Tokens.BOOLEAN);
        TYPES.put(String.class, Tokens.STRING);
        TYPES.put(Date.class, Tokens.DATE);
    }
    
    /**
     * @see prefux.data.io.GraphWriter#writeGraph(prefux.data.Graph, java.io.OutputStream)
     */
    public void writeGraph(Graph graph, OutputStream os) throws DataIOException
    {
        // first, check the schemas to ensure GraphML compatibility
        Schema ns = graph.getNodeTable().getSchema();
        checkTreeMLSchema(ns);
        
        XMLWriter xml = new XMLWriter(new PrintWriter(os));
        xml.begin();
        
        xml.comment("prefux TreeML Writer | "
                + new Date(System.currentTimeMillis()));
                
        // print the tree contents
        xml.start(Tokens.TREE);
        
        // print the tree node schema
        xml.start(Tokens.DECLS);
        String[] attr = new String[] {Tokens.NAME, Tokens.TYPE };
        String[] vals = new String[2];

        for ( int i=0; i<ns.getColumnCount(); ++i ) {
            vals[0] = ns.getColumnName(i);
            vals[1] = (String)TYPES.get(ns.getColumnType(i));
            xml.tag(Tokens.DECL, attr, vals, 2);
        }
        xml.end();
        xml.println();
        
        
        // print the tree nodes
        attr[0] = Tokens.NAME;
        attr[1] = Tokens.VALUE;
        
        Node n = graph.getSpanningTree().getRoot();
        while ( n != null ) {
            boolean leaf = (n.getChildCount() == 0);
            
            if ( leaf ) {
                xml.start(Tokens.LEAF);
            } else {
                xml.start(Tokens.BRANCH);
            }
            
            if ( ns.getColumnCount() > 0 ) {
                for ( int i=0; i<ns.getColumnCount(); ++i ) {
                    vals[0] = ns.getColumnName(i);
                    vals[1] = n.getString(vals[0]);
                    if (vals[1] != null) {
                    	xml.tag(Tokens.ATTR, attr, vals, 2);
                    }
                }
            }
            n = nextNode(n, xml);
        }
        
        // finish writing file
        xml.end();
        xml.finish();
    }
    
    /**
     * Find the next node in the depth first iteration, closing off open
     * branch tags as needed.
     * @param x the current node
     * @param xml the XMLWriter
     * @return the next node
     */
    private Node nextNode(Node x, XMLWriter xml) {
        Node n, c;
        if ( (c=x.getChild(0)) != null ) {
            // do nothing
        } else if ( (c=x.getNextSibling()) != null ) {
            xml.end();
        } else {
            c = x.getParent();
            xml.end();
            while ( c != null ) {
                if ( (n=c.getNextSibling()) != null ) {
                    c = n;
                    xml.end();
                    break;
                }
                c = c.getParent();
                xml.end();
            }
        }
        return c;
    }
    
    /**
     * Checks if all Schema types are compatible with the TreeML specification.
     * The TreeML spec only allows the types <code>int</code>,
     * <code>long</code>, <code>float</code>, <code>double</code>,
     * <code>boolean</code>, <code>string</code>, and <code>date</code>.
     * @param s the Schema to check
     */
    private void checkTreeMLSchema(Schema s) throws DataIOException {
        for ( int i=0; i<s.getColumnCount(); ++i ) {
            Class type = s.getColumnType(i);
            if ( TYPES.get(type) == null ) {
                throw new DataIOException("Data type unsupported by the "
                    + "TreeML format: " + type.getName());
            }
        }
    }
    
} // end of class GraphMLWriter