/**
 * Copyright (c) 2014 Martin Stockhammer
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.render;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prefux.data.util.Point2D;
import prefux.util.FxGraphicsLib;
import prefux.visual.VisualItem;

public class BorderPaneRenderer implements Renderer {

	public enum BorderPanePos {
		BOTTOM, TOP, LEFT, RIGHT, CENTER
	}

	private final class BorderRenderer {
		private BorderPanePos position;
		private Pos alignment;
		private Renderer renderer;

		public BorderRenderer(Renderer renderer, BorderPanePos position) {
			this.renderer = renderer;
			this.position = position;
		}

		public BorderRenderer(Renderer renderer, BorderPanePos position,
				Pos alignment) {
			this.renderer = renderer;
			this.position = position;
			this.alignment = alignment;
		}

		public BorderPanePos getPosition() {
			return position;
		}

		public Renderer getRenderer() {
			return renderer;
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof BorderRenderer)
					&& ((BorderRenderer) obj).getRenderer().equals(
							this.renderer);
		}

		@Override
		public int hashCode() {
			return renderer.hashCode();
		}

		public Pos getAlignment() {
			return alignment;
		}

		@SuppressWarnings("unused")
		public void setAlignment(Pos alignment) {
			this.alignment = alignment;
		}

	}

	private static final Logger log = LoggerFactory
			.getLogger(BorderPaneRenderer.class);

	private List<BorderRenderer> renderers = new ArrayList<>();

	public BorderPaneRenderer() {
	}

	@Override
	public void render(Parent g, VisualItem item) {
		BorderPane pane = new BorderPane();
		if (item.getNode() == null)
			item.setNode(pane);
		for (BorderRenderer renderer : renderers) {
			Group grp = new Group();
			renderer.getRenderer().render(grp, item);
			ArrayList<Node> childNodes = new ArrayList<Node>(grp.getChildren());
			for (Node node : childNodes) {
				if (node != null) {
					switch (renderer.getPosition()) {
					case BOTTOM:
						log.debug("Adding bottom node");
						pane.setBottom(node);
						break;
					case TOP:
						log.debug("Adding top node");
						pane.setTop(node);
						break;
					case LEFT:
						log.debug("Adding left node");
						pane.setLeft(node);
						break;
					case RIGHT:
						log.debug("Adding right node");
						pane.setRight(node);
					case CENTER:
						log.debug("Adding center node");
						pane.setCenter(node);
					default:
						break;
					}
					if (renderer.getAlignment() != null) {
						BorderPane.setAlignment(node, renderer.getAlignment());
					}
				}
			}
		}
		FxGraphicsLib.addToParent(g, pane);
	}

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		boolean locate = false;
		for (BorderRenderer renderer : renderers) {
			locate |= renderer.getRenderer().locatePoint(p, item);
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

	public void add(Renderer renderer, BorderPanePos position) {
		renderers.add(new BorderRenderer(renderer, position));
	}

	public void add(Renderer renderer, BorderPanePos position, Pos alignment) {
		renderers.add(new BorderRenderer(renderer, position, alignment));
	}

	public void remove(Renderer renderer) {
		renderers.remove(new BorderRenderer(renderer, BorderPanePos.CENTER));
	}

	@Override
	public void addStyle(String style) {
		// TODO Auto-generated method stub
		
	}

}
