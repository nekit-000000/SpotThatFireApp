package com.example.a810200.spotthatfireapp.FireLoader.kmeans;

public class ForestFire {
    public double latitude;
    public double longitude;
    public double brightness;

    private int index = -1;

    public ForestFire() {
        latitude = 0;
        longitude = 0;
        brightness = 0;
    }

    public ForestFire(double newLatitude, double newLongitude, double newBrightness) {
        latitude = newLatitude;
        longitude = newLongitude;
        brightness = newBrightness;
    }

    public ForestFire(String newLatitude, String newLongitude, String newBrightness) {
        latitude = Double.parseDouble(newLatitude);
        longitude = Double.parseDouble(newLongitude);
        brightness = Double.parseDouble(newBrightness);
    }

    public double getSquareDistFromCurFire(ForestFire curFire) {
        double a = latitude - curFire.latitude;
        double b = longitude - curFire.longitude;

        return a * a + b * b;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
