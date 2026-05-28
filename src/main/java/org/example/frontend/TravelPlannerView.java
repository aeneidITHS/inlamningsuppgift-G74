package org.example.frontend;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.geometry.Pos;


public class TravelPlannerView extends BorderPane {


    private TravelPlannerModel model;
    private Pane mapPane;
    private Label statusLabel;
    private TextArea display = new TextArea();
    private FileChooser fileChooser = new FileChooser();
    private boolean changed = false;
    private boolean useBFS = true;
    private Stage stage;


    public TravelPlannerView(TravelPlannerModel model, Stage stage) {
        this.model = model;
        this.stage = stage;


        VBox vbox = new VBox();
        MenuBar menuBar = new MenuBar();
        vbox.getChildren().add(menuBar);


        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);


        MenuItem newItem = new MenuItem("New");
        fileMenu.getItems().add(newItem);
        newItem.setOnAction(new NewHandler());


        MenuItem openItem = new MenuItem("Open");
        fileMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());


        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SaveHandler());


        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);
        exitItem.setOnAction(new ExitItemHandler());

        Menu algorithmMenu = new Menu("Algorithm");
        menuBar.getMenus().add(algorithmMenu);

        MenuItem bfsItem = new MenuItem("BFS");
        algorithmMenu.getItems().add(bfsItem);
        bfsItem.setOnAction(new BFSHandler());

        MenuItem dfsItem = new MenuItem("DFS");
        algorithmMenu.getItems().add(dfsItem);
        dfsItem.setOnAction(new DFSHandler());

        FlowPane controls = new FlowPane();
        controls.setAlignment(Pos.CENTER);
        controls.setHgap(5);

        Button addCityButton = new Button("Add City");
        Button findPathButton = new Button("Find Path");
        Button connectCitiesButton = new Button("Connect Cities");

        addCityButton.setOnAction(new AddCityHandler());
        findPathButton.setOnAction(new FindPathHandler());
        connectCitiesButton.setOnAction(new ConnectCitiesHandler());

        controls.getChildren().addAll(addCityButton, findPathButton, connectCitiesButton);

        vbox.getChildren().add(controls);


        display.setWrapText(true);
        display.setEditable(false);


        mapPane = new Pane();
        statusLabel = new Label("Travel Planner");


        setTop(vbox);
        setCenter(mapPane);
        setBottom(statusLabel);
        setRight(display);


        for (City city : model.getCities()) {
            addCityToMap(city);
        }


        stage.setOnCloseRequest(new ExitHandler());
    }


    public void addCityToMap(City city) {
        CityNodeView cityNode = new CityNodeView(city);
        mapPane.getChildren().add(cityNode);
    }


    class NewHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            if (changed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText(
                        "Unsaved changes, continue anyway?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() &&
                        res.get().equals(ButtonType.OK)) {
                    mapPane.getChildren().clear();
                    changed = false;
                    statusLabel.setText("New map created");
                }
            } else {
                mapPane.getChildren().clear();
                statusLabel.setText("New map created");
            }
        }
    }


    class OpenHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                statusLabel.setText("Opened: " + file.getName());
                changed = false;
            }
        }
    }


    class SaveHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                statusLabel.setText("Saved: " + file.getName());
                changed = false;
            }
        }
    }


    class ExitItemHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    class BFSHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
         useBFS = true;
         statusLabel.setText(" BFS ");
        }
    }

    class DFSHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            useBFS = false;
            statusLabel.setText(" DFS ");
        }
    }


    class ExitHandler implements EventHandler<WindowEvent> {
        public void handle(WindowEvent event) {
            if (changed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Unsaved changes, exit anyway?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() &&
                        res.get().equals(ButtonType.CANCEL)) {
                    event.consume();
                }
            }
        }
    }

    class ConnectCitiesHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {

            List<CityNodeView> selected = new ArrayList<>();
            for (javafx.scene.Node node : mapPane.getChildren()) {
                if (node instanceof CityNodeView) {
                    CityNodeView cityNode = (CityNodeView) node;
                    if (cityNode.isSelected()) {
                        selected.add(cityNode);
                    }
                }
            }

            if (selected.size() != 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: Select two cities");
                alert.showAndWait();
                return;
            }

            City from = selected.get(0).getCity();
            City to = selected.get(1).getCity();

            model.connectCities(from, to, 1, "Route");
            changed = true;
            statusLabel.setText("Connected " + from.name() + " + " + to.name());
        }
    }


    class FindPathHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            List<CityNodeView> selected = new ArrayList<>();
            for (javafx.scene.Node node : mapPane.getChildren()) {
                if (node instanceof CityNodeView) {
                    CityNodeView cityNode = (CityNodeView) node;
                    if (cityNode.isSelected()) {
                        selected.add(cityNode);
                    }
                }
            }

            if (selected.size() != 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Select two cities");
                alert.showAndWait();
                return;
            }

            City from = selected.get(0).getCity();
            City to = selected.get(1).getCity();

            Path<City> path;
            if (useBFS) {
                path = model.findPathBFS(from, to);
            } else {
                path = model.findPathDFS(from, to);
            }

            if (path == null) {
                display.setText("No path found");
            } else {
                display.setText(path.toString());
            }


        }
    }


}

