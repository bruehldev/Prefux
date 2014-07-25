package prefux.render;


import javafx.scene.Parent;
import prefux.data.util.Point2D;
import prefux.visual.VisualItem;


/**
 * Interface for rendering VisualItems, providing drawing as well as location
 * checking and bounding box routines.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @author alan newberger
 */
public interface Renderer {

    /**
     * Provides a default graphics context for renderers to do useful
     * things like compute string widths when an external graphics context
     * has not yet been provided.
     */
//    public static final Graphics2D DEFAULT_GRAPHICS = (Graphics2D)
//        new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();

    /**
     * Render item into a Parent context.
     * @param g the Parent node
     * @param item the visual item to draw
     */
    public void render(Parent g, VisualItem item);
    
    /**
     * Render item into a Parent context.
     * @param g the Parent node
     * @param item the visual item to draw
     * @param bind if false the binding to the visual item should be omitted
     */
    public void render(Parent g, VisualItem item, boolean bind);

    /**
     * Returns true if the Point is located inside the extents of the item.
     * This calculation matches against the exaxt item shape, and so is more
     * sensitive than just checking within a bounding box.
     * @param p the point to test for containment
     * @param item the item to test containment against
     * @return true if the point is contained within the the item, else false
     */
    public boolean locatePoint(Point2D p, VisualItem item);

    /**
     * Adds an additional style class to each rendered element.
     * 
     * @param style
     */
    public void addStyle(String style);
    

} // end of interface Renderer
