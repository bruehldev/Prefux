package fx;

import java.util.Iterator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
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
import prefux.action.assignment.ColorAction;
import prefux.action.assignment.DataColorAction;
import prefux.action.assignment.NodeDegreeSizeAction;
import prefux.action.layout.graph.ForceDirectedLayout;
import prefux.activity.Activity;
import prefux.controls.DragControl;
import prefux.controls.OwnControl;
import prefux.data.Edge;
import prefux.data.Graph;
import prefux.data.Table;
import prefux.data.expression.Predicate;
import prefux.data.expression.parser.ExpressionParser;
import prefux.render.DefaultRendererFactory;
import prefux.render.EdgeRenderer;
import prefux.render.LabelRenderer;
import prefux.render.ShapeRenderer;
import prefux.util.ColorLib;
import prefux.util.PrefuseLib;
import prefux.visual.NodeItem;
import prefux.visual.VisualItem;

public class JavaFxSample extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private static final double WIDTH = 900;
	private static final double HEIGHT = 750;
	private static final String GROUP = "graph";
	private CheckBox keyCB = new CheckBox();
	private CheckBox titleCB = new CheckBox();
	private OwnControl cl = new OwnControl();
	private BorderPane root = new BorderPane();

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Sample Graph");
		//BorderPane root = new BorderPane();

		primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
		root.getStyleClass().add("display");
		primaryStage.show();


			// graph = new GraphMLReader().readGraph("data/graphml-sample.xml");
			// Create tables for node and edge data, and configure their columns.
			Table nodeData = new Table();
			Table edgeData = new Table();
			nodeData.addColumn("bibtexkey", String.class);
			nodeData.addColumn("gender", String.class);
			nodeData.addColumn("age", int.class);
			edgeData.addColumn(Graph.DEFAULT_SOURCE_KEY, int.class);
			edgeData.addColumn(Graph.DEFAULT_TARGET_KEY, int.class);
			edgeData.addColumn("label", String.class);
			// Need more data in your nodes or edges?  Just add more
			// columns.

		// begin: graph manually created
			// Create Graph backed by those tables.  Note that I'm
			// creating a directed graph here also.
			Graph graph = new Graph(nodeData, edgeData, true);

			// Create some nodes and edges, each carrying some data.
			// There are surely prettier ways to do this, but for the
			// example it gets the job done.
			//for (int i = 0; i < 3; ++i) {
				prefux.data.Node n1 = graph.addNode();
				prefux.data.Node n2 = graph.addNode();
				prefux.data.Node n3 = graph.addNode();
				prefux.data.Node n4 = graph.addNode();
				n1.set("bibtexkey", "Jeff");
				n1.set("gender", "M");
				n1.set("age", 31);
		n2.set("bibtexkey", "Mek");
		n2.set("gender", "M");
		n2.set("age", 91);
		n3.set("bibtexkey", "Jonny");
		n3.set("gender", "F");
		n3.set("age", 11);
		n4.set("bibtexkey", "Me");
		n4.set("gender", "F");
		n4.set("age", 10);
				Edge e1 = graph.addEdge(n1, n2);
				Edge e2 = graph.addEdge(n1, n3);
				Edge e3 = graph.addEdge(n4, n4);
				//Edge e3 = g.addEdge(n2, n3);
				e1.setString("label", "a");
				//e2.setString("label", "b");
				//e3.setString("label", "c");
			//}

		Visualization vis = new Visualization();
			vis.add(GROUP, graph);
		// end: copy

			ShapeRenderer female = new ShapeRenderer();
			female.setFillMode(ShapeRenderer.GRADIENT_SPHERE);
			LabelRenderer lr = new LabelRenderer("bibtexkey");
			ShapeRenderer male = new ShapeRenderer();
			male.setFillMode(ShapeRenderer.GRADIENT_SPHERE);
			 lr.translate(5.0, 5.0);
			// LabelRenderer lr2 = new LabelRenderer("name");
			// lr2.addStyle("invisible");
			// BorderPaneRenderer r = new BorderPaneRenderer();
//			CombinedRenderer r = new CombinedRenderer();
		//	r.add(lr);
//			r.add(sr);

			// create a new default renderer factory
			// return our name label renderer as the default for all
			// non-EdgeItems
			// includes straight line edges for EdgeItems by default
			DefaultRendererFactory rfa = new DefaultRendererFactory();
			Predicate expMale = ExpressionParser.predicate("gender='M'");
			Predicate expFemale = ExpressionParser.predicate("gender='F'");
			rfa.add(expMale, male);
			rfa.add(expFemale, female);
			vis.setRendererFactory(rfa);

			ActionList layout = new ActionList(Activity.INFINITY);
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
        int[] palette = new int[] {
                ColorLib.rgb(255,180,180), ColorLib.rgb(190,190,255)
        };
        ColorAction nStroke = new ColorAction("graph.nodes", VisualItem.STROKECOLOR);
        nStroke.setDefaultColor(ColorLib.gray(100));
        DataColorAction nFill = new DataColorAction("graph.nodes", "gender",
                Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
        ColorAction edges = new ColorAction("graph.edges",
                VisualItem.STROKECOLOR, ColorLib.gray(200));
        ColorAction arrow = new ColorAction("graph.edges",
                VisualItem.FILLCOLOR, ColorLib.gray(200));
        ActionList color = new ActionList();
        color.add(nStroke);
        color.add(nFill);
        color.add(edges);
        color.add(arrow);

			DataColorAction colorF = new DataColorAction(NODES, expFemale, "age",
			        Constants.NUMERICAL, VisualItem.FILLCOLOR, palette);
			DataColorAction colorM = new DataColorAction(NODES, expMale, "age",
			        Constants.NUMERICAL, VisualItem.FILLCOLOR, palette);
			nodeActions.add(colorF);
			nodeActions.add(colorM);
			vis.putAction("nodes", nodeActions);
            vis.putAction("color" ,color);

			FxDisplay display = new FxDisplay(vis);
		display.prefWidth(root.getPrefWidth());
		display.prefHeight(root.getPrefHeight());

			display.addControlListener(new DragControl());
		root.prefWidthProperty().bind(primaryStage.widthProperty());
		root.prefHeightProperty().bind(primaryStage.heightProperty());

		//Bind pref Witdh and Height to according stage
		root.prefWidthProperty().bind(primaryStage.widthProperty());
		root.prefHeightProperty().bind(primaryStage.heightProperty());

		display.prefHeight(root.getPrefHeight());
		display.prefWidth(root.getPrefWidth());

		for (int i = 0; EdgeRenderer.arrowHeadList.size() > i; i++) {
			display.getChildren().add(EdgeRenderer.arrowHeadList.get(i));
		}
			root.setCenter(display);
			root.setBottom(buildControlPanel(display));
			root.setTop(showKeyCheckBox(display, root));

		vis.run("nodes");
		vis.run("color");
		vis.run("layout");
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

	private Node showKeyCheckBox(FxDisplay display,  BorderPane pane) {
		// HERE NEXT - most important dude
		VBox vbox = new VBox();
		keyCB.setText("Show all keys");
		titleCB.setText("Show all keys");
		//attach click-method to all 3 checkboxes
		keyCB.setOnAction(e -> handleCheckBoxAction(e, display,  pane));
		titleCB.setOnAction(e -> handleCheckBoxAction(e, display,  pane));
		vbox.getChildren().addAll(keyCB, titleCB);
		return vbox;
	}

	private void handleCheckBoxAction(ActionEvent e, FxDisplay display, BorderPane pane) {
		if(keyCB.isSelected()){
			// Make bullshit
			Iterator<VisualItem> it = display.getVisualization().items();
			while(it.hasNext()) {
				if(it.hasNext()) {
					VisualItem item = it.next();
					if (item instanceof NodeItem) {
						cl.itemEvent(item, display, pane);
					}
				}
			}
		}
		else {
			cl.hideToolTips();
		}
		if(titleCB.isSelected()){
			display.setTranslateX(100 - display.getLayoutBounds().getMinX());
			display.setTranslateY(100 - display.getLayoutBounds().getMinY());
			display.computeAreaInScreen();




		}
		else {

			display.setTranslateX(0);
			display.setTranslateY(0);
		}
	}
}
