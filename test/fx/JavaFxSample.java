package fx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import prefux.FxDisplay;
import prefux.Visualization;
import prefux.action.ActionList;
import prefux.action.RepaintAction;
import prefux.action.layout.graph.ForceDirectedLayout;
import prefux.activity.Activity;
import prefux.data.Graph;
import prefux.data.io.DataIOException;
import prefux.data.io.GraphMLReader;
import prefux.render.BorderPaneRenderer;
import prefux.render.BorderPaneRenderer.BorderPanePos;
import prefux.render.DefaultRendererFactory;
import prefux.render.LabelRenderer;
import prefux.render.ShapeRenderer;

public class JavaFxSample extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Hello World!");
		StackPane root = new StackPane();
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
		
		Graph graph = null;
		try {
		    graph = new GraphMLReader().readGraph("data/socialnet.xml");
		    Visualization vis = new Visualization();
		    vis.add("graph", graph);
		    
		    ShapeRenderer sr = new ShapeRenderer();
		    LabelRenderer lr = new LabelRenderer("name");
		    LabelRenderer lr2 = new LabelRenderer("name");
		    lr2.addStyle("invisible");
		    BorderPaneRenderer r = new BorderPaneRenderer();
		    r.add(sr, BorderPanePos.CENTER, Pos.CENTER);
		    r.add(lr, BorderPanePos.TOP);
		    r.add(lr2, BorderPanePos.BOTTOM);

		    // create a new default renderer factory
		    // return our name label renderer as the default for all non-EdgeItems
		    // includes straight line edges for EdgeItems by default
		    vis.setRendererFactory(new DefaultRendererFactory(r));
		    
		    ActionList layout = new ActionList(Activity.INFINITY);
		    layout.add(new ForceDirectedLayout("graph"));
		    layout.add(new RepaintAction());
		    vis.putAction("layout", layout);
		    FxDisplay display = new FxDisplay(vis);
		    root.getChildren().add(display);
		    
		    vis.run("layout");

		} catch ( DataIOException e ) {
		    e.printStackTrace();
		    System.err.println("Error loading graph. Exiting...");
		    System.exit(1);
		}
	}

}
