package com.application.fxgraph.cells;

import com.application.fxgraph.ElementHelpers.Element;
import com.application.fxgraph.graph.Cell;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleCell extends Cell {

    Label label;

    public CircleCell(String id) {
        super( id);
        // setStyle("-fx-background-color: yellow");
       //  Group group = new Group();
        Circle circle = new Circle(20);
        label = new Label();

        // group.getChildren().addAll(circle, label);
        circle.setStroke(Color.RED);
        circle.setFill(Color.RED);
        getChildren().setAll(circle, label);
        label.setTranslateY(circle.getRadius() + 5);
        // setView(group);
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