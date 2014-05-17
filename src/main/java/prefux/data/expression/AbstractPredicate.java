package prefux.data.expression;

import prefux.data.Schema;
import prefux.data.Tuple;

/**
 * Abstract base class for dedicated Predicate instances.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractPredicate extends AbstractExpression 
    implements Predicate
{

    /**
     * Returns boolean.class.
     * @see prefux.data.expression.Expression#getType(prefux.data.Schema)
     */
    public Class getType(Schema s) {
        return boolean.class;
    }

    /**
     * Returns the wrapper Object type for the result of
     * {@link Expression#getBoolean(Tuple)}.
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        return ( getBoolean(t) ? Boolean.TRUE : Boolean.FALSE );
    }
    
} // end of class AbstractPredicate
