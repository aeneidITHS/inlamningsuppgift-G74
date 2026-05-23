package org.example.frontend;

import org.example.backend.ListGraph;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRenderedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class TravelFileManager {

    public void savePicture(BufferedImage image,File file) throws IOException{
        ImageIO.write(image,"jpg",file);

    }

    public BufferedImage loadPictures(File file) throws  IOException{
        return ImageIO.read(file);
    }


    public void saveTrip(Trip trip, File filename) throws IOException {
        FileWriter writer = new FileWriter(filename, true);
        writer.write(trip.toFileString() + "\n");
        writer.close();

    }
    public List<Trip> loadTrips(File file) throws IOException {
        List<Trip> trips = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isBlank()) {
                Trip trip = loadTripFromLine(line);
                trips.add(trip);
            }
        }
        scanner.close();
        return trips;
    }

    private Trip loadTripFromLine(String line) throws IOException {
        String[] parts = line.split(";");

        String algorithm = parts[0];
        String from = parts[1];
        String to = parts[2];
        List<String> path = Arrays.asList(parts[3].split(","));
        int totalWeight = Integer.parseInt(parts[4]);
        LocalDateTime createdAt = LocalDateTime.parse(parts[5]);

        return new Trip(from,to,algorithm,path,totalWeight,createdAt);


    }
}
