package prefux.render;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public void render(Parent g, VisualItem item, boolean bind) {
		if (item.getNode() == null) {
			Group grp = new Group();
			grp.setManaged(false);
			// grp.setAutoSizeChildren(false);
			item.setNode(grp);
			for (Renderer renderer : this) {
				renderer.render(grp, item, false);
			}
			if (bind) {
				Platform.runLater(() -> {
					grp.layoutXProperty().bind(item.xProperty());
					grp.layoutYProperty().bind(item.yProperty());
				});
			}
			FxGraphicsLib.addToParent(g, grp);
		}

	}

	public void render(Parent g, VisualItem item) {
		render(g, item, true);
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
	public void addStyle(String style) {
		for (Renderer renderer : this) {
			renderer.addStyle(style);
		}
	}

}
