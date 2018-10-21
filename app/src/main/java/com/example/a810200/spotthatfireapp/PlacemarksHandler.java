package com.example.a810200.spotthatfireapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Camera;
import gov.nasa.worldwind.geom.LookAt;
import gov.nasa.worldwind.geom.Position;


class PlacemarksHandler extends WorldWindow {

    public PlacemarksHandler (Context context) {
        super(context);
    }


    public PlacemarksHandler (Context context, EGLConfigChooser configChooser) {
        super(context, configChooser);
    }


    public PlacemarksHandler (Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            PlacemarkKeeper.Update(this, navigator.getAltitude());
        }

        return true;
    }
}