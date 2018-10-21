package com.example.a810200.spotthatfireapp.Finder;

public class GeomObject {
    public String cityName;
    public String countryName;
    public double latitude;
    public double longitude;

    GeomObject(String city, String country, double latit, double longit) {
        cityName = city;
        countryName = country;
        latitude = latit;
        longitude = longit;
    }
}
