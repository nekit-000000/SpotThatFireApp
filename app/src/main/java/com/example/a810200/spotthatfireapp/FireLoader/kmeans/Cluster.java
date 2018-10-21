package com.example.a810200.spotthatfireapp.FireLoader.kmeans;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private final List<ForestFire> points;
    private ForestFire centroid;

    public Cluster(ForestFire firstPoint) {
        points = new ArrayList<>();
        centroid = firstPoint;
    }

    public ForestFire getCentroid(){
        return centroid;
    }

    public void updateCentroid(){
        double newx = 0d, newy = 0d;
        for (ForestFire point : points){
            newx += point.latitude;
            newy += point.longitude;
        }
        centroid = new ForestFire(newx / points.size(), newy / points.size(), 0);
    }

    public List<ForestFire> getPoints() {
        return points;
    }
}
