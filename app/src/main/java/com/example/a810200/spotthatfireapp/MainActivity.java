package com.example.a810200.spotthatfireapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.a810200.spotthatfireapp.Finder.FinderPlaces;
import com.example.a810200.spotthatfireapp.Finder.GeomObject;

import gov.nasa.worldwind.layer.BackgroundLayer;
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer;


public class MainActivity extends AppCompatActivity {
    SearchView searchView;

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
        ImageButton plus = findViewById(R.id.plus_button);
        ImageButton minus = findViewById(R.id.minus_button);
        ImageButton myPosition = findViewById(R.id.my_position_button);

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
                if (SelfPositionManager.GetPositionPlacemark() != null) {
                    RenderTasks.getHandle().getNavigator().setLongitude(SelfPositionManager.GetPositionPlacemark()
                            .getPosition().longitude);
                    RenderTasks.getHandle().getNavigator().setLatitude(SelfPositionManager.GetPositionPlacemark()
                            .getPosition().latitude);
                    RenderTasks.getHandle().getNavigator().setAltitude(1e4);
                    RenderTasks.getHandle().requestRedraw();
                }
            }
        });
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

        SelfPositionManager.Init(this);
        InitSearvhView();
        InitButtons();
    }
}
