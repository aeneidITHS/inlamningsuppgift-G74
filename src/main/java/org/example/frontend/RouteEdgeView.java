package org.example.frontend;

import org.example.backend.*;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class RouteEdgeView extends Group{
    public RouteEdgeView(Path<City> path) {
        for (int i = 0; i < path.getEdges().size(); i++) {
            Edge<City> edge = path.getEdges().get(i);
            City from = (i == 0) ? path.getStart() : path.getEdges().get(i - 1).getDestination();
            City to = edge.getDestination();
            Line line = new Line(from.x(), from.y(), to.x(), to.y());
            getChildren().add(line);
        }
    }
}
