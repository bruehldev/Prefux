package fx;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prefux.Constants;
import prefux.FxDisplay;
import prefux.Visualization;
import prefux.action.ActionList;
import prefux.action.RepaintAction;
import prefux.action.assignment.DataColorAction;
import prefux.action.assignment.NodeDegreeSizeAction;
import prefux.action.layout.graph.ForceDirectedLayout;
import prefux.activity.Activity;
import prefux.controls.DragControl;
import prefux.data.Graph;
import prefux.data.expression.Predicate;
import prefux.data.expression.parser.ExpressionParser;
import prefux.data.io.DataIOException;
import prefux.data.io.GraphMLReader;
import prefux.render.DefaultRendererFactory;
import prefux.render.EdgeRenderer;
import prefux.render.LabelRenderer;
import prefux.render.ShapeRenderer;
import prefux.util.ColorLib;
import prefux.util.PrefuseLib;
import prefux.visual.VisualItem;

public class JavaFxSample extends Application {



	private static final double WIDTH = 1000;
	private static final double HEIGHT = 800;
	private static final String GROUP = "graph";

	@Override
	public void start(Stage primaryStage) {

		// -- 1. setup dialog -----------------------------------------------------
		primaryStage.setTitle("PRRV");
		BorderPane root = new BorderPane();
		primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
		root.getStyleClass().add("display");
		primaryStage.show();

		Graph graph = null;
		try {
			// -- 2. load the data ------------------------------------------------
			graph = new GraphMLReader().readGraph("data/socialnet.xml");

			// -- 3. the visualization --------------------------------------------
			Visualization vis = new Visualization();
			vis.add(GROUP, graph);


			// -- 4. the renderers and renderer factory ---------------------------
			ShapeRenderer female = new ShapeRenderer();
			female.setFillMode(ShapeRenderer.GRADIENT_SPHERE);
			LabelRenderer lr = new LabelRenderer("name");
			ShapeRenderer male = new ShapeRenderer();
			male.setFillMode(ShapeRenderer.GRADIENT_SPHERE);

			// create a new default renderer factory
			// return our name label renderer as the default for all
			// non-EdgeItems
			// includes straight line edges for EdgeItems by default
			//EdgeRenderer er = new EdgeRenderer();
			DefaultRendererFactory rfa = new DefaultRendererFactory();
			Predicate expMale = ExpressionParser.predicate("gender='M'");
			Predicate expFemale = ExpressionParser.predicate("gender='F'");
			rfa.add(expMale, male);
			rfa.add(expFemale, female);
			vis.setRendererFactory(rfa);

			// -- 5. the processing actions ---------------------------------------
			ActionList layout = new ActionList(Activity.INFINITY,30);
			layout.add(new ForceDirectedLayout("graph"));
			layout.add(new RepaintAction());
			vis.putAction("layout", layout);

			ActionList nodeActions = new ActionList();
			final String NODES = PrefuseLib.getGroupName(GROUP, Graph.NODES);
			// DataSizeAction size = new DataSizeAction(NODES, "age");
			// nodeActions.add(size);
			NodeDegreeSizeAction size = new NodeDegreeSizeAction(NODES);
			nodeActions.add(size);
			int[] femalePalette = new int[] { ColorLib.rgb(255, 247, 243),
			        ColorLib.rgb(253, 224, 221), ColorLib.rgb(252, 197, 192),
			        ColorLib.rgb(250, 159, 181), ColorLib.rgb(247, 104, 161),
			        ColorLib.rgb(221, 52, 151), ColorLib.rgb(174, 1, 126),
			        ColorLib.rgb(122, 1, 119), ColorLib.rgb(73, 0, 106) };
			
			int[] malePalette = new int[] {
					ColorLib.rgb(255,247,251),
					ColorLib.rgb(236,226,240),
					ColorLib.rgb(208,209,230),
					ColorLib.rgb(166,189,219),
					ColorLib.rgb(103,169,207),
					ColorLib.rgb(54,144,192),
					ColorLib.rgb(2,129,138),
					ColorLib.rgb(1,108,89),
					ColorLib.rgb(1,70,54)
			};

			DataColorAction colorF = new DataColorAction(NODES, "age",
			        Constants.NUMERICAL, VisualItem.FILLCOLOR, femalePalette);
			DataColorAction colorM = new DataColorAction(NODES, "age",
			        Constants.NUMERICAL, VisualItem.FILLCOLOR, malePalette);
			nodeActions.add(colorF);
			nodeActions.add(colorM);
			vis.putAction("nodes", nodeActions);

			// -- 6. the display and interactive controls -------------------------
			FxDisplay display = new FxDisplay(vis);
			display.addControlListener(new DragControl());

			// -- 7. launch the visualization -------------------------------------
			for(int i=0; EdgeRenderer.arrows.size()>i; i++) {
				System.out.println(EdgeRenderer.arrows.get(i));
			}
			root.getChildren().addAll(EdgeRenderer.arrows);
			root.setCenter(display);
			root.setBottom(buildControlPanel(display));
			vis.run("nodes");
			vis.run("layout");

		} catch (DataIOException e) {
			e.printStackTrace();
			System.err.println("Error loading graph. Exiting...");
			System.exit(1);
		}
	}

	private Node buildControlPanel(FxDisplay display) {
		VBox vbox = new VBox();
		Label txt = new Label("Zoom Factor");
		Slider slider = new Slider(0.0, 10.0, 1.0);
		Label txt2 = new Label("");
		display.zoomFactorProperty().bind(slider.valueProperty());
		vbox.getChildren().addAll(txt, slider, txt2);
		return vbox;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
