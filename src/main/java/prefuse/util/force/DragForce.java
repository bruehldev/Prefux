package prefuse.util.force;

/**
 * Implements a viscosity/drag force to help stabilize items.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DragForce extends AbstractForce {

    private static String[] pnames = new String[] { "DragCoefficient" };
    
    public static final double DEFAULT_DRAG_COEFF = 0.01;
    public static final double DEFAULT_MIN_DRAG_COEFF = 0.0;
    public static final double DEFAULT_MAX_DRAG_COEFF = 0.1;
    public static final int DRAG_COEFF = 0;

    /**
     * Create a new DragForce.
     * @param dragCoeff the drag co-efficient
     */
    public DragForce(double dragCoeff) {
        params = new double[] { dragCoeff };
        minValues = new double[] { DEFAULT_MIN_DRAG_COEFF };
        maxValues = new double[] { DEFAULT_MAX_DRAG_COEFF };
    }

    /**
     * Create a new DragForce with default drag co-efficient.
     */
    public DragForce() {
        this(DEFAULT_DRAG_COEFF);
    }

    /**
     * Returns true.
     * @see prefuse.util.force.Force#isItemForce()
     */
    public boolean isItemForce() {
        return true;
    }
    
    /**
     * @see prefuse.util.force.AbstractForce#getParameterNames()
     */
    protected String[] getParameterNames() {
        return pnames;
    }
    
    /**
     * @see prefuse.util.force.Force#getForce(prefuse.util.force.ForceItem)
     */
    public void getForce(ForceItem item) {
        item.force[0] -= params[DRAG_COEFF]*item.velocity[0];
        item.force[1] -= params[DRAG_COEFF]*item.velocity[1];
    }

} // end of class DragForce
