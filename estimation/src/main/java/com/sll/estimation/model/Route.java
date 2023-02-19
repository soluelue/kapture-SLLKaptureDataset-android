package com.sll.estimation.model;

import java.util.ArrayList;

public class Route {

    public class Coordinate{

        private long timestamp;
        private float x;
        private float y;
        private float z;

        public Coordinate(long timestamp, float x, float y, float z){
            this.timestamp = timestamp;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return timestamp
                    + "," +
                    x
                    + "," +
                    y
                    + "," +
                    z;
        }
    }

    private ArrayList<Coordinate> routes;

    public Route(){
        this.routes = new ArrayList<>();
    }

    public void addCoordinate(long timestamp, float x, float y, float z){
        routes.add(new Coordinate(timestamp, x,y,z));
    }

    public ArrayList<Coordinate> getRoute(){
        return routes;
    }


    @Override
    public String toString() {
        return "Route{" +
                "routes=" + routes.toString() +
                '}';
    }
}
