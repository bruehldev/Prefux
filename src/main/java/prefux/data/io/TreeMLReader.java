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

import java.io.InputStream;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import prefux.data.Graph;
import prefux.data.Node;
import prefux.data.Table;
import prefux.data.Tree;
import prefux.data.parser.DataParseException;
import prefux.data.parser.DataParser;
import prefux.data.parser.ParserFactory;


/**
 * GraphReader instance that reads in tree-structured data in the
 * XML-based TreeML format. TreeML is an XML format originally created for
 * the 2003 InfoVis conference contest. A DTD (Document Type Definition) for
 * TreeML is
 * <a href="http://www.nomencurator.org/InfoVis2003/download/treeml.dtd">
 *  available online</a>.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TreeMLReader extends AbstractGraphReader {

    private ParserFactory m_pf = ParserFactory.getDefaultFactory();

    /**
     * @see prefux.data.io.GraphReader#readGraph(java.io.InputStream)
     */
    public Graph readGraph(InputStream is) throws DataIOException {
        try {       
            TreeMLHandler    handler   = new TreeMLHandler();
            SAXParserFactory factory   = SAXParserFactory.newInstance();
            SAXParser        saxParser = factory.newSAXParser();
            saxParser.parse(is, handler);
            return handler.getTree();
        } catch ( Exception e ) {
            throw new DataIOException(e);
        }
    }

    /**
     * String tokens used in the TreeML format.
     */
    public static interface Tokens {
        public static final String TREE   = "tree";
        public static final String BRANCH = "branch";
        public static final String LEAF   = "leaf";
        public static final String ATTR   = "attribute";
        public static final String NAME   = "name";
        public static final String VALUE  = "value";
        public static final String TYPE   = "type";
        
        public static final String DECLS  = "declarations";
        public static final String DECL   = "attributeDecl";
        
        public static final String INT = "Int";
        public static final String INTEGER = "Integer";
        public static final String LONG = "Long";
        public static final String FLOAT = "Float";
        public static final String REAL = "Real";
        public static final String STRING = "String";
        public static final String DATE = "Date";
        public static final String CATEGORY = "Category";
        
        // prefux-specific allowed types
        public static final String BOOLEAN = "Boolean";
        public static final String DOUBLE = "Double";
    }
    
    /**
     * A SAX Parser for TreeML data files.
     */
    public class TreeMLHandler extends DefaultHandler implements Tokens {
        
        private Table m_nodes = null;
        private Tree m_tree = null;
        
        private Node m_activeNode = null;
        private boolean m_inSchema = true;
        
        public void startDocument() {
            m_tree = new Tree();
            m_nodes = m_tree.getNodeTable();
        }
        
        private void schemaCheck() {
            if ( m_inSchema ) {
                m_inSchema = false;
            }
        }
        
        public void endElement(String namespaceURI, String localName, String qName) {
            if ( qName.equals(BRANCH) || qName.equals(LEAF) ) {
                m_activeNode = m_activeNode.getParent();
            }
        }
        
        public void startElement(String namespaceURI, String localName,
                                 String qName, Attributes atts) {           
            if ( qName.equals(DECL) ) {
                if ( !m_inSchema ) {
                    throw new RuntimeException("All declarations must be done "
                            + "before nodes begin");
                }
                String name = atts.getValue(NAME);
                String type = atts.getValue(TYPE);
                Class t = parseType(type);
                m_nodes.addColumn(name, t);
            }
            else if ( qName.equals(BRANCH) || qName.equals(LEAF) ) {
                schemaCheck();
                
                // parse a node element
                Node n;
                if ( m_activeNode == null ) {
                    n = m_tree.addRoot();
                } else {
                    n = m_tree.addChild(m_activeNode);
                }
                m_activeNode = n;
            }
            else if ( qName.equals(ATTR) ) {
                // parse an attribute
                parseAttribute(atts);
            }
        }
        
        protected void parseAttribute(Attributes atts) {
            String alName, name = null, value = null;
            for ( int i = 0; i < atts.getLength(); i++ ) {
                alName = atts.getQName(i);
                if ( alName.equals(NAME) ) {
                    name = atts.getValue(i);
                } else if ( alName.equals(VALUE) ) {
                    value = atts.getValue(i);
                }
            }
            if ( name == null || value == null ) {
                System.err.println("Attribute under-specified");
                return;
            }

            try {
                Object val = parse(value, m_nodes.getColumnType(name));
                m_activeNode.set(name, val);
            } catch ( Exception e ) {
                throw new RuntimeException(e);
            }
        }
        
        protected Object parse(String s, Class type)
            throws DataParseException
        {
            DataParser dp = m_pf.getParser(type);
            return dp.parse(s);
        }
        
        protected Class parseType(String type) {
            type = Character.toUpperCase(type.charAt(0)) +
                   type.substring(1).toLowerCase();
            if ( type.equals(INT) || type.equals(INTEGER) ) {
                return int.class;
            } else if ( type.equals(LONG) ) {
                return long.class;
            } else if ( type.equals(FLOAT) ) {
                return float.class;
            } else if ( type.equals(DOUBLE) || type.equals(REAL)) {
                return double.class;
            } else if ( type.equals(BOOLEAN) ) {
                return boolean.class;
            } else if ( type.equals(STRING) ) {
                return String.class;
            } else if ( type.equals(DATE) ) {
                return Date.class;
            } else {
                throw new RuntimeException("Unrecognized data type: "+type);
            }
        }
        
        public Tree getTree() {
            return m_tree;
        }
        
    } // end of inner class TreeMLHandler
    
} // end of class TreeMLTReeReader
