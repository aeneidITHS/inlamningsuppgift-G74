package org.example.frontend;

import org.example.backend.ListGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TravelFileManager {





    public void saveTrip(ListGraph<City> cities , File filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(cities.toString());
        writer.close();
    }

}
