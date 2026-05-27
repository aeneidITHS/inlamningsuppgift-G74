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
import java.util.Optional;


public class TravelPlannerView extends BorderPane {


   private TravelPlannerModel model;
   private Pane mapPane;
   private Label statusLabel;
   private FileChooser fileChooser = new FileChooser();
   private boolean changed = false;
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


       FlowPane controls = new FlowPane();
       vbox.getChildren().add(controls);


       mapPane = new Pane();
       statusLabel = new Label("Travel Planner");


       setTop(vbox);
       setCenter(mapPane);
       setBottom(statusLabel);


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
               Alert alert = new Alert(
                       Alert.AlertType.CONFIRMATION);
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
           stage.fireEvent(new WindowEvent(
                   stage, WindowEvent.WINDOW_CLOSE_REQUEST));
       }
   }


   class ExitHandler implements EventHandler<WindowEvent> {
       public void handle(WindowEvent event) {
           if (changed) {
               Alert alert = new Alert(
                       Alert.AlertType.CONFIRMATION);
               alert.setContentText(
                       "Unsaved changes, exit anyway?");
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

           if (selected.size() !=2){
               Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: Select two cities");
               alert.showAndWait();
               return;
           }

           City from = selected.get(0).getCity();
           City to = selected.get(1).getCity();

           model.connectCities(from, to, 1, "Route");
           changed = true;
           statusLabel.setText("Connected ") +
                   from.getName() + " + " + to.getName());
       }
   }


}
