package prefux.render;

import javafx.scene.Node;
import javafx.scene.Parent;
import prefux.data.util.Point2D;
import prefux.visual.VisualItem;


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
     * @see prefux.render.Renderer#setBounds(prefux.visual.VisualItem)
     */
    public void setBounds(VisualItem item) {
        item.setBounds(item.getX(), item.getY(), 0, 0);
    }

	@Override
	public void render(Parent g, VisualItem item) {
		// Do nothing
		
	}

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		return false;
	}



} // end of class NullRenderer
