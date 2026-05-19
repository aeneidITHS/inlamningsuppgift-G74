package org.example.backend;

public interface Edge<T>{
    T getDestination();
    int getWeight();
    void setWeight(int weight);
    String getName();
    String toString();
}
