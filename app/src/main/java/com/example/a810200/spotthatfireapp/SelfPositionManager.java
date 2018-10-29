package com.example.a810200.spotthatfireapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.shape.Placemark;
import gov.nasa.worldwind.shape.PlacemarkAttributes;


public class SelfPositionManager {
    static private Placemark positionPlacemark = null;

    public static Placemark GetPositionPlacemark() { return positionPlacemark; }

    public static void Init (MainActivity activity) {
        if (positionPlacemark != null) {
            return;
        }

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                if (positionPlacemark == null) {
                    return;
                }

                Position pos = new Position();
                pos.latitude = location.getLatitude();
                pos.longitude = location.getLongitude();
                positionPlacemark.setPosition(pos);

                RenderTasks.getHandle().requestRedraw();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            android.location.Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            positionPlacemark = new Placemark( Position.fromDegrees(location.getLatitude(),
                    location.getLongitude(), 0), PlacemarkAttributes.
                    createWithImageAndLeader(ImageSource.fromResource(R.drawable.location)).
                    setImageScale(PlacemarkKeeper.CountPlacemarkScale(RenderTasks.getHandle().
                            getNavigator().getAltitude())));

            RenderableLayer posLayer = new RenderableLayer("MyPosition");
            RenderTasks.getHandle().getLayers().addLayer(posLayer);

            posLayer.addRenderable(positionPlacemark);
        }
    }
}
