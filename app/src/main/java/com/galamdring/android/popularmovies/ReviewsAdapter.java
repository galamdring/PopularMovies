package com.galamdring.android.popularmovies;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.galamdring.android.popularmovies.Data.MovieContract;
import com.squareup.picasso.Picasso;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    Cursor ReviewsCursor;
    Context TheContext;

    public ReviewsAdapter(Context context ){
        TheContext=context;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(TheContext).inflate(R.layout.review_item,parent, false);
        view.setFocusable(true);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        ReviewsCursor.moveToPosition(position);
        holder.tv_content.setText(ReviewsCursor.getString(MovieContract.MovieEntry.INDEX_REVIEW_COLUMN_CONTENT));
        holder.tv_author.setText(ReviewsCursor.getString(MovieContract.MovieEntry.INDEX_REVIEW_COLUMN_AUTHOR));
        holder.tv_link.setTag(ReviewsCursor.getString(MovieContract.MovieEntry.INDEX_REVIEW_COLUMN_URL));
    }

    @Override
    public int getItemCount() {
        if(ReviewsCursor!=null)
        return ReviewsCursor.getCount();
        else return 0;
    }

    public void setData(Cursor data){
        this.ReviewsCursor= data;
        notifyDataSetChanged();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_author;
        public TextView tv_content;
        public TextView tv_link;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            tv_author=itemView.findViewById(R.id.tv_review_author);
            tv_content=itemView.findViewById(R.id.tv_review_content);
            tv_link = itemView.findViewById(R.id.tv_reviewLink);

            tv_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = v.findViewById(R.id.tv_reviewLink).getTag().toString();
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(Uri.parse(url));
                    if (webIntent.resolveActivity(TheContext.getPackageManager()) != null) {
                        TheContext.startActivity(webIntent);
                    }
                }
            });
        }
    }
}
