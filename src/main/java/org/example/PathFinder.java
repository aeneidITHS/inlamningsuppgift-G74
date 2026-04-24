package org.example;

public interface PathFinder<T> extends Iterable<Edge<T>> {
    Path<T> findPath(Graph<T> graph, T from, T to);
}
