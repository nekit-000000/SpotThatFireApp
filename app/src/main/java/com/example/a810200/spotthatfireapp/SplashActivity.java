package com.example.a810200.spotthatfireapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.a810200.spotthatfireapp.FireLoader.FireManager;


public class SplashActivity extends Activity {

    public FireManager fm = new FireManager();

    private class LoadingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground (Void ... params) {
            FireManagerHolder.fm.InitManager(1e7, 1e4, 5);

            return null;
        }

        @Override
        protected void onPostExecute (Void content) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoadingTask().execute();
    }
}