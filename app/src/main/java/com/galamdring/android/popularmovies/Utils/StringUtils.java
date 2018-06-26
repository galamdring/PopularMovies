package com.galamdring.android.popularmovies.Utils;

import java.util.List;

public class StringUtils {
    public static String ListToStringJoin(List<String> elements, String joiningString){
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(String element :elements){
            if(first) first =false;
            else sb.append(joiningString);
            sb.append(element);
        }
        return sb.toString();
    }
}
