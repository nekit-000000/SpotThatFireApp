package com.example.a810200.spotthatfireapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.a810200.spotthatfireapp.Finder.FinderPlaces;
import com.example.a810200.spotthatfireapp.Finder.GeomObject;

import gov.nasa.worldwind.Navigator;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Camera;
import gov.nasa.worldwind.geom.Location;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layer.BackgroundLayer;
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.shape.Placemark;
import gov.nasa.worldwind.shape.PlacemarkAttributes;


public class MainActivity extends AppCompatActivity {
    SearchView searchView;
    Placemark positionPlacemark = null;

    private static class SearchTask extends AsyncTask<String, Void, Void> {
        GeomObject go;

        @Override
        protected Void doInBackground (String ... params) {
            FinderPlaces finder = new FinderPlaces();
            go = finder.EqualsWith(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute (Void content) {
            if (go != null) {
                RenderTasks.getHandle().getNavigator().setLatitude(go.latitude);
                RenderTasks.getHandle().getNavigator().setLongitude(go.longitude);
                RenderTasks.getHandle().getNavigator().setAltitude((float) 1e4);
                PlacemarkKeeper.Update(RenderTasks.getHandle(), RenderTasks.getHandle().getNavigator().getAltitude());
                RenderTasks.getHandle().requestRedraw();
            }
        }
    }

    private  void InitSearvhView() {
        searchView = findViewById(R.id.search_view);
        searchView.setMaxWidth( Integer.MAX_VALUE );
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new SearchTask().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }


    private void InitButtons() {
        ImageButton plus = (ImageButton) findViewById(R.id.plus_button);
        ImageButton minus = (ImageButton) findViewById(R.id.minus_button);
        ImageButton myPosition = (ImageButton) findViewById(R.id.my_position_button);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                double navAltitude = RenderTasks.getHandle().getNavigator().getAltitude();

                RenderTasks.getHandle().getNavigator().setAltitude(navAltitude - navAltitude / 10);
                PlacemarkKeeper.Update(RenderTasks.getHandle(), RenderTasks.getHandle().getNavigator().getAltitude());
                RenderTasks.getHandle().requestRedraw();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                double navAltitude = RenderTasks.getHandle().getNavigator().getAltitude();

                RenderTasks.getHandle().getNavigator().setAltitude(navAltitude + navAltitude / 10);
                PlacemarkKeeper.Update(RenderTasks.getHandle(), RenderTasks.getHandle().getNavigator().getAltitude());
                RenderTasks.getHandle().requestRedraw();
            }
        });

        myPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (positionPlacemark != null) {
                    RenderTasks.getHandle().getNavigator().setLongitude(positionPlacemark.getPosition().longitude);
                    RenderTasks.getHandle().getNavigator().setLatitude(positionPlacemark.getPosition().latitude);
                    RenderTasks.getHandle().getNavigator().setAltitude(1e4);
                    RenderTasks.getHandle().requestRedraw();
                }
            }
        });
    }

    private void InitLocationManager() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                if (positionPlacemark == null) {
                    positionPlacemark = new Placemark( Position.fromDegrees(location.getLatitude(),
                            location.getLongitude(), 0), PlacemarkAttributes.
                            createWithImageAndLeader(ImageSource.fromResource(R.drawable.location)).
                            setImageScale(PlacemarkKeeper.CountPlacemarkScale(RenderTasks.getHandle().
                                    getNavigator().getAltitude())));

                    RenderableLayer posLayer = new RenderableLayer("MyPosition");
                    RenderTasks.getHandle().getLayers().addLayer(posLayer);

                    posLayer.addRenderable(positionPlacemark);

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        RenderTasks.InitHandle(getApplicationContext());

        RenderTasks.getHandle().getLayers().addLayer(new BackgroundLayer());
        RenderTasks.getHandle().getLayers().addLayer(new BlueMarbleLandsatLayer());

        PlacemarkKeeper.Init();
        PlacemarkKeeper.SetPlacemarksToHandler(RenderTasks.getHandle(), "Placemarks");

        FrameLayout globeLayout = findViewById(R.id.globe);
        globeLayout.addView(RenderTasks.getHandle());

        InitLocationManager();
        InitSearvhView();
        InitButtons();
    }
}
