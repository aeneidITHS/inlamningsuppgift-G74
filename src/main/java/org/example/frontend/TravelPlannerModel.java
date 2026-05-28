package org.example.frontend;

import org.example.backend.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TravelPlannerModel {
    private final PathFinder<City> bfsPathFinder = new BFSPathFinder<>();
    private final PathFinder<City> dfsPathFinder = new DFSPathFinder<>();
    private final ListGraph<City> cities;
    private final List<Trip> tripHistory = new ArrayList<>();
    private final TravelFileManager travelFileManager;
    private List<Path> paths = new ArrayList<>();
    private String imagePath;
    public TravelPlannerModel(){
        cities = new ListGraph<>();
        createBasicCityList();
        this.travelFileManager = new TravelFileManager(this);

    }


    private void createBasicCityList(){
        cities.add(new City("Stockholm",0,250,150));
        cities.add(new City("London",1,200,100));
        cities.add(new City("Berlin",2,350,140));
        cities.add(new City("Helsinki",3,400,120));
        cities.add(new City("Rome",4,130,170));
        cities.add(new City("Paris",5,180,80));
    }

    public Set<City> getCities() {
        return cities.getNodes();
    }

    public City getCity(City city){
        for (City city1 : cities){
            if(city1.equals(city)){
                return city1;
            }
        }
        return null;
    }

    public City getCityByName(String name){
        for(City city: cities){
            if(city.name().equalsIgnoreCase(name)){
                return city;
            }
        }
        return null;
    }
    public List<Trip> getTripHistory(){
        return tripHistory;
    }
    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }
    public String loadImageRefence(File file) throws IOException {
        return travelFileManager.loadImageReference(file);
    }

    public void saveImageReference(String imagePath,File file) throws IOException {
        travelFileManager.saveImageReference(imagePath,file);
    }

    public boolean removeCity(City city){
        if(cities.hasNode(city)){
            cities.remove(city);
            return true;
        }
        else {
            System.out.println("That city does not exist!");
            return false;
        }

    }
    public void removeAllCities(){
        for (City city: new ArrayList<>(cities.getNodes())){
            cities.remove(city);
        }
    }

    public boolean addCities(City city){
        if(cities.hasNode(city)){
            System.out.println("The city already exists!");
            return  false;
        }
        else {
            cities.add(city);
            return true;
        }
    }

    public void connectCities(City from, City to, int weight, String connectionName){
        if(graphContainsNodes(from,to)){
            cities.connect(from,to,connectionName,weight);
        }
    }

    public boolean disconnectCities(City from, City to){
        if(graphContainsNodes(from,to)){
            cities.disconnect(from,to);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean changeConnectionWeight(City from, City to, int weight){
        if(graphContainsNodes(from,to) && weight > 0){
            cities.setConnectionWeight(from,to,weight);
            return true;
        }
        else {
            return false;
        }
    }

    public Set<String> getConnections(){
        Set<String> connectionLines = new HashSet<>();

        for(City from : cities){
            for(Edge<City> edge: cities.getEdgesFrom(from)){
                City to = edge.getDestination();


                String edgeKey1 = "EDGE;" +  from.name() + "-" + to.name() + ";";
                String edgeKey2 = "EDGE;" + to.name() + "-" + from.name() + ";";

                if (!connectionLines.contains(edgeKey1) && !connectionLines.contains(edgeKey2)) {
                    connectionLines.add("EDGE;" +
                            from.name() + "-" +
                            to.name()  + ";" +
                            to.name() + "-" +
                            from.name() + ";" +
                            cities.getEdgeBetween(from,to).getName() + ";" +
                            edge.getWeight());
                }
            }
        }
        return connectionLines;
    }

    public ArrayList<double[]> getPathCoordinates(Path<City> path){
        ArrayList<double[]> coordinates = new ArrayList<>();

        for(int i = 0; i< path.getEdges().size();i++){
            Edge<City> edge = path.getEdges().get(i);
            City from =(i==0)? path.getStart(): path.getEdges().get(i-1).getDestination();
            City to = edge.getDestination();

            coordinates.add(new double[]{from.x(),from.y(), to.x(), to.y()});
        }
        return coordinates;
    }
    public Path<City> findPath(City from, City to, String algorithm) {
        PathFinder<City> pathFinder;

        if (algorithm.equals("BFS")) {
            return findPathBFS(from,to);
        }
        else if(algorithm.equals("DFS")){
            return findPathDFS(from,to);
        }
        else{
            throw new IllegalArgumentException("Unknown algorithm" + algorithm);
        }

    }

    public Path<City> findPathBFS(City from, City to){
        Path<City> fastestPath = bfsPathFinder.findPath(cities,from,to);
        if(fastestPath == null){
            return null;
        }

        Trip trip = new Trip(
                from.name()
                ,to.name()
                , "BFS"
                ,convertPathToCityNames(fastestPath)
                , fastestPath.getTotalWeight());

        tripHistory.add(trip);
        return fastestPath;
    }
    public Path<City> findPathDFS(City from, City to){
        Path<City> fastestPath = dfsPathFinder.findPath(cities,from,to);
        if(fastestPath == null){
            return null;
        }

        Trip trip = new Trip(
                from.name()
                ,to.name()
                , "DFS"
                ,convertPathToCityNames(fastestPath)
                , fastestPath.getTotalWeight());
        tripHistory.add(trip);
        return fastestPath;
    }

    public void saveGraph(File fileName){
        travelFileManager.saveGraph(fileName);
    }

    public void loadGraph(File fileName) throws FileNotFoundException {
        travelFileManager.loadGraph(fileName);
        System.out.println("Hej3");
        System.out.println(getCities().toString());

    }

    public boolean loadSavedTrips(File file) throws IOException {
        List<Trip> loadedTrips =  travelFileManager.loadTrips(file);
        return tripHistory.addAll(loadedTrips);

    }
   /* public boolean saveTrip(String filename,Trip trip) throws IOException {
        File file = new File(filename);
        for(Trip trips: tripHistory){
            if(trips.equals(trip)){
                travelFileManager.saveTrip(trip,file);
                return true;
            }
        }
        return false;
    }
    public void saveTrips(File file) throws IOException {
       travelFileManager.saveTrips(tripHistory,file);
    }*/



    private List<String> convertPathToCityNames(Path<City> path){
        List<String> cityNames = new ArrayList<>();

        for(City city : path.getNodes()){
            cityNames.add(city.name());
        }
        return cityNames;
    }

    private boolean graphContainsNodes(City node1, City node2){
        return cities.hasNode(node1) && cities.hasNode(node2);
    }



}
