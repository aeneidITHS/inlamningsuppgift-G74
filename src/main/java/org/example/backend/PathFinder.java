package org.example.backend;

public interface PathFinder<T>{
    Path<T> findPath(Graph<T> graph, T from, T to);
}
