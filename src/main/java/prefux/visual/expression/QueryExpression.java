package prefux.visual.expression;

import prefux.Visualization;
import prefux.data.Schema;
import prefux.data.Tuple;
import prefux.data.search.SearchTupleSet;
import prefux.visual.VisualItem;

/**
 * Expression that returns the current query string of a data group of the type
 * {@link prefux.data.search.SearchTupleSet}. The data group name is provided
 * by a String-valued sub-expression.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class QueryExpression extends GroupExpression {

    /**
     * Create a new QueryExpression.
     */
    public QueryExpression() {
        super();
    }
    
    /**
     * Create a new QueryExpression.
     * @param group @param group the data group name to use as a parameter
     */
    public QueryExpression(String group) {
        super(group);
    }
    
    /**
     * @see prefux.data.expression.Function#getName()
     */
    public String getName() {
        return "QUERY";
    }

    /**
     * @see prefux.data.expression.Expression#getType(prefux.data.Schema)
     */
    public Class getType(Schema s) {
        return String.class;
    }
    
    /**
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        VisualItem item = (VisualItem)t;
        Visualization vis = item.getVisualization();
        String group = getGroup(t);
        SearchTupleSet sts = (SearchTupleSet)vis.getGroup(group);
        return sts.getQuery();
    }

} // end of class QueryExpression
