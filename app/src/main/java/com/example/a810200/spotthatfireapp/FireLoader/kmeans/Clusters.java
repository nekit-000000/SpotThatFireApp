package com.example.a810200.spotthatfireapp.FireLoader.kmeans;

import java.util.*;
import java.util.List;

public class Clusters extends ArrayList<Cluster> {
    private final List<ForestFire> allPoints;
    private boolean isChanged;

    public Clusters(List<ForestFire> allPoints){
        this.allPoints = allPoints;
    }

    public Integer getNearestCluster(ForestFire point){
        double minSquareOfDistance = Double.MAX_VALUE;
        int itsIndex = -1;
        for (int i = 0 ; i < size(); i++){
            double squareOfDistance = point.getSquareDistFromCurFire(get(i).getCentroid());
            if (squareOfDistance < minSquareOfDistance){
                minSquareOfDistance = squareOfDistance;
                itsIndex = i;
            }
        }
        return itsIndex;
    }

    public boolean updateClusters(){
        for (Cluster cluster : this){
            cluster.updateCentroid();
            cluster.getPoints().clear();
        }
        isChanged = false;
        assignPointsToClusters();
        return isChanged;
    }

    public void assignPointsToClusters(){
        for (ForestFire point : allPoints){
            int previousIndex = point.getIndex();
            int newIndex = getNearestCluster(point);
            if (previousIndex != newIndex)
                isChanged = true;
            Cluster target = get(newIndex);
            point.setIndex(newIndex);
            target.getPoints().add(point);
        }
    }
}