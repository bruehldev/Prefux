
package prefux.visual.tuple;

import javafx.application.Application
import javafx.application.Platform;
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.stage.Stage
import prefux.Visualization
import prefux.data.Graph
import prefux.data.io.GraphMLReader
import prefux.visual.VisualItem
import prefux.visual.VisualTable
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

public class TableVisualItemTest extends Specification {

    public static class AsNonApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // noop
        }
    }

    def setupSpec() {
        Thread t = new Thread("JavaFX Init Thread") {
                    public void run() {
                        Application.launch(AsNonApp.class, new String[0]);
                    }
                };
        t.setDaemon(true);
        t.start();
    }

    @Shared
    TableVisualItem item

    @Shared
    VisualTable vt

    def setup() {
        Graph graph = new GraphMLReader().readGraph("data/socialnet.xml");
        Visualization vis = new Visualization();
        vt = new VisualTable(vis,"test");
        vt.addRow();
        item = new TableVisualItem()
        item.init(vt, graph, 0)
    }

    @Unroll
    def "check fx #prop properties with numbers: #a, #b, #c"() {
        /*
         * Using synchronized to get changes by the fx thread.
         * May lead to errors if the JavaFx run queue has more elements
         */
        
        when:
        item."set${prop.capitalize()}"(a)

        then:
        synchronized(item) {
            item."${prop}Property"().get()==a
        }

        when:
        vt.setDouble(0, VisualItem."${prop.toUpperCase()}", b)

        then:
        synchronized(item) {
            item."${prop}Property"().get()==b
        }

        when:
        Double newV=-1.0
        item."${prop}Property"().addListener({ ObservableValue o,Object oldVal, Object newVal ->
            newV=newVal
        } as ChangeListener<Double>)
        item."set${prop.capitalize()}"(c)

        then:
        synchronized(item) {
            item."${prop}Property"().get()==c
            newV==c
        }

        where:
        a      |    b    |     c     |   prop
        255.75 |  315.77 |  8885.25  |   "x"
        255.75 |  315.77 |  8885.25  |   "y"
        255.75 |  315.77 |  8885.25  |   "startX"
        255.75 |  315.77 |  8885.25  |   "startY"
        255.75 |  315.77 |  8885.25  |   "endX"
        255.75 |  315.77 |  8885.25  |   "endY"
        15.01 |   44.44 |    84.124 |   "x"
        15.01 |   44.44 |    84.124 |   "y"
        15.01 |   44.44 |    84.124 |   "startX"
        15.01 |   44.44 |    84.124 |   "startY"
        15.01 |   44.44 |    84.124 |   "endX"
        15.01 |   44.44 |    84.124 |   "endY"
    }
}
