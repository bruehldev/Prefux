package prefux.visual.tuple;

import prefux.data.Graph;
import prefux.data.Node;
import prefux.visual.EdgeItem;
import prefux.visual.NodeItem;
import prefux.visual.VisualTable;

/**
 * EdgeItem implementation that used data values from a backing
 * VisualTable of edges.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TableEdgeItem extends TableVisualItem implements EdgeItem {

    protected Graph m_graph;
    
    /**
     * Initialize a new TableEdgeItem for the given graph, table, and row.
     * This method is used by the appropriate TupleManager instance, and
     * should not be called directly by client code, unless by a
     * client-supplied custom TupleManager.
     * @param table the backing VisualTable
     * @param graph the backing VisualGraph
     * @param row the row in the node table to which this Edge instance
     *  corresponds.
     */
    protected void init(VisualTable table, Graph graph, int row) {
        super.init(table, graph, row);
        m_graph = graph;
    }
    
    /**
     * @see prefux.data.Edge#getGraph()
     */
    public Graph getGraph() {
        return m_graph;
    }
    
    /**
     * @see prefux.data.Edge#isDirected()
     */
    public boolean isDirected() {
        return m_graph.isDirected();
    }

    /**
     * @see prefux.data.Edge#getSourceNode()
     */
    public Node getSourceNode() {
        return m_graph.getSourceNode(this);
    }

    /**
     * @see prefux.data.Edge#getTargetNode()
     */
    public Node getTargetNode() {
        return m_graph.getTargetNode(this);
    }

    /**
     * @see prefux.data.Edge#getAdjacentNode(prefux.data.Node)
     */
    public Node getAdjacentNode(Node n) {
        return m_graph.getAdjacentNode(this, n);
    }
    
    /**
     * @see prefux.visual.EdgeItem#getSourceItem()
     */
    public NodeItem getSourceItem() {
        return (NodeItem)getSourceNode();
    }

    /**
     * @see prefux.visual.EdgeItem#getTargetItem()
     */
    public NodeItem getTargetItem() {
        return (NodeItem)getTargetNode();
    }

    /**
     * @see prefux.visual.EdgeItem#getAdjacentItem(prefux.visual.NodeItem)
     */
    public NodeItem getAdjacentItem(NodeItem n) {
        return (NodeItem)getAdjacentNode(n);
    }

} // end of class TableEdgeItem
