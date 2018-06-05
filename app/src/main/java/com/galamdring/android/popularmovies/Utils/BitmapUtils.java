package com.galamdring.android.popularmovies.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;

public class BitmapUtils {
    public static Bitmap GetResizedBitmapForBackground(DisplayMetrics metrics, Bitmap map){


        int targetWidth = metrics.widthPixels;
        int targetHeight = metrics.heightPixels;

        int width = map.getWidth();
        int height = map.getHeight();
        float ratio = 0;
        float widthratio = (float)targetWidth/(float)width;
        float heightratio=(float)targetHeight/(float)height;
        if(widthratio>heightratio){
            ratio=widthratio;
        }
        else{
            ratio=heightratio;
        }
        if(ratio==0){
            Log.d("BitmapResize","Got ratio of 0, returning original. targetWidth: "+targetWidth+" targetHeight: "+targetHeight);
            return map;
        }
        int dstWidth=(int)(width*ratio);
        int dstHeight =(int) (height*ratio);
        Log.d("BitmapResize","Using ratio of "+ratio+" dstHeight: "+dstHeight+" dstWidth: "+dstWidth);
        return Bitmap.createScaledBitmap(map,dstWidth,dstHeight ,false);
    }

    public static Bitmap GetResizedBitmap(Bitmap map, int width, int height){
        Log.d("BitmapResize","Original Size: "+map.getHeight()+" x "+map.getWidth()+" Resizing to height: "+height+" width: "+width);
        return Bitmap.createScaledBitmap(map,width,height ,false);
    }

    public static Bitmap GetResizedBitmapWithAspect(Bitmap bitmap, int size) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float ratio;
        if(height>width){
            ratio = (float)size/width;
        }
        else{
            ratio = (float)size/height;
        }
        return GetResizedBitmap(bitmap,(int)(width*ratio), (int)(height*ratio));

    }
}
