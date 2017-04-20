package com.application.fxgraph.cells;

import com.application.fxgraph.ElementHelpers.Element;
import com.application.fxgraph.graph.Cell;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleCell extends Cell {

    Label label;

    public CircleCell(String id) {
        super( id);

       // Polygon circle = new Polygon( width / 2, 0, width, height, 0, height);
        Group group = new Group();
        Circle circle = new Circle(20);
        label = new Label();
        group.getChildren().addAll(circle, label);

        circle.setStroke(Color.RED);
        circle.setFill(Color.RED);
        setView(group);
        circle.setOnMousePressed(event -> {
            // ((Circle)event.getSource()).setStyle("-fx-background-color: blue");
        });
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
        this.relocate(xCoordinate , yCoordinate);
        // this.relocate((xCoordinate - getWidth()) * .5 , (yCoordinate - getHeight()) * .5);
    }

    public String getLabel() {
        return label.getText();
    }

    public void setLabel(String text) {
        this.label.setText(text);
    }
}