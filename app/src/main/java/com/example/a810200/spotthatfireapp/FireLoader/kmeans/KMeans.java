package com.example.a810200.spotthatfireapp.FireLoader.kmeans;

import java.util.*;

public class KMeans {
    private static final Random random = new Random();

    public final List<ForestFire> allPoints;
    public final int k;
    private Clusters pointClusters;

    public KMeans(List<ForestFire> pointsFire, int k) {
        if (k < 2)
            new Exception("The value of k should be 2 or more.").printStackTrace();
        this.k = k;
        this.allPoints = pointsFire;
    }

    private void getInitialClusters() {
        pointClusters.assignPointsToClusters();
    }

    private void updateClustersUntilNoChange(){
        boolean isChanged = pointClusters.updateClusters();
        while (isChanged)
            isChanged = pointClusters.updateClusters();
    }

    private void getInitialKRandomSeeds(){
        pointClusters = new Clusters(allPoints);
        List<ForestFire> kRandomPoints = getKRandomPoints();
        for (int i = 0; i < k; i++){
            kRandomPoints.get(i).setIndex(i);
            pointClusters.add(new Cluster(kRandomPoints.get(i)));
        }
    }

    private List<ForestFire> getKRandomPoints() {
        List<ForestFire> kRandomPoints = new ArrayList<>();
        boolean[] alreadyChosen = new boolean[allPoints.size()];
        int size = allPoints.size();
        for (int i = 0; i < k; i++) {
            int index = -1, r = random.nextInt(size--) + 1;
            for (int j = 0; j < r; j++) {
                index++;
                while (alreadyChosen[index])
                    index++;
            }
            kRandomPoints.add(allPoints.get(index));
            alreadyChosen[index] = true;
        }
        return kRandomPoints;
    }

    public List<ForestFire> getPointsClusters() {
        if (pointClusters == null) {
            getInitialKRandomSeeds();
            getInitialClusters();
            updateClustersUntilNoChange();
        }

        List<ForestFire> res = new ArrayList<>();

        for (int i = 0; i < pointClusters.size(); i++)
            res.add(pointClusters.get(i).getCentroid());
        return res;
    }
}
