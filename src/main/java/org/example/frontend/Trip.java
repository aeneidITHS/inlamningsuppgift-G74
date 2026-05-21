package org.example.frontend;

import java.time.LocalDateTime;
import java.util.List;

public class Trip {
    private final String from;
    private final String to;
    private final String algorithm;
    private final List<String> path;
    private final int totalWeight;
    private final LocalDateTime createdAt;

    public Trip(String from, String to, String algorithm, List<String> path, int totalWeight) {
        this(from, to, algorithm, path, totalWeight, LocalDateTime.now());
    }


    public Trip(String from, String to,String algorithm, List<String> path, int totalweight, LocalDateTime createdAt) {
        this.from = from;
        this.to = to;
        this.algorithm = algorithm;
        this.path = path;
        this.totalWeight = totalweight;
        this.createdAt = createdAt;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public List<String> getPath() {
        return path;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public String toFileString() {
        return algorithm + ";" +
                from + ";" +
                to + ";" +
                path + ";" +
                totalWeight + ";" +
                createdAt;
    }

    @Override
    public String toString() {
        return  "<Trip/>"
                +"\t<algorithm/>" + algorithm + "</algorithm>"
                + "\t<from/>" + from + "</from>"
                + "\t<to/>" + to + "</from>"
                + "\t<Path/>" + path.toString() + "</Path>"
                + "\t<created at/>" + createdAt + "</created at>" +  ";";
    }
}
