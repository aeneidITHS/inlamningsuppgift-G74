package org.example.frontend;

import java.time.LocalDateTime;
import java.util.List;

public class Trip {
    private final City from;
    private final City to;
    private final String algorithm;
    private final List<City> path;
    private final LocalDateTime createdAt;

    public Trip(City from, City city,String algorithm, List<City> path) {
        this.from = from;
        this.to = city;
        this.algorithm = algorithm;
        this.path = path;
        this.createdAt = LocalDateTime.now();
    }

    public City getFrom() {
        return from;
    }

    public City getTo() {
        return to;
    }

    public List<City> getPath() {
        return path;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return algorithm + " trip from " + from + " to " + to +
                "\nPath: " + path +
                "\nCreated at: " + createdAt;
    }
}
