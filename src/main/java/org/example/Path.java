package org.example;

import java.util.List;

public interface Path<T> extends Iterable<Edge> {
    T getStart();
    T getEnd();
    int getTotalWeight();
    List<Edge<T>> getEdges();
    List<T> getNodes();
}
