package org.example.frontend;

import org.example.backend.*;

import java.util.List;
import java.util.Set;

public class TravelPlannerModel {
    private PathFinder<City> bfsPathFinder = new BFSPathFinder<>();
    private PathFinder<City> dfsPathFinder = new DFSPathFinder<>();
    private ListGraph<City> cities;
    private Graph<City> graph;
    private Path<City> path;

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

    public void addCities(City city){
        cities.add(city);
    }

    public Set<City> getCities() {
        return cities.getNodes();
    }

    public String findPathBFS(City from, City to){
        Path<City> fastestPath = bfsPathFinder.findPath(cities,from,to);
        if(fastestPath == null){
            return "No path found";
        }
        return fastestPath.toString();
    }
    public String findPathDFS(City from, City to){
        Path<City> fastestPath = dfsPathFinder.findPath(cities,from,to);
        if(fastestPath == null){
            return "No path found";
        }

        return fastestPath.toString();
    }



}
