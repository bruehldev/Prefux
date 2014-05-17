package prefux.visual.expression;

import prefux.data.Schema;
import prefux.data.Tuple;
import prefux.data.tuple.TupleSet;
import prefux.visual.VisualItem;

/**
 * GroupExpression that returns the size of a data group.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GroupSizeFunction extends GroupExpression {

    /**
     * Create a new, uninitialized GroupSizeFunction. The parameter for
     * this Function needs to be set.
     */
    public GroupSizeFunction() {
    }
    
    /**
     * Create a new GroupSizeFunction using the given data group name
     * as the Function parameter.
     * @param group the data group name to use as a parameter
     */
    public GroupSizeFunction(String group) {
        super(group);
    }
    
    /**
     * @see prefux.data.expression.Function#getName()
     */
    public String getName() {
        return "GROUPSIZE";
    }

    /**
     * @see prefux.data.expression.Expression#getType(prefux.data.Schema)
     */
    public Class getType(Schema s) {
        return int.class;
    }

    /**
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        return new Integer(getInt(t));
    }

    /**
     * @see prefux.data.expression.Expression#getDouble(prefux.data.Tuple)
     */
    public double getDouble(Tuple t) {
        return getInt(t);
    }

    /**
     * @see prefux.data.expression.Expression#getFloat(prefux.data.Tuple)
     */
    public float getFloat(Tuple t) {
        return getInt(t);
    }

    /**
     * @see prefux.data.expression.Expression#getInt(prefux.data.Tuple)
     */
    public int getInt(Tuple t) {
        String group = getGroup(t);
        if ( group == null ) { return -1; }
        TupleSet ts = ((VisualItem)t).getVisualization().getGroup(group);
        return ( ts==null ? 0 : ts.getTupleCount() );
    }

    /**
     * @see prefux.data.expression.Expression#getLong(prefux.data.Tuple)
     */
    public long getLong(Tuple t) {
        return getInt(t);
    }

} // end of class GroupSizeFunction
