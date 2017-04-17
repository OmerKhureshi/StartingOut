package com.application.fxgraph.graph;

import com.application.db.DAOImplementation.CallTraceDAOImpl;
import com.application.db.DAOImplementation.ElementDAOImpl;
import com.application.db.DAOImplementation.ElementToChildDAOImpl;
import com.application.db.DAOImplementation.MethodDefnDAOImpl;
import com.application.db.TableNames;
import com.application.fxgraph.ElementHelpers.ConvertDBtoElementTree;
import com.application.fxgraph.ElementHelpers.Element;
import com.application.fxgraph.cells.CircleCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.PopOver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MouseGestures {

    final DragContext dragContext = new DragContext();

    Graph graph;

    public MouseGestures( Graph graph) {
        this.graph = graph;
    }

    public void makeDraggable(final Node node) {
        // node.addEventFilter(MouseEvent.ANY, onMouseHoverToShowInfoEventHandler);

        // node.addEventFilter(MouseEvent.ANY, event -> System.out.println(event));
        node.setOnMousePressed(onMousePressedToCollapseTree);
        // node.setOnMousePressed(onMousePressedToCollapseTree);
        // node.setOnMousePressed(onMousePressedEventHandler);
        // node.setOnMouseDragged(onMouseDraggedEventHandler);
        // node.setOnMouseReleased(onMouseReleasedEventHandler);
    }

    EventHandler<MouseEvent> onMouseHoverToShowInfoEventHandler = new EventHandler<MouseEvent>() {

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
                if (callTraceRS.next()) {
                    timeStamp = callTraceRS.getString("time_instant");
                    methodId = callTraceRS.getInt("method_id");
                    processId = callTraceRS.getInt("process_id");
                    parameters = callTraceRS.getString("parameters");
                    ResultSet methodDefRS = MethodDefnDAOImpl.selectWhere("id = " + methodId);

                    methodDefRS.next();
                    packageName = methodDefRS.getString("package_name");
                    methodName = methodDefRS.getString("method_name");
                    parameterTypes = methodDefRS.getString("parameter_types");
                    ObservableList<String> list = FXCollections.observableArrayList();
                    list.add("Method Name: " + methodName);
                    list.add("Package Name: " + packageName);
                    list.add("Parameter Types: " + parameterTypes);
                    list.add("Parameters: " + parameters);

                    ListView<String> listView = new ListView<>(list);
                    PopOver popOver = new PopOver(listView);
                    popOver.setMaxHeight(40);
                    popOver.show(node);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    EventHandler<MouseEvent> onMousePressedToCollapseTree = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            CellLayer cellLayer = (CellLayer) graph.getCellLayer();
            CircleCell cell = (CircleCell) event.getSource();
            String cellId =  cell.getCellId();
            ResultSet cellRS = ElementDAOImpl.selectWhere("id = " + cellId);
            int collapsed = 0;
            try {
                if (cellRS.next()) {
                    collapsed = cellRS.getInt("collapsed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            /*
             * collapsed - actions
             *     0     - Show cell on UI. Starting value for all cells.
             *     1     - parent of this cell was minimized. Don't show on UI
             *     2     - this cell was minimized. Show on UI.
             *     3     - parent of this cell was minimized. this cell was minimized. Don't expand this cell's children. Don't show on UI.
             */
            if (collapsed == 1) {
                // expand sub tree.
                System.out.println("onMousePressedToCollapseTree: cell: " + cellId + " ; collapsed: " + collapsed);
            } else if (collapsed == 0) {
                System.out.println("onMousePressedToCollapseTree: cell: " + cellId + " ; collapsed: " + collapsed);
                ElementDAOImpl.updateWhere("collapsed", "2", "id = " + cellId);

                Map<String, CircleCell> mapCircleCellsOnUI = graph.getModel().getMapCircleCellsOnUI();
                List<CircleCell> listCircleCellsOnUI = graph.getModel().getListCircleCellsOnUI();
                List<String> removeCircleCells = new ArrayList<>();
                recursivelyRemove(cellId, mapCircleCellsOnUI, removeCircleCells);
                //noinspection Duplicates
                removeCircleCells.stream()
                        .forEach(circleCellId -> {
                            CircleCell circleCell = mapCircleCellsOnUI.get(circleCellId);
                            cellLayer.getChildren().remove(circleCell);
                            mapCircleCellsOnUI.remove(circleCellId);
                            listCircleCellsOnUI.remove(circleCell);
                        });
            } else if (collapsed == 2) {
                // expand now.
                System.out.println("onMousePressedToCollapseTree: cell: " + cellId + " ; collapsed: " + collapsed);

                recursivelyAdd(cellId);
            } else if (collapsed == 3) {
                System.out.println("onMousePressedToCollapseTree: cell: " + cellId + " ; collapsed: " + collapsed);
                throw new IllegalStateException("This cell should not have been on the UI.");
            }
        }
    };

    public void recursivelyAdd(String cellId) {
        ResultSet elementRS = ElementDAOImpl.selectWhere("id = " + cellId);
        System.out.println("recursivelyAdd: cellid: " + cellId);
        try {
            if (elementRS.next()) {
                int collapsed = elementRS.getInt("collapsed");
                if (collapsed == 0) {
                    System.out.println("recursivelyAdd: cellid: " + cellId + " ; collapsed: " + collapsed);
                    throw new IllegalStateException("Collapsed cannot be 0 here.");
                } else if (collapsed == 1) {
                    System.out.println("recursivelyAdd: cellid: " + cellId + " ; collapsed: " + collapsed);

                    // update collapsed = 0
                    // show this cell.
                    // recurse to children.
                    ElementDAOImpl.updateWhere("collapsed", "0", "id = " + cellId);
                    float xCoordinateTemp = elementRS.getFloat("bound_box_x_coordinate");
                    float yCoordinateTemp = elementRS.getFloat("bound_box_y_coordinate");
                    CircleCell cell = new CircleCell(cellId, xCoordinateTemp, yCoordinateTemp);
                    graph.getModel().addCell(cell);
                    graph.myEndUpdate();
                    ResultSet childrenRS = ElementToChildDAOImpl.selectWhere("parent_id = " + cellId);
                    while (childrenRS.next()) {
                        String childId = String.valueOf(childrenRS.getInt("child_id"));
                        recursivelyAdd(childId);
                    }

                } else if (collapsed == 2) {
                    System.out.println("recursivelyAdd: cellid: " + cellId + " ; collapsed: " + collapsed);

                    // update collapsed=0
                    ElementDAOImpl.updateWhere("collapsed", "0", "id = " + cellId);
                    // for all children with collapsed=1, show and update collapsed=0
                    ResultSet childrenRS = ElementToChildDAOImpl.selectWhere("parent_id = " + cellId);
                    while (childrenRS.next()) {
                        String childId = String.valueOf(childrenRS.getInt("child_id"));
                        recursivelyAdd(childId);
                    }

                } else if (collapsed == 3) {
                    System.out.println("recursivelyAdd: cellid: " + cellId + " ; collapsed: " + collapsed);
                    // update collapsed=2
                    ElementDAOImpl.updateWhere("collapsed", "2", "id = " + cellId);
                    // this cell is hidden. make it visible.
                    float xCoordinateTemp = elementRS.getFloat("bound_box_x_coordinate");
                    float yCoordinateTemp = elementRS.getFloat("bound_box_y_coordinate");
                    CircleCell cell = new CircleCell(cellId, xCoordinateTemp, yCoordinateTemp);
                    graph.getModel().addCell(cell);
                    graph.myEndUpdate();
                    // stop here. dont show children.
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recursivelyRemove(String cellId, Map<String, CircleCell> mapCircleCellsOnUI, List<String> removeCircleCells) {

        System.out.println("recursivelyRemove: cellid: " + cellId);

        ResultSet childrenRS = ElementToChildDAOImpl.selectWhere("parent_id = " + cellId);
        try {
            while (childrenRS.next()) {
                String childId = String.valueOf(childrenRS.getInt("child_id"));
                if (mapCircleCellsOnUI.containsKey(cellId)) {
                    removeCircleCells.add(childId);
                }
                ElementDAOImpl.updateWhere("collapsed", "1",
                        "id = " + childId + " AND collapsed = 0");
                ElementDAOImpl.updateWhere("collapsed", "3",
                        "id = " + childId + " AND collapsed = 2");
                recursivelyRemove(childId, mapCircleCellsOnUI, removeCircleCells);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recursivelyUpdateColumn(String cellId) {
        System.out.println("Updating Cell: " + cellId);
        ResultSet childrenRS = ElementToChildDAOImpl.selectWhere("parent_id = " + cellId);
        try {
            while (childrenRS.next()) {
                recursivelyUpdateColumn(String.valueOf(childrenRS.getInt("child_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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