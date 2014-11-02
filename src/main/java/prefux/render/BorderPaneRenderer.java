/*  
 * Copyright (c) 2004-2013 Regents of the University of California.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3.  Neither the name of the University nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * Copyright (c) 2014 Martin Stockhammer
 */
/**
 * Copyright (c) 2014 Martin Stockhammer
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.render;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	private static final Logger log = LogManager.getLogger(BorderPaneRenderer.class);

	private List<BorderRenderer> renderers = new ArrayList<>();

	public BorderPaneRenderer() {
	}

	@Override
	public void render(Parent g, VisualItem item, boolean bind) {
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

	@Override
    public void render(Parent g, VisualItem item) {
	    render(g, item, true);
	    
    }

}
