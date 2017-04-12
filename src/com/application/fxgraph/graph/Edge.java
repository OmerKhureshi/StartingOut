package com.application.fxgraph.graph;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Edge extends Group {

    protected Cell source;
    protected Cell target;

    private String edgeId;

    Line line;

    public Edge(Cell source, Cell target) {
        edgeId = target.getCellId();
        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();
        line.setStartX(source.getLayoutX());
        line.setStartY(source.getLayoutY());
        line.setEndX(target.getLayoutX());
        line.setEndY(target.getLayoutY());

        // line.startXProperty().bind( source.layoutXProperty().add(source.getLayoutX()));
        // line.startYProperty().bind( source.layoutYProperty().add(source.getLayoutY()));
        // line.endXProperty().bind( target.layoutXProperty().add( target.getLayoutX()));
        // line.endYProperty().bind( target.layoutYProperty().add( target.getLayoutY()));

        // Bind a line to the source and target cells.
        // line.startXProperty().bind( source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        // line.startYProperty().bind( source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));
        // line.endXProperty().bind( target.layoutXProperty().add( target.getBoundsInParent().getWidth() / 2.0));
        // line.endYProperty().bind( target.layoutYProperty().add( target.getBoundsInParent().getHeight() / 2.0));

        // System.out.println( source.getCellId() + ": layoutX: " + source.getLayoutX() + "; layoutY: " + source.getLayoutY() + "; width: " + source.getWidth() + "; height: " + source.getHeight());
        // System.out.println( target.getCellId() + ": layoutX: " + target.getLayoutX() + "; layoutY: " + target.getLayoutY() + "; width: " + target.getWidth() + "; height: " + target.getHeight());
        // System.out.println(getEdgeId() + ": Line: " + line.getStartX() + "; end: " + line.getEndX());

        getChildren().add( line);

    }

    public String getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = edgeId;
    }

    public Cell getSource() {
        return source;
    }

    public Cell getTarget() {
        return target;
    }

}