package prefux.render;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import prefux.data.util.Point2D;
import prefux.util.FxGraphicsLib;
import prefux.visual.VisualItem;

public class StackPaneRenderer extends ArrayList<Renderer> implements Renderer,
		List<Renderer> {
	
	//private static final Logger log = LoggerFactory.getLogger(StackPaneRenderer.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -333317478953441168L;

	public StackPaneRenderer() {
	}

	@Override
	public void render(Parent g, VisualItem item) {
		StackPane pane = new StackPane();
		if (item.getNode() == null)
			item.setNode(pane);
		for (Renderer renderer : this) {
			renderer.render(pane, item);
		}
		FxGraphicsLib.addToParent(g, pane);
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
		Node node = item.getNode();
		if (node != null) {
			Platform.runLater(() -> {
				FxGraphicsLib.setCenterCoord(item.getX(), item.getY(), node);
			});
		}
	}

}
