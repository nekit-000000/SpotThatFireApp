package com.example.a810200.spotthatfireapp;

import android.content.Context;

public class RenderTasks {
    private static PlacemarksHandler wwd = null;

    public static void InitHandle(Context context) {
        wwd = new PlacemarksHandler(context);
    }

    public static PlacemarksHandler getHandle() {
        return wwd;
    }
}
