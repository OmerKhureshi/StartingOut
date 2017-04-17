package com.application.fxgraph.ElementHelpers;

import com.application.Main;
import com.application.db.DAOImplementation.ElementDAOImpl;
import com.application.db.DAOImplementation.ElementToChildDAOImpl;
import com.application.fxgraph.cells.CircleCell;
import com.application.fxgraph.graph.CellLayer;
import com.application.fxgraph.graph.Edge;
import com.application.fxgraph.graph.Graph;
import com.application.fxgraph.graph.Model;
import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class ConvertDBtoElementTree {
    private Map<Integer, Element> threadMapToRoot = new LinkedHashMap<>();
    public ArrayList<Element> rootsList;
    Element grandParent, parent, cur;

    public ConvertDBtoElementTree() {
        rootsList = new ArrayList<>();
    }

    public static Element greatGrandParent = new Element(null, -2);
    public void StringToElementList(List<String> line, int fkCallTrace) {
        String msg = line.get(3);  // ToDo replace hardcoded indices with universal indices.
        Integer threadId = Integer.valueOf(line.get(1));

        switch (msg.toUpperCase()) {
            case "ENTER":   // Todo Performance: Use int codes instead of String like "ENTER":
                if (!threadMapToRoot.containsKey(threadId)) {
                    // new thread
                    parent = null;
                } else {
                    parent = cur;
                }
                cur = new Element(parent, fkCallTrace);
                break;

            case "EXIT":
                cur = cur.getParent();
                break;

            default:
                IllegalStateException up = new IllegalStateException("EventType should be either ENTER OR EXIT. This line caused exception: " + line);
                throw up;  // Yuck! Not having any of that :(
        }

        if (parent == null && !msg.equalsIgnoreCase("EXIT")) {
            if (!threadMapToRoot.containsKey(threadId)) {
                grandParent = new Element(greatGrandParent, -1);
                grandParent.setChildren(new ArrayList<>(Arrays.asList(cur)));
                cur.setParent(grandParent);
                threadMapToRoot.put(threadId, grandParent);
                /*defaultInitialize(grandParent);
                ElementDAOImpl.insert(grandParent);*/
            } else {
                Element grandparent = threadMapToRoot.get(threadId);   // Get grandParent root for the current threadId
                grandparent.setChildren(new ArrayList<>(Collections.singletonList(cur)));       // set the current element as the child of the grandParent element.
                cur.setParent(grandparent);
            }
        }

        /*if ( msg.equalsIgnoreCase("ENTER")) {
            defaultInitialize(cur);
            ElementDAOImpl.insert(cur);
        }*/
    }

    private void defaultInitialize(Element element) {
        cur.setLeafCount(-1);
        cur.setLevelCount(-1);
        cur.getBoundBox().xTopLeft = -1;
        cur.getBoundBox().yTopLeft = -1;
        cur.getBoundBox().xTopRight = -1;
        cur.getBoundBox().yTopRight = -1;
        cur.getBoundBox().xBottomRight = -1;
        cur.getBoundBox().yBottomRight = -1;
        cur.getBoundBox().xBottomLeft = -1;
        cur.getBoundBox().yBottomLeft = -1;
    }

    public Map<Integer, Element> getThreadMapToRoot() {
        return threadMapToRoot;
    }

    /**
     * Calculates the Element properties on all direct and indirect children of current element.
     * Ensure that the sub tree is fully constructed before invoking this method.
     */
    public void calculateElementProperties() {
        /*
        Iterate through the threadMapToRoot and calculate Element properties for all the roots.
         */
        // threadMapToRoot.entrySet().stream()
        //         .map(Map.Entry::getValue)
        //         .forEachOrdered(root -> {
        //             root.calculateLeafCount();
        //             root.calculateLevelCount(0);
        //             root.setBoundBoxOnAll(root);
        //         });
        //
        greatGrandParent.calculateLeafCount();
        greatGrandParent.calculateLevelCount(0);
        greatGrandParent.setBoundBoxOnAll(greatGrandParent);

    }

    public void recursivelyInsertElementsIntoDB(Element root) {
        if (root == null)
            return;
        ElementDAOImpl.insert(root);
        ElementToChildDAOImpl.insert(
                root.getParent() == null? -1 : root.getParent().getElementId(),
                root.getElementId());
        if (root.getChildren() != null)
            root.getChildren().stream().forEachOrdered(this::recursivelyInsertElementsIntoDB);
    }

    public void getCirclesToLoadIntoViewPort(Graph graph) {
        ScrollPane scrollPane = graph.getScrollPane();
        Model model = graph.getModel();

        Map<String, CircleCell> mapCircleCellsOnUI = model.getMapCircleCellsOnUI();
        Map<String, Edge> mapEdgesOnUI = model.getMapEdgesOnUI();
        BoundingBox boundingBox = Graph.getViewPortDims(scrollPane);
        double viewPortMinX = boundingBox.getMinX();
        double viewPortMaxX = boundingBox.getMaxX();
        double viewPortMinY = boundingBox.getMinY();
        double viewPortMaxY = boundingBox.getMaxY();
        int offset = 40;
        String whereClause = "bound_box_x_coordinate > " + (viewPortMinX) +
                " AND bound_box_x_coordinate < " + (viewPortMaxX) +
                " AND bound_box_y_coordinate > " + (viewPortMinY + offset) +
                " AND bound_box_y_coordinate < " + (viewPortMaxY - offset);
        ResultSet rs = ElementDAOImpl.selectWhere(whereClause);
        CircleCell curCircleCell = null;
        CircleCell parentCircleCell = null;

        try {
            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String parentId = String.valueOf(rs.getInt("parent_id"));
                int collapsed = rs.getInt("collapsed");
                float xCoordinate = rs.getFloat("bound_box_x_coordinate");
                float yCoordinate = rs.getFloat("bound_box_y_coordinate");

                /*
                * collapsed - actions
                *     0     - Show cell on UI
                *     1     - parent of this cell was minimized. Don't show on UI
                *     2     - this cell was minimized. Show on UI.
                *     3     - parent of this cell was minimized. this cell was minimized. Don't expand this cell's children. Don't show on UI.
                */
                if (!mapCircleCellsOnUI.containsKey(id) && (collapsed == 0 || collapsed == 2)) {
                    curCircleCell = new CircleCell(id, xCoordinate, yCoordinate);
                    model.addCell(curCircleCell);
                    parentCircleCell = mapCircleCellsOnUI.get(parentId);
                    if (!mapCircleCellsOnUI.containsKey(parentId)) {
                        ResultSet rsTemp = ElementDAOImpl.selectWhere("id = " + parentId);
                        if (rsTemp.next()) {
                            float xCoordinateTemp = rsTemp.getFloat("bound_box_x_coordinate");
                            float yCoordinateTemp = rsTemp.getFloat("bound_box_y_coordinate");
                            parentCircleCell = new CircleCell(parentId, xCoordinateTemp, yCoordinateTemp);
                            model.addCell(parentCircleCell);
                        }
                    }
                }  else {
                    curCircleCell = mapCircleCellsOnUI.get(id);
                    parentCircleCell = mapCircleCellsOnUI.get(parentId);
                }

                if (curCircleCell != null && !model.getMapEdgesOnUI().containsKey(curCircleCell.getCellId()) && parentCircleCell != null) {
                    Edge curEdge = new Edge(parentCircleCell, curCircleCell);
                    model.addEdge(curEdge);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       removeFromUI(graph);
    }

    Object lock = Main.getLock();

    public void removeFromUI(Graph graph) {
        CellLayer cellLayer = (CellLayer) graph.getCellLayer();
        Model model = graph.getModel();
        ScrollPane scrollPane = graph.getScrollPane();

        Map<String, CircleCell> mapCircleCellsOnUI = model.getMapCircleCellsOnUI();
        List<String> removeCircleCells = new ArrayList<>();
        List<String> removeEdges = new ArrayList<>();
        List<CircleCell> listCircleCellsOnUI = model.getListCircleCellsOnUI();
        Map<String, Edge> mapEdgesOnUI = model.getMapEdgesOnUI();
        List<Edge> listEdgesOnUI = model.getListEdgesOnUI();

        BoundingBox curViewPort = Graph.getViewPortDims(scrollPane);
        double minX = curViewPort.getMinX();
        double minY = curViewPort.getMinY();

        int offset = 20;
        BoundingBox shrunkBB = new BoundingBox(minX + offset, minY + offset, curViewPort.getWidth() - (2 * offset), curViewPort.getHeight() - (2 * offset));

        synchronized (lock) {
            Iterator i = mapCircleCellsOnUI.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<String, CircleCell> entry = (Map.Entry) i.next();
                CircleCell cell = entry.getValue();
                if (!shrunkBB.contains(cell.getLayoutX(), cell.getLayoutY())) {
                    removeCircleCells.add(cell.getCellId());
                }
            }

            removeCircleCells.stream()
                    .forEach(cellId -> {
                        CircleCell circleCell = mapCircleCellsOnUI.get(cellId);
                        cellLayer.getChildren().remove(circleCell);
                        mapCircleCellsOnUI.remove(cellId);
                        listCircleCellsOnUI.remove(circleCell);
                    });

            Iterator j = mapEdgesOnUI.entrySet().iterator();
            while (j.hasNext()) {
                Map.Entry<String, Edge> entry = (Map.Entry) j.next();
                Edge edge = entry.getValue();
                Line line = (Line) edge.getChildren().get(0);
                BoundingBox lineBB = new BoundingBox(
                        line.getStartX(),
                        Math.min(line.getStartY(), line.getEndY()),
                        Math.abs(line.getEndX() - line.getStartX()),
                        Math.abs(line.getEndY() - line.getStartY()) );
                // if (!shrunkBB.contains(line.getEndX(), line.getEndY())) {
                //     removeEdges.add(edge.getEdgeId());
                // }
                if (!shrunkBB.intersects(lineBB)) {
                    removeEdges.add(edge.getEdgeId());
                }
            }

            removeEdges.stream()
                    .forEach(edgeId -> {
                        Edge edge = mapEdgesOnUI.get(edgeId);
                        cellLayer.getChildren().remove(edge);
                        mapEdgesOnUI.remove(edgeId);
                        listEdgesOnUI.remove(edge);
                    });
        }
    }
}

