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
    TravelPlannerModel model;

    public TravelFileManager(TravelPlannerModel model){
        this.model = model;

    }

    public void saveImageReference(String imagePath,File fileName) throws IOException{
        if(imagePath == null || imagePath.isBlank()){
            throw new IOException("There is no image reference to save!");
        }
        FileWriter writer = new FileWriter(fileName);
        writer.write(imagePath);
        writer.close();
    }

    public String loadImageReference(File fileName) throws IOException {
       try(Scanner scanner = new Scanner(fileName)){
        if(scanner.hasNextLine()){
            return scanner.nextLine();
        }
    }
           throw new IOException("The image reference does not exist");
       }

    public void saveGraph(File fileName){
        try(FileWriter fileWriter = new FileWriter(fileName, false)){
            if(model.getImagePath() != null){
                fileWriter.write("IMAGE;" + model.getImagePath()+ "\n");
            }
            for(City city : model.getCities()){
                fileWriter.write("CITY;" +
                        city.name() + ";" +
                        city.id() + ";" +
                        city.x() + ";" +
                        city.y() + "\n"
                );
            }
            for(String connectionLine : model.getConnections()){
                fileWriter.write(connectionLine);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGraph(File fileName) throws FileNotFoundException {
        model.removeAllCities();

        Set<String> connections = new HashSet<>();
        System.out.println("Before Scanner loop");

        try(Scanner scanner = new Scanner(fileName)){
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                System.out.println("Inside scanner loop");
                System.out.println(line);
                if(line.isBlank()){
                    continue;
                }
                String[] parts = line.split(";");

                switch (parts[0]) {
                    case "IMAGE" -> {
                        model.setImagePath(parts[1]);
                        System.out.println("Image");
                    }
                    case "CITY" -> {
                        String name = parts[1];
                        int id = Integer.parseInt(parts[2]);
                        int x = Integer.parseInt(parts[3]);
                        int y = Integer.parseInt(parts[4]);
                        System.out.println(name);
                        System.out.println(id);
                        System.out.println(x);
                        System.out.println(y);
                        model.addCities(new City(name, id, x, y));
                    }
                    case "EDGE" -> {
                        connections.add(line);
                        System.out.println("edge");
                    }
                }
            }
        }
        for(String connection: connections){
            String[] parts = connection.split(";");
            System.out.println(Arrays.toString(parts));
            System.out.println(parts[0]);
            System.out.println(parts[1]);
            System.out.println(parts[2]);
            System.out.println(parts[3]);
            City from = model.getCityByName(parts[1]);
            City to = model.getCityByName(parts[2]);
            String name = parts[3];
            int totalWeight = Integer.parseInt(parts[4]);
            if (from != null && to != null){
                model.connectCities(from,to,totalWeight,name);
            }
        }
    }
}
