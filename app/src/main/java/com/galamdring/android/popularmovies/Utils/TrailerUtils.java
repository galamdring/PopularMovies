package com.galamdring.android.popularmovies.Utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class TrailerUtils {
    // Helper method found at : https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    // This will launch the Youtube App if available, and open Youtube in the browser if not.
    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
