package com.galamdring.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.galamdring.android.popularmovies.Data.MovieContract;
import com.galamdring.android.popularmovies.Utils.BitmapUtils;
import com.galamdring.android.popularmovies.Utils.TrailerUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    ArrayList<String> TrailerCursor;
    Context TheContext;

    public TrailerAdapter(Context context) {
        TheContext=context;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(TheContext).inflate(R.layout.trailer_list_item,parent, false);
        view.setFocusable(true);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        String youtubeKey = TrailerCursor.get(position);
        holder.thumbnail.setTag(R.string.youtubeKey,youtubeKey);
        final ImageView thumbnail = holder.thumbnail;
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                bitmap= BitmapUtils.GetResizedBitmapWithAspect(bitmap,60);
                thumbnail.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(TheContext).load("http://img.youtube.com/vi/"+youtubeKey+"/0.jpg").into(target);
        holder.thumbnail.setTag(R.string.targetHolder,target);
    }

    @Override
    public int getItemCount() {
        if(TrailerCursor==null) return 0;
        return TrailerCursor.size();
    }

    public void setData(ArrayList<String> data){
        TrailerCursor = data;
        //This will also check if it is null, because we return 0 if the list is null.
        if(getItemCount()==0){
            Toast.makeText(TheContext, "No trailer data found for this movie.", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("TrailerAdapter","Got "+getItemCount()+" trailers.");
        }
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TrailerViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.iv_trailerThumb);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = (String) v.getTag(R.string.youtubeKey);
                    TrailerUtils.watchYoutubeVideo(TheContext,id);
                }
            });

        }
        //get images from http://img.youtube.com/vi/{ID}/0.jpg
    }
}
