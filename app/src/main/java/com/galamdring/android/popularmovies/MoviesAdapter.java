package com.galamdring.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.galamdring.android.popularmovies.Data.Movie;
import com.galamdring.android.popularmovies.Data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.List;


class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private final MoviesAdapterOnClickHandler itemClickHandler;
    List<Movie> TheMovies;
    Context TheContext;

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(TheContext).inflate(R.layout.movie_list_item, parent, false);
        view.setFocusable(true);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        Movie movie = TheMovies.get(position);


        Picasso.with(TheContext).load(movie.getPosterUrl())
                .into(holder.iv_poster);
        holder.ib_favorite.setActivated(movie.isFavorite());
    }

    @Override
    public int getItemCount() {
        if(TheMovies!=null)return TheMovies.size();
        return 0;
    }

    public void setData(List<Movie> movies) {
        this.TheMovies = movies;
        notifyDataSetChanged();
    }

    public interface MoviesAdapterOnClickHandler{
        void onClick(Movie movie);
        void onFavoriteClick(boolean favorite, Movie movie);
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iv_poster;
        private ImageView ib_favorite;


        public MoviesViewHolder(View view){
            super(view);
            iv_poster=view.findViewById(R.id.iv_poster);
            ib_favorite = view.findViewById(R.id.iv_favorite);
            view.setOnClickListener(this);
            ib_favorite.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    //get which movie we are looking at.
                    int position = getAdapterPosition();
                    Movie movie = TheMovies.get(position);

                    if(ib_favorite.isActivated()){
                        //Toast.makeText(TheContext, "Calling to remove favorite.", Toast.LENGTH_SHORT).show();
                        itemClickHandler.onFavoriteClick(false,movie);
                    }
                    else {
                        //Toast.makeText(TheContext, "Calling to add favorite.", Toast.LENGTH_SHORT).show();
                        itemClickHandler.onFavoriteClick(true, movie);
                    }
                    ib_favorite.setActivated(!ib_favorite.isActivated());
                }
            });
        }

        @Override
        public void onClick(View v) {
            //get which movie we are looking at.
            int position = getAdapterPosition();
            Movie movie = TheMovies.get(position);
            int id = movie.get_id();
            itemClickHandler.onClick(movie);
/*                case R.id.iv_favorite:

                    break;
            }
*/
        }
    }

    public MoviesAdapter(@NonNull Context context, MoviesAdapterOnClickHandler onClickHandler) {
        itemClickHandler = onClickHandler;
        TheContext = context;
    }


}
