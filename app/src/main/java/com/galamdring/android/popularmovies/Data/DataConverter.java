package com.galamdring.android.popularmovies.Data;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;


public class DataConverter {
    @TypeConverter
    public static String trailerListToString(ArrayList<String> trailerIds){
        if(trailerIds==null) return "";
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(String id : trailerIds){
            if(first) first=false;
            else sb.append(";");

            sb.append(id);
        }

        return sb.toString().trim();
    }

    @TypeConverter
    public static ArrayList<String> stringToTrailerList(String trailerIds){
        if(trailerIds==null) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(trailerIds.split(";")));

    }
}
