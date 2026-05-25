package org.example.frontend;


public class City {
   private String name;
   private int id;
   private int x;
   private int y;




   public City(String name, int id, int x, int y){
       this.name = name;
       this.id = id;
       this.x = x;
       this.y = y;


   }
   public String getName() {
       return name;
   }


   public int getId() {
       return id;
   }


   public int getX() {
       return x;
   }


   public int getY() {
       return y;
   }


   @Override
   public boolean equals(Object obj) {
       if (obj instanceof City city) {
           return name.equals(city.name);
       }
       return false;
   }


   @Override
   public int hashCode() {
       return name.hashCode;
   }


   @Override
   public String toString() {
       return name;
   }
}

