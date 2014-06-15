package prefux.render;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import prefux.data.util.Point2D;
import prefux.util.FxGraphicsLib;
import prefux.visual.VisualItem;

public class CombinedRenderer extends ArrayList<Renderer> implements Renderer {

	private static final Logger log = LoggerFactory
			.getLogger(CombinedRenderer.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -8423310534883356968L;

	@Override
	public void render(Parent g, VisualItem item) {
		if (item.getNode() == null) {
			Group grp = new Group();
			grp.setManaged(false);
			// grp.setAutoSizeChildren(false);
			item.setNode(grp);
			for (Renderer renderer : this) {
				renderer.render(grp, item);
			}
			FxGraphicsLib.addToParent(g, grp);
		}

	}

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		boolean locate = false;
		for (Renderer renderer : this) {
			locate |= renderer.locatePoint(p, item);
		}
		return locate;
	}

	@Override
	public void setBounds(VisualItem item) {
		if (log.isTraceEnabled())
			log.trace("setBounds");
		Group grp = (Group) item.getNode();
		// FxGraphicsLib.setCenterCoord(item.getX(), item.getY(), grp);
		for (Node node : grp.getChildrenUnmodifiable()) {
			if (log.isTraceEnabled())
				log.trace("setBounds " + item.getX() + "/" + item.getY()
						+ " for " + node);
			Platform.runLater(() -> {
				FxGraphicsLib.setCenterCoord(item.getX(), item.getY(), node);
			});
			// node.relocate(item.getX(),item.getY());
		}
	}

	@Override
	public void addStyle(String style) {
		for (Renderer renderer : this) {
			renderer.addStyle(style);
		}
	}

}
