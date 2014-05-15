package prefuse.util.force;

/**
 * Uses a gravitational force model to act as a circular "wall". Can be used to
 * construct circles which either attract or repel items.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class CircularWallForce extends AbstractForce {

    private static String[] pnames = new String[] { "GravitationalConstant" };
    
    public static final double DEFAULT_GRAV_CONSTANT = -0.1;
    public static final double DEFAULT_MIN_GRAV_CONSTANT = -1.0;
    public static final double DEFAULT_MAX_GRAV_CONSTANT = 1.0;
    public static final int GRAVITATIONAL_CONST = 0;
    
    private double x, y, r;

    /**
     * Create a new CircularWallForce.
     * @param gravConst the gravitational constant to use
     * @param x the center x-coordinate of the circle
     * @param y the center y-coordinate of the circle
     * @param r the radius of the circle
     */
    public CircularWallForce(double gravConst, 
        double x, double y, double r) 
    {
        params = new double[] { gravConst };
        minValues = new double[] { DEFAULT_MIN_GRAV_CONSTANT };
        maxValues = new double[] { DEFAULT_MAX_GRAV_CONSTANT };
        this.x = x; this.y = y;
        this.r = r;
    }
    
    /**
     * Create a new CircularWallForce with default gravitational constant.
     * @param x the center x-coordinate of the circle
     * @param y the center y-coordinate of the circle
     * @param r the radius of the circle
     */
    public CircularWallForce(double x, double y, double r) {
        this(DEFAULT_GRAV_CONSTANT,x,y,r);
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
        double[] n = item.location;
        double dx = x-n[0];
        double dy = y-n[1];
        double d = (double)Math.sqrt(dx*dx+dy*dy);
        double dr = r-d;
        double c = dr > 0 ? -1 : 1;
        double v = c*params[GRAVITATIONAL_CONST]*item.mass / (dr*dr);
        if ( d == 0.0 ) {
            dx = ((double)Math.random()-0.5) / 50.0;
            dy = ((double)Math.random()-0.5) / 50.0;
            d  = (double)Math.sqrt(dx*dx+dy*dy);
        }
        item.force[0] += v*dx/d;
        item.force[1] += v*dy/d;
        //System.out.println(dx/d+","+dy/d+","+dr+","+v);
    }

} // end of class CircularWallForce
