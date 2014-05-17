package prefux.visual.expression;

import prefux.data.expression.ColumnExpression;
import prefux.data.expression.Expression;
import prefux.data.expression.Function;
import prefux.data.expression.NotPredicate;
import prefux.data.expression.Predicate;
import prefux.visual.VisualItem;

/**
 * Expression that indicates if an item is currently under the mouse
 * pointer.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class HoverPredicate extends ColumnExpression
    implements Predicate, Function
{
    /** Convenience instance for the hover == true case. */
    public static final Predicate TRUE = new HoverPredicate();
    /** Convenience instance for the hover == false case. */
    public static final Predicate FALSE = new NotPredicate(TRUE);
    
    /**
     * Create a new HoverPredicate.
     */
    public HoverPredicate() {
        super(VisualItem.HOVER);
    }

    /**
     * @see prefux.data.expression.Function#getName()
     */
    public String getName() {
        return "HOVER";
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

} // end of class HoverPredicate
