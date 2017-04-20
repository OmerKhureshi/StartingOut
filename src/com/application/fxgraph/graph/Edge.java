package com.application.fxgraph.graph;

import com.application.fxgraph.ElementHelpers.Element;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Edge extends Group {
    protected Cell source;
    protected Cell target;

    private Element sourceElement;
    private Element targetElement;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    private String edgeId;
    Line line;

    public Edge(Cell source, Cell target) {
        edgeId = target.getCellId();
        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();

        // System.out.println("EDGE added");
        // System.out.println("  " + source.getLayoutX () + " : " + source.getLayoutY ());
        // System.out.println("  " + target.getLayoutX () + " : " + target.getLayoutY ());
        // System.out.println("  " + source.getWidth   () + " : " + source.getHeight  ());
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

        startX = source.getLayoutX();
        startY = source.getLayoutY();
        endX = target.getLayoutX();
        endY = target.getLayoutY();
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

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public Element getSourceElement() {
        return sourceElement;
    }

    public void setSourceElement(Element sourceElement) {
        this.sourceElement = sourceElement;
    }

    public Element getTargetElement() {
        return targetElement;
    }

    public void setTargetElement(Element targetElement) {
        this.targetElement = targetElement;
    }
}