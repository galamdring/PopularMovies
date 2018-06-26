package com.galamdring.android.popularmovies.Utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JSONUtils {


    /* Largely replicated from helper method in:
    Title: Sunshine
    Author: Udacity
    Date: 4/30/18
    Source: https://github.com/udacity/ud851-Sunshine/blob/student/S12.04-Solution-ResourceQualifiers/app/src/main/java/com/example/android/sunshine/utilities/NetworkUtils.java
    */
    public static JSONObject getJSONObjectFromUrl(String urlString) throws IOException,JSONException{
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream input = urlConnection.getInputStream();

            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            boolean moreData = scanner.hasNext();
            String data = null;
            if(moreData) data = scanner.next();
            scanner.close();
            //Log.d("getJSONObjectFromUrl","Got json data: "+data);
            return new JSONObject(data);

        }
        finally { urlConnection.disconnect(); }
    }
}
