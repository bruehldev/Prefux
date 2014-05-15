package prefuse.render;

import javafx.scene.Node;
import javafx.scene.Parent;
import prefuse.visual.VisualItem;


/**
 * Renderer that does nothing, causing an item to be rendered "into
 * the void". Possibly useful for items that must exist and have a spatial
 * location but should otherwise be invisible and non-interactive (e.g.,
 * invisible end-points for visible edges).
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class NullRenderer implements Renderer {

    
    
    /**
     * @see prefuse.render.Renderer#setBounds(prefuse.visual.VisualItem)
     */
    public void setBounds(VisualItem item) {
        item.setBounds(item.getX(), item.getY(), 0, 0);
    }

	@Override
	public void render(Parent g, VisualItem item) {
		// Do nothing
		
	}

	@Override
	public boolean locatePoint(javafx.geometry.Point2D p, VisualItem item) {
		return false;
	}



} // end of class NullRenderer
