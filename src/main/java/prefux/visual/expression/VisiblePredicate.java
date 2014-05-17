package prefux.visual.expression;

import prefux.data.expression.ColumnExpression;
import prefux.data.expression.Expression;
import prefux.data.expression.Function;
import prefux.data.expression.NotPredicate;
import prefux.data.expression.Predicate;
import prefux.visual.VisualItem;

/**
 * Expression that indicates if an item's visible flag is set.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class VisiblePredicate extends ColumnExpression
    implements Predicate, Function
{

    /** Convenience instance for the visible == true case. */
    public static final Predicate TRUE = new VisiblePredicate();
    /** Convenience instance for the visible == false case. */
    public static final Predicate FALSE = new NotPredicate(TRUE);
    
    /**
     * Create a new VisiblePredicate.
     */
    public VisiblePredicate() {
        super(VisualItem.VISIBLE);
    }

    /**
     * @see prefux.data.expression.Function#getName()
     */
    public String getName() {
        return "VISIBLE";
    }

    /**
     * @see prefux.data.expression.Function#addParameter(prefux.data.expression.Expression)
     */
    public void addParameter(Expression e) {
        throw new IllegalStateException("This function takes 0 parameters");
    }

    /**
     * @see prefux.data.expression.Function#getParameterCount()
     */
    public int getParameterCount() {
        return 0;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName()+"()";
    }

} // end of class VisiblePredicate
