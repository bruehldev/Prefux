package prefux.data.tuple;

import java.util.Iterator;

import prefux.data.Edge;
import prefux.data.Graph;
import prefux.data.Node;
import prefux.data.Table;

/**
 * Node implementation that reads Node data from a backing node table.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TableNode extends TableTuple implements Node {

    /**
     * The backing graph.
     */
    protected Graph m_graph;
    
    /**
     * Initialize a new Node backed by a node table. This method is used by
     * the appropriate TupleManager instance, and should not be called
     * directly by client code, unless by a client-supplied custom
     * TupleManager.
     * @param table the node Table
     * @param graph the backing Graph
     * @param row the row in the node table to which this Node instance
     *  corresponds.
     */
    protected void init(Table table, Graph graph, int row) {
        m_table = table;
        m_graph = graph;
        m_row = m_table.isValidRow(row) ? row : -1;
    }
    
    /**
     * @see prefux.data.Node#getGraph()
     */
    public Graph getGraph() {
        return m_graph;
    }
    
    // ------------------------------------------------------------------------
    // Graph Methods
    
    /**
     * @see prefux.data.Node#getInDegree()
     */
    public int getInDegree() {
        return m_graph.getInDegree(this);
    }

    /**
     * @see prefux.data.Node#getOutDegree()
     */
    public int getOutDegree() {
        return m_graph.getOutDegree(this);
    }

    /**
     * @see prefux.data.Node#getDegree()
     */
    public int getDegree() {
        return m_graph.getDegree(this);
    }

    /**
     * @see prefux.data.Node#inEdges()
     */
    public Iterator inEdges() {
        return m_graph.inEdges(this);
    }

    /**
     * @see prefux.data.Node#outEdges()
     */
    public Iterator outEdges() {
        return m_graph.outEdges(this);
    }

    /**
     * @see prefux.data.Node#edges()
     */
    public Iterator edges() {
        return m_graph.edges(this);
    }
    
    /**
     * @see prefux.data.Node#inNeighbors()
     */
    public Iterator inNeighbors() {
        return m_graph.inNeighbors(this);
    }
    
    /**
     * @see prefux.data.Node#outNeighbors()
     */
    public Iterator outNeighbors() {
        return m_graph.outNeighbors(this);
    }
    
    /**
     * @see prefux.data.Node#neighbors()
     */
    public Iterator neighbors() {
        return m_graph.neighbors(this);
    }

    
    // ------------------------------------------------------------------------
    // Tree Methods

    /**
     * @see prefux.data.Node#getParent()
     */
    public Node getParent() {
        return m_graph.getSpanningTree().getParent(this);
    }

    /**
     * @see prefux.data.Node#getParentEdge()
     */
    public Edge getParentEdge() {
        return m_graph.getSpanningTree().getParentEdge(this);
    }
    
    /**
     * @see prefux.data.Node#getChildCount()
     */
    public int getChildCount() {
        return m_graph.getSpanningTree().getChildCount(m_row);
    }

    /**
     * @see prefux.data.Node#getChildIndex(prefux.data.Node)
     */
    public int getChildIndex(Node child) {
        return m_graph.getSpanningTree().getChildIndex(this, child);
    }
    
    /**
     * @see prefux.data.Node#getChild(int)
     */
    public Node getChild(int idx) {
        return m_graph.getSpanningTree().getChild(this, idx);
    }
    
    /**
     * @see prefux.data.Node#getFirstChild()
     */
    public Node getFirstChild() {
        return m_graph.getSpanningTree().getFirstChild(this);
    }
    
    /**
     * @see prefux.data.Node#getLastChild()
     */
    public Node getLastChild() {
        return m_graph.getSpanningTree().getLastChild(this);
    }
    
    /**
     * @see prefux.data.Node#getPreviousSibling()
     */
    public Node getPreviousSibling() {
        return m_graph.getSpanningTree().getPreviousSibling(this);
    }
    
    /**
     * @see prefux.data.Node#getNextSibling()
     */
    public Node getNextSibling() {
        return m_graph.getSpanningTree().getNextSibling(this);
    }
    
    /**
     * @see prefux.data.Node#children()
     */
    public Iterator children() {
        return m_graph.getSpanningTree().children(this);
    }

    /**
     * @see prefux.data.Node#childEdges()
     */
    public Iterator childEdges() {
        return m_graph.getSpanningTree().childEdges(this);
    }

    /**
     * @see prefux.data.Node#getDepth()
     */
    public int getDepth() {
        return m_graph.getSpanningTree().getDepth(m_row);
    }

} // end of class TableNode
