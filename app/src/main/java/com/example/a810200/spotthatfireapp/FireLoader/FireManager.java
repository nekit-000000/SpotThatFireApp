package com.example.a810200.spotthatfireapp.FireLoader;

import com.example.a810200.spotthatfireapp.FireLoader.kmeans.ForestFire;
import com.example.a810200.spotthatfireapp.FireLoader.kmeans.KMeans;

import java.util.ArrayList;
import java.util.List;

public class FireManager {
    final static private String URL = "https://firms.modaps.eosdis.nasa.gov/data/active_fire/c6/csv/MODIS_C6_Global_24h.csv";
    private int nMipMaps;

    private List<ForestFire>[] allFireData;
    private double[] distList;

    public List<ForestFire> GetForestList(double curDist) {
        int numMM = 0;
        for (; numMM < nMipMaps; numMM++) {
            if (curDist < distList[numMM]) {
                break;
            }
        }
        numMM = Math.min(numMM, nMipMaps - 1);

        return allFireData[numMM];
    }

    public String InitManager(double maxDist, double minDist, int numOfMipMaps) {
        allFireData = (ArrayList<ForestFire>[]) new ArrayList[numOfMipMaps];
        distList = new double[numOfMipMaps];

        allFireData[0] = new ArrayList<>();
        String err = LoadFireData();
        if (err != null)
            return err;

        nMipMaps = numOfMipMaps;
        distList[0] = minDist;
        for (int i = 1; i < numOfMipMaps; i++)
            distList[i] = distList[i - 1] + (maxDist - minDist) / (nMipMaps - 1);

        if (numOfMipMaps < 2)
            throw new IllegalArgumentException("num of mip maps must be >= 2");

        KMeans km;

        for (int i = 1; i < numOfMipMaps; i++) {
            int numOfClasters = (int) (allFireData[0].size() / (6 * Math.pow(4, (i - 1))));
            km = new KMeans(allFireData[0], numOfClasters);
            allFireData[i] = km.getPointsClusters();
        }
        return null;
    }

    // возвращает null, если лоад прошел успешно и error string в другом случае
    private String LoadFireData() {
        Loader loader = new Loader();

        String[] content = loader.getFireInfo(URL);

        if (content.length == 1)
            return content[0];

        for (int i = 1; i < content.length; i++) {
            try {
                String[] data = content[i].split(",");
                allFireData[0].add(new ForestFire(data[0], data[1], data[2]));
            }
            catch (Exception ex) {
                return "Error format of file, parser cant parse that! " + ex.getMessage();
            }
        }

        return null;
    }


}
