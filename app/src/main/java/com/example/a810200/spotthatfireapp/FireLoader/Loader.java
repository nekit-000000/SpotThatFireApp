package com.example.a810200.spotthatfireapp.FireLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class Loader {
    private String getContent(String path) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(path);
            HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(10000);
            c.connect();
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            return (buf.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public String[] getFireInfo(String url) {
        String[] text;
        try {
            text = getContent(url).split("\n");
        }
        catch (IOException ex) {
            text = new String[1];
            text[0] = ex.getMessage();
        }

        if (text == null) {
            text = new String[1];
            text[0] = "Error! In file 0 strings!";
        }

        return text;
    }
}
