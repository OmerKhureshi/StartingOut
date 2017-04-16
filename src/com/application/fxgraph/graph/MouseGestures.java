package com.application.fxgraph.graph;

import com.application.db.DAOImplementation.CallTraceDAOImpl;
import com.application.db.DAOImplementation.ElementDAOImpl;
import com.application.db.DAOImplementation.MethodDefnDAOImpl;
import com.application.db.TableNames;
import com.application.fxgraph.cells.CircleCell;
import com.application.logs.fileHandler.MethodDefinitionLogFile;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.PopOver;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MouseGestures {

    final DragContext dragContext = new DragContext();

    Graph graph;

    public MouseGestures( Graph graph) {
        this.graph = graph;
    }

    public void makeDraggable( final Node node) {
        node.setOnMouseClicked(onMousePressedToShowInfoEventHandler);
        // node.setOnMousePressed(onMousePressedEventHandler);
        // node.setOnMouseDragged(onMouseDraggedEventHandler);
        // node.setOnMouseReleased(onMouseReleasedEventHandler);
    }
    EventHandler<MouseEvent> onMousePressedToShowInfoEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();
            CircleCell cell = (CircleCell) node;
            System.out.println("Clicked Cell: " + cell.getCellId());
            int idCallTrace = 0;
            String timeStamp = null;
            int methodId = 0, processId = 0;
            String parameters = null, packageName = null, methodName = null, parameterTypes = null;

            ResultSet callTraceRS = CallTraceDAOImpl.selectWhere("id = (Select id_call_trace FROM " + TableNames.ELEMENT_TABLE  +
                    " WHERE id = "+ cell.getCellId() +")");
            try {
                callTraceRS.next();
                timeStamp = callTraceRS.getString("time_instant");
                methodId = callTraceRS.getInt("method_id");
                processId = callTraceRS.getInt("process_id");
                parameters = callTraceRS.getString("parameters");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("On click: " + timeStamp.toString() + "; methodID: " + methodId);

            ResultSet methodDefnRS = MethodDefnDAOImpl.selectWhere("id = " + methodId);
            try {
                methodDefnRS.next();
                packageName = methodDefnRS.getString("package_name");
                methodName = methodDefnRS.getString("method_name");
                parameterTypes = methodDefnRS.getString("parameter_types");
                System.out.println(">> node width: " + ((CircleCell) node).getWidth() + " : height: " + ((CircleCell) node).getHeight());
                String str = "Method Name: " + methodName +
                        "Package Name: " +  packageName +
                        "Parameter Types: " + parameterTypes +
                        "Parameters: " + parameters;
                ObservableList<String> list = FXCollections.observableArrayList();
                list.add("Method Name: " + methodName);
                list.add("Package Name: " + packageName);
                list.add("Parameter Types: " + parameterTypes);
                list.add("Parameters: " + parameters);

                ListView<String> listView = new ListView<>(list);
                PopOver popOver = new PopOver(listView);
                popOver.show(node);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            // use cellId and get call trace info and method defn info
            // print on console
            // print on pop up.
        }
    };


    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();

            double scale = graph.getScale();

            dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
            dragContext.y = node.getBoundsInParent().getMinY()  * scale - event.getScreenY();
            // System.out.println("-----------");
            // System.out.println("event.getScreenX(): " + event.getScreenX());
            // System.out.println("event.getScreenY(): " + event.getScreenY());
            // System.out.println("");
            // System.out.println("node.getBoundsInParent().getMinX(): " + node.getBoundsInParent().getMinX());
            // System.out.println("node.getBoundsInParent().getMaxX(): " + node.getBoundsInParent().getMaxX());
            // System.out.println("node.getBoundsInParent().getMinY(): " + node.getBoundsInParent().getMinY());
            // System.out.println("node.getBoundsInParent().getMaxY(): " + node.getBoundsInParent().getMaxY());
            // System.out.println("");
            // System.out.println("node.getBoundsInLocal().getMinX(): " + node.getBoundsInLocal().getMinX());
            // System.out.println("node.getBoundsInLocal().getMinY(): " + node.getBoundsInLocal().getMinY());
            // System.out.println("node.getBoundsInLocal().getMaxX(): " + node.getBoundsInLocal().getMaxX());
            // System.out.println("node.getBoundsInLocal().getMaxY(): " + node.getBoundsInLocal().getMaxY());
            // System.out.println("");
            // System.out.println("node.getLayoutX(): " + node.getLayoutX());
            // System.out.println("node.getLayoutY(): " + node.getLayoutY());

        }
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();

            double offsetX = event.getScreenX() + dragContext.x;
            double offsetY = event.getScreenY() + dragContext.y;

            // adjust the offset in case we are zoomed
            double scale = graph.getScale();

            offsetX /= scale;
            offsetY /= scale;

            node.relocate(offsetX, offsetY);

        }
    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

        }
    };

    class DragContext {

        double x;
        double y;

    }
}