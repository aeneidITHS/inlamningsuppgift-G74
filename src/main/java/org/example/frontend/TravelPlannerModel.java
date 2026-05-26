package org.example.frontend;

import org.example.backend.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TravelPlannerModel {
    private final PathFinder<City> bfsPathFinder = new BFSPathFinder<>();
    private final PathFinder<City> dfsPathFinder = new DFSPathFinder<>();
    private final ListGraph<City> cities;
    private final List<Trip> tripHistory = new ArrayList<>();
    private final TravelFileManager travelFileManager = new TravelFileManager();
    Graph graph;
    Path path;
    public TravelPlannerModel(){
        cities = new ListGraph<>();
        createBasicCityList();

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

    public boolean removeCities(City city){
        if(cities.hasNode(city)){
            cities.remove(city);
            return true;
        }
        else {
            System.out.println("That city does not exist!");
            return false;
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

    public boolean connectCities(City from, City to,int weight, String connectionName){
        if(graphContainsNodes(from,to)){
            cities.connect(from,to,connectionName,weight);
            return true;
        }
        else {
            return false;
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
                from.getName()
                ,to.getName()
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
                from.getName()
                ,to.getName()
                , "DFS"
                ,convertPathToCityNames(fastestPath)
                , fastestPath.getTotalWeight());
        tripHistory.add(trip);
        return fastestPath;
    }

    public boolean loadSavedTrips(File file) throws IOException {
        List<Trip> loadedTrips =  travelFileManager.loadTrips(file);
        return tripHistory.addAll(loadedTrips);

    }
    public boolean saveTrip(String filename,Trip trip) throws IOException {
        File file = new File(filename);
        for(Trip trips: tripHistory){
            if(trips.equals(trip)){
                travelFileManager.saveTrip(trip,file);
                return true;
            }
        }
        return false;
    }



    private List<String> convertPathToCityNames(Path<City> path){
        List<String> cityNames = new ArrayList<>();

        for(City city : path.getNodes()){
            cityNames.add(city.getName());
        }
        return cityNames;
    }

    private boolean graphContainsNodes(City node1, City node2){
        return cities.hasNode(node1) && cities.hasNode(node2);
    }



}
