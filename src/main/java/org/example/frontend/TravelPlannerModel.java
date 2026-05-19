package org.example.frontend;

import java.util.ArrayList;
import java.util.List;

public class TravelPlannerModel {

    private List<City> cities;

    public TravelPlannerModel(){
        cities = new ArrayList<>();
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

    public List<City> getCities() {
        return cities;
    }

    public String findFastestPath(){
        return "path";
    }
    public String findFastestPathBFS(){
        return "path";
    }

    public String findFastestPathDFS(){
        return "path";
    }

}
