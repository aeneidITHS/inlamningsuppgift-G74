package org.example.frontend;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;

public class CityNodeView extends Group {

    private City city;
    private Circle circle;

    public CityNodeView(City city){
        this.city = city;

        this.circle = new Circle(city.getX(),city.getY(), 12);
        circle.setFill(Color.ALICEBLUE);
        circle.setStroke(Color.BLACK);
        Label label = new Label(city.getName());
        label.setLocation(city.getX(),city.getY());
        getChildren().add(circle);
    }

    public Circle getCircle() {
        return circle;
    }
}
