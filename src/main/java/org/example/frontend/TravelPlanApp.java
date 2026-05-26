package org.example.frontend;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class TravelPlanApp extends  Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TravelPlannerModel model = new TravelPlannerModel();
        TravelPlannerView view = new TravelPlannerView(model,stage);
        view.getChildren();
        Scene scene = new Scene(view, 900, 900);

        stage.setTitle("TravelPlanner");
        stage.setScene(scene);
        stage.show();
    }
}
