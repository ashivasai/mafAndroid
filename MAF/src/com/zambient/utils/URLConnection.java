package com.zambient.utils;

import android.util.Log;

import com.zambient.constants.AppConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLConnection {
    public String readUrl(String serviceURL) throws IOException {
        String data = "";
        Log.d("GETHOMEDETAILSURL", serviceURL);
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(serviceURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}