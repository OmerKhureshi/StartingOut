package com.application.fxgraph.graph;

import com.application.fxgraph.ElementHelpers.Element;
import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class Graph {

    private Model model;

    private Group canvas;

    private ZoomableScrollPane scrollPane;
//    private ScrollPane scrollPane;
    MouseGestures mouseGestures;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    static CellLayer cellLayer;

    public Graph() {
        this.model = new Model();
        canvas = new Group();
        cellLayer = new CellLayer();
        canvas.getChildren().add(cellLayer);
        mouseGestures = new MouseGestures(this);
        scrollPane = new ZoomableScrollPane(canvas);
//        scrollPane = new ScrollPane(canvas);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    public static void drawPlaceHolderLines() {
        Line hPlaceHolderLine = new Line(0, 0, (Element.getMaxLevelCount() + 1) * BoundBox.unitWidthFactor, 0);
        // hPlaceHolderLine.setStrokeWidth(2);
        cellLayer.getChildren().add(hPlaceHolderLine);

        Line vPlaceHolderLine = new Line(0, 0, 0, Element.getMaxLeafCount() * BoundBox.unitHeightFactor);
        // vPlaceHolderLine.setStrokeWidth(2);
        cellLayer.getChildren().add(vPlaceHolderLine);
        System.out.println("Lines have been drawn: level: " + Element.getMaxLevelCount() * BoundBox.unitWidthFactor + "; leaf: " + Element.getMaxLeafCount() * BoundBox.unitHeightFactor );
    }
    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public Pane getCellLayer() {
        return this.cellLayer;
    }

    public Model getModel() {
        return model;
    }

    public void beginUpdate() {
    }

    public void myEndUpdate() {
        model.listCircleCellsOnUI.stream()
                .map(item -> item.getCellId())
                .forEach(System.out::println);

        getCellLayer().getChildren().addAll(model.listCircleCellsOnUI);
        getCellLayer().getChildren().addAll(model.listEdgesOnUI);

        model.listCircleCellsOnUI.stream()
                .forEach(circleCell -> mouseGestures.makeDraggable(circleCell));

        model.clearListCircleCellsOnUI();
        model.clearListEdgesOnUI();
    }

    public void endUpdate() {
        // add components to graph pane
        getCellLayer().getChildren().addAll(model.getAddedEdges());
        getCellLayer().getChildren().addAll(model.getAddedCells());

        // remove components from graph pane
        getCellLayer().getChildren().removeAll(model.getRemovedCells());
        getCellLayer().getChildren().removeAll(model.getRemovedEdges());

        // enable dragging of cells
        for (Cell cell : model.getAddedCells()) {
            mouseGestures.makeDraggable(cell);
        }

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedCells());

        // remove reference to graphParent
        getModel().disconnectFromGraphParent(model.getRemovedCells());

        // merge added & removed cells with all cells
        getModel().merge();

    }

    public double getScale() {
//        throw new IllegalStateException(">>>>>>>>>> Invoking getScale()");
        return this.scrollPane.getScaleValue();
    }

    public static BoundingBox getViewPortDims(ScrollPane scrollPane) {
        // http://stackoverflow.com/questions/26240501/javafx-scrollpane-update-viewportbounds-on-scroll
        double hValue = scrollPane.getHvalue();  // horizontal scroll bar position.
        double contentWidth = scrollPane.getContent().getLayoutBounds().getWidth();
        double viewportWidth = scrollPane.getViewportBounds().getWidth();

        double vValue = scrollPane.getVvalue();
        double contentHeight = scrollPane.getContent().getLayoutBounds().getHeight();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();

        double minX = hValue * (contentWidth - viewportWidth);
        double minY = vValue * (contentHeight - viewportHeight);
        System.out.println("Scrollpane height: " + scrollPane.getViewportBounds().getHeight() + " : width: " + scrollPane.getViewportBounds().getWidth());
        BoundingBox boundingBox = new BoundingBox(minX, minY, viewportWidth, viewportHeight);
        return boundingBox;
    }
}