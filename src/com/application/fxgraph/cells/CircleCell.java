package com.application.fxgraph.cells;

import com.application.fxgraph.ElementHelpers.Element;
import com.application.fxgraph.graph.Cell;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleCell extends Cell {

    public CircleCell(String id) {
        super( id);
        double width = 10;
        double height = 10;

       // Polygon view = new Polygon( width / 2, 0, width, height, 0, height);
        Group group = new Group();
        Circle view = new Circle(20);
        Label label = new Label(id);
        group.getChildren().addAll(view, label);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);
        setView( group);
    }

    public CircleCell (String id, Element element) {
        this(id);
        this.relocate(
                element.getBoundBox().xCoordinate,
                element.getBoundBox().yCoordinate
        );
    }
    public CircleCell (String id, float xCoordinate, float yCoordinate) {
        this(id);
        this.relocate(xCoordinate, yCoordinate);
    }


}