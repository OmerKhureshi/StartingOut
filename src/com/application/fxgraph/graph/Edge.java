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

        line.setStartX    (source.getLayoutX        () + source.getPrefWidth       () * .5 );
        line.setStartY    (source.getLayoutY        () + source.getPrefHeight      () * .5 );
        line.setEndX      (target.getLayoutX        () + source.getPrefWidth       () * .5 );
        line.setEndY      (target.getLayoutY        () + source.getPrefHeight      () * .5 );

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

    public Edge(String edgeId, double startX, double endX, double startY, double endY) {
        this.edgeId = edgeId;
        // this.source = source;
        // this.target = target;
        //
        // source.addCellChild(target);
        // target.addCellParent(source);

        line = new Line();

        line.setStartX(startX + Cell.prefWidth * .5 ) ;
        line.setStartY(startY + Cell.prefHeight * .5 );
        line.setEndX(endX + Cell.prefWidth * .5 );
        line.setEndY(endY + Cell.prefHeight * .5 );

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

    public void createLine() {

    }

    public String getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = edgeId;
    }

}