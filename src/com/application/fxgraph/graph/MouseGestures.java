package com.application.fxgraph.graph;

import com.application.db.DAOImplementation.CallTraceDAOImpl;
import com.application.db.DAOImplementation.ElementDAOImpl;
import com.application.db.DAOImplementation.ElementToChildDAOImpl;
import com.application.db.DAOImplementation.MethodDefnDAOImpl;
import com.application.db.TableNames;
import com.application.fxgraph.cells.CircleCell;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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
        node.setOnMouseEntered(onMouseHoverToShowInfoEventHandler);
        node.setOnMouseExited(onMouseExitToDismissPopover);
        // node.setOnMousePressed(onMousePressedEventHandler);
        node.setOnMouseDragged(onMouseDraggedEventHandler);
        node.setOnMouseReleased(onMouseReleasedEventHandler);
    }

    PopOver popOver;

    EventHandler<MouseEvent> onMouseHoverToShowInfoEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();
            CircleCell cell = (CircleCell) node;
            System.out.println("Clicked Cell: " + cell.getCellId());
            String timeStamp;
            int methodId,  processId, threadId;
            String parameters, packageName, methodName, parameterTypes;

            ResultSet callTraceRS = CallTraceDAOImpl.selectWhere("id = (Select id_call_trace FROM " + TableNames.ELEMENT_TABLE  +
                    " WHERE id = "+ cell.getCellId() +")");
            try {
                if (callTraceRS.next()) {
                    timeStamp = callTraceRS.getString("time_instant");
                    methodId = callTraceRS.getInt("method_id");
                    processId = callTraceRS.getInt("process_id");
                    threadId = callTraceRS.getInt("thread_id");
                    parameters = callTraceRS.getString("parameters");
                    ResultSet methodDefRS = MethodDefnDAOImpl.selectWhere("id = " + methodId);

                    methodDefRS.next();
                    packageName = methodDefRS.getString("package_name");
                    methodName = methodDefRS.getString("method_name");
                    parameterTypes = methodDefRS.getString("parameter_types");
                    // ObservableList<String> list = FXCollections.observableArrayList();
                    // list.add("Method Name: " + methodName);
                    // list.add("Package Name: " + packageName);
                    // list.add("Parameter Types: " + parameterTypes);
                    // list.add("Parameters: " + parameters);
                    // ListView<String> listView = new ListView<>(list);

                    Label lMethodName = new Label(methodName);
                    Label lPackageName = new Label(packageName);
                    Label lParameterTypes = new Label(parameterTypes);
                    Label lParameters = new Label(parameters);
                    Label lProcessId = new Label(String.valueOf(processId));
                    Label lThreadId = new Label(String.valueOf(threadId));
                    Label lTimeInstant = new Label(timeStamp);

                    GridPane gridPane = new GridPane();
                    gridPane.setPadding(new Insets(10, 10, 10, 10));
                    gridPane.setVgap(10);
                    gridPane.setHgap(20);
                    gridPane.add(new Label("Method Name: "), 0, 0);
                    gridPane.add(lMethodName, 1,0);

                    gridPane.add(new Label("Package Name: "), 0, 1);
                    gridPane.add(lPackageName, 1,1);

                    gridPane.add(new Label("Parameter Types: "), 0, 2);
                    gridPane.add(lParameterTypes, 1,2);

                    gridPane.add(new Label("Parameters: "), 0, 3);
                    gridPane.add(lParameters, 1,3);

                    gridPane.add(new Label("Process ID: "), 0, 4);
                    gridPane.add(lProcessId, 1,4);

                    gridPane.add(new Label("Thread ID: "), 0, 5);
                    gridPane.add(lThreadId, 1,5);

                    gridPane.add(new Label("Time of Invocation: "), 0, 6);
                    gridPane.add(lTimeInstant, 1,6);

                    popOver = new PopOver(gridPane);
                    popOver.show(node);

                    // Popup popup = new Popup();
                    // popup.getContent().add(gridPane);
                    // popup.show(node.getScene().getWindow());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    EventHandler<MouseEvent> onMouseExitToDismissPopover = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            System.out.println("Exited");
            popOver.hide();
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

                Map<String, Edge> mapEdgesOnUI = graph.getModel().getMapEdgesOnUI();
                List<Edge> listEdgesOnUI = graph.getModel().getListEdgesOnUI();
                List<String> removeEdges = new ArrayList<>();

                recursivelyRemove(cellId, removeCircleCells, removeEdges);
                removeCircleCells.forEach(circleCellId -> {
                            if (mapCircleCellsOnUI.containsKey(circleCellId)) {
                                CircleCell circleCell = mapCircleCellsOnUI.get(circleCellId);
                                cellLayer.getChildren().remove(circleCell);
                                mapCircleCellsOnUI.remove(circleCellId);
                                listCircleCellsOnUI.remove(circleCell);
                            }
                        });

                removeEdges.forEach(edgeId -> {
                            if (mapEdgesOnUI.containsKey(edgeId)) {
                                Edge edge = mapEdgesOnUI.get(edgeId);
                                cellLayer.getChildren().remove(edge);
                                mapEdgesOnUI.remove(edgeId);
                                listEdgesOnUI.remove(edge);
                            }
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
        try {
            if (elementRS.next()) {
                int collapsed = elementRS.getInt("collapsed");
                if (collapsed == 0) {
                    throw new IllegalStateException("Collapsed cannot be 0 here.");
                } else if (collapsed == 1) {
                    ElementDAOImpl.updateWhere("collapsed", "0", "id = " + cellId);
                    float xCoordinateTemp = elementRS.getFloat("bound_box_x_coordinate");
                    float yCoordinateTemp = elementRS.getFloat("bound_box_y_coordinate");
                    CircleCell cell = new CircleCell(cellId, xCoordinateTemp, yCoordinateTemp);
                    graph.getModel().addCell(cell);

                    ResultSet parentRS = ElementToChildDAOImpl.selectWhere("child_id = " + cellId);
                    if (parentRS.next()) {
                        String parentId = String.valueOf(parentRS.getInt("parent_id"));
                        CircleCell parentCell = graph.getModel().getMapCircleCellsOnUI().get(parentId);
                        Edge edge = new Edge(parentCell, cell);
                        graph.getModel().addEdge(edge);
                    }
                    graph.myEndUpdate();

                    ResultSet childrenRS = ElementToChildDAOImpl.selectWhere("parent_id = " + cellId);
                    while (childrenRS.next()) {
                        String childId = String.valueOf(childrenRS.getInt("child_id"));
                        recursivelyAdd(childId);
                    }

                } else if (collapsed == 2) {
                    // update collapsed=0
                    ElementDAOImpl.updateWhere("collapsed", "0", "id = " + cellId);
                    // for all children with collapsed=1, show and update collapsed=0
                    ResultSet childrenRS = ElementToChildDAOImpl.selectWhere("parent_id = " + cellId);
                    while (childrenRS.next()) {
                        String childId = String.valueOf(childrenRS.getInt("child_id"));
                        recursivelyAdd(childId);
                    }

                } else if (collapsed == 3) {
                    ElementDAOImpl.updateWhere("collapsed", "2", "id = " + cellId);
                    float xCoordinateTemp = elementRS.getFloat("bound_box_x_coordinate");
                    float yCoordinateTemp = elementRS.getFloat("bound_box_y_coordinate");
                    CircleCell cell = new CircleCell(cellId, xCoordinateTemp, yCoordinateTemp);
                    graph.getModel().addCell(cell);
                    graph.myEndUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recursivelyRemove(String cellId, List<String> removeCircleCells, List<String> removeEdges ) {
        ResultSet childrenRS = ElementToChildDAOImpl.selectWhere("parent_id = " + cellId);
        try {
            while (childrenRS.next()) {
                String childId = String.valueOf(childrenRS.getInt("child_id"));
                removeCircleCells.add(childId);
                removeEdges.add(childId);
                ElementDAOImpl.updateWhere("collapsed", "1",
                        "id = " + childId + " AND collapsed = 0");
                ElementDAOImpl.updateWhere("collapsed", "3",
                        "id = " + childId + " AND collapsed = 2");
                recursivelyRemove(childId, removeCircleCells, removeEdges);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unused")
    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Node node = (Node) event.getSource();
            double scale = graph.getScale();
            dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
            dragContext.y = node.getBoundsInParent().getMinY()  * scale - event.getScreenY();
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

    EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {};
    class DragContext {
        double x;
        double y;
    }
}