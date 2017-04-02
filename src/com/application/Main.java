package com.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.application.fxgraph.graph.CellType;
import com.application.fxgraph.graph.Graph;
import com.application.fxgraph.graph.Model;
import com.application.fxgraph.layout.base.Layout;
import com.application.fxgraph.layout.random.RandomLayout;

public class Main extends Application {

    // Part of code from http://stackoverflow.com/a/30696075/3690248
    Graph graph = new Graph();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        graph = new Graph();

        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

        Layout layout = new RandomLayout(graph);
        layout.execute();

    }

    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

//        model.addCell("Cell A", CellType.RECTANGLE);
//        model.addCell("Cell B", CellType.RECTANGLE);
//        model.addCell("Cell C", CellType.RECTANGLE);
//        model.addCell("Cell D", CellType.TRIANGLE);
//        model.addCell("Cell E", CellType.TRIANGLE);
//        model.addCell("Cell F", CellType.RECTANGLE);
//        model.addCell("Cell G", CellType.RECTANGLE);
        model.addCell("Cell A", CellType.RECTANGLE);
        model.addCell("Cell B", CellType.RECTANGLE);
        model.addCell("Cell C", CellType.RECTANGLE);
        model.addCell("Cell D", CellType.RECTANGLE);
        model.addCell("Cell E", CellType.RECTANGLE);
        model.addCell("Cell F", CellType.RECTANGLE);
        model.addCell("Cell G", CellType.RECTANGLE);
        model.addCell("Cell H", CellType.RECTANGLE);
        model.addCell("Cell I", CellType.RECTANGLE);
        model.addCell("Cell J", CellType.RECTANGLE);
        model.addCell("Cell K", CellType.RECTANGLE);

        model.addEdge("Cell A", "Cell B");
        model.addEdge("Cell A", "Cell C");
//        model.addEdge("Cell B", "Cell C");
        model.addEdge("Cell C", "Cell D");
        model.addEdge("Cell B", "Cell E");
        model.addEdge("Cell D", "Cell F");
        model.addEdge("Cell D", "Cell G");
        model.addEdge("Cell G", "Cell H");
        model.addEdge("Cell G", "Cell I");
        model.addEdge("Cell G", "Cell J");
        model.addEdge("Cell G", "Cell K");

        graph.endUpdate();
    }

    public static void main(String[] args) {
        launch(args);
    }
}