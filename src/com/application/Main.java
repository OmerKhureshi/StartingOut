package com.application;

import com.application.fxgraph.ElementHelpers.ConvertDBtoElementTree;
import com.application.fxgraph.ElementHelpers.Element;
import com.application.fxgraph.graph.CellType;
import com.application.fxgraph.graph.Graph;
import com.application.fxgraph.graph.Model;
import com.application.logs.fileHandler.CallTraceLogFile;
import com.application.logs.fileIntegrity.CheckFileIntegrity;
import com.application.logs.parsers.ParseMethodDefinition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

//        addGraphComponents();
        addGraphCellComponents();
//        Layout layout = new RandomLayout(graph);
//        layout.execute();
    }

    private void addGraphCellComponents() {
        System.out.println("Starting out");
        // Check log file integrity.
        CheckFileIntegrity.checkFile(CallTraceLogFile.getFile());

        // Parse log file
            // insert into db
            // create elements
            // create circle cells and add to ui.

//        DatabaseUtil.dropCallTrace();
//        DatabaseUtil.dropMethodDefn();
//        new LoadLogFile().load(LoadLogFile.LogType.MethodDefn);
        List<List<String>> list = new ArrayList<>();
        final ConvertDBtoElementTree convertDBtoElementTree = new ConvertDBtoElementTree();
        new ParseMethodDefinition().readFile(CallTraceLogFile.getFile(),
//        new ParseMethodDefinition().readFile(MethodDefinitionLogFile.getFile(),
                brokenLineList -> {
                    list.add(brokenLineList);
//                    try {
////                        DatabaseUtil.insertMDStmt(brokenLineList);
//                    } catch (SQLException e) {  // Todo Create a custom exception class and clean this.
//                        e.printStackTrace();
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (InstantiationException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
                    convertDBtoElementTree.StringToElementList(brokenLineList);
                });
        convertDBtoElementTree.calculateElementProperties();
        Map<Integer, Element> threadMapToRoot = convertDBtoElementTree.getThreadMapToRoot();
        Model model = graph.getModel();
//        Iterate through the tree and create circle cells for each element found.
        threadMapToRoot.entrySet().stream()
                .map(entry -> entry.getValue())
                .forEachOrdered(root -> createCircleCellsRecursively(root, model));
//        convertDBtoElementTree.rootsList.stream()
//                .forEachOrdered(root -> createCircleCellsRecursively(root, model));

        graph.endUpdate();
    }

    private void createCircleCellsRecursively(Element root, Model model) {
        if (root == null) {
            return;
        }
        model.addCircleCell("Shit", root);
        System.out.println("Created new cell: " + root);

        if (root.getChildren() != null){
            root.getChildren().stream()
                    .forEachOrdered(ele -> createCircleCellsRecursively(ele, model));
        }
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