package com.example.a810200.spotthatfireapp;


import com.example.a810200.spotthatfireapp.FireLoader.kmeans.ForestFire;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layer.LayerList;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.shape.Placemark;
import gov.nasa.worldwind.shape.PlacemarkAttributes;


class PlacemarkKeeper {
    public static ArrayList<Placemark> placemarks = new ArrayList();
    private static PlacemarkAttributes largeFireAttributes =  PlacemarkAttributes.
            createWithImageAndLeader(ImageSource.fromResource(R.drawable.fire));
    private static String placemarksLayerName;


    public static void Init() {
        for (ForestFire i : FireManagerHolder.fm.GetForestList(RenderTasks.getHandle().getNavigator().getAltitude())) {
            Placemark wildfire = new Placemark( Position.fromDegrees(i.latitude, i.longitude, 0),
                    PlacemarkKeeper.GetLargeFireAttributes().setImageScale(PlacemarkKeeper.CountPlacemarkScale(RenderTasks.getHandle().getNavigator().getAltitude())));
            PlacemarkKeeper.placemarks.add(wildfire);
        }
    }


    public static double CountPlacemarkScale (double cameraDist) {
        return Math.sqrt(cameraDist) / 1e3;
    }


    public static PlacemarkAttributes GetLargeFireAttributes() {
        return largeFireAttributes;
    }

    public static  void SetPlacemarksToHandler (PlacemarksHandler handler, String layerName) {
        RenderableLayer placemarksLayer = new RenderableLayer(layerName);
        handler.getLayers().addLayer(placemarksLayer);

        placemarksLayerName = layerName;

        for (Placemark i : placemarks) {
            placemarksLayer.addRenderable(i);
        }
    }


    public static void Update (PlacemarksHandler handler, double cameraDistance) {
        List<ForestFire> newPlacemarksPositions = FireManagerHolder.fm.GetForestList(cameraDistance);

        if (placemarks.size() != newPlacemarksPositions.size()) {
            LayerList layers = handler.getLayers();
            layers.removeLayer(layers.indexOfLayerNamed(placemarksLayerName));

            placemarks.clear();

            for (ForestFire i : newPlacemarksPositions) {
                Placemark wildfire = new Placemark( Position.fromDegrees(i.latitude, i.longitude, 0),
                        PlacemarkKeeper.GetLargeFireAttributes().setImageScale(CountPlacemarkScale(cameraDistance)));
                PlacemarkKeeper.placemarks.add(wildfire);
            }

            SetPlacemarksToHandler(handler, placemarksLayerName);
        }
    }
}
