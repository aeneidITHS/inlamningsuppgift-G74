package org.example.frontend;
import org.example.backend.*;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class RouteEdgeView extends Group{
    public RouteEdgeView(Path<City> path) {
        for (int i = 0; i < path.getEdges().size(); i++) {
            Edge<City> edge = path.getEdges().get(i);
            City from = edge.getFrom();
            City to = edge.getDestination();
            Line line = new Line(from.getX(), from.getY(), to.getX(), to.getY());
            getChildren().add(line);
        }
    }
}
