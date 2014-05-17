package prefux.render;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import prefux.data.util.Point2D;
import prefux.util.FxGraphicsLib;
import prefux.visual.VisualItem;

public class StackPaneRenderer extends ArrayList<Renderer> implements Renderer,
		List<Renderer> {

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
		// pane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
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
				node.setLayoutX(item.getX());
				node.setLayoutY(item.getY());
			});
		}

		// for (Renderer renderer : this) {
		// renderer.setBounds(item);
		// }
	}

}
