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

import com.galamdring.android.popularmovies.Data.MovieContract;
import com.squareup.picasso.Picasso;


class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private final MoviesAdapterOnClickHandler itemClickHandler;
    Cursor DataSet;
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
        DataSet.moveToPosition(position);


        Picasso.with(TheContext).load(DataSet.getString(MovieContract.MovieEntry.INDEX_COLUMN_POSTER_URL))
                .into(holder.iv_poster);
        holder.ib_favorite.setActivated(DataSet.getInt(MovieContract.MovieEntry.INDEX_COLUMN_FAVORITE) == 1);
    }

    @Override
    public int getItemCount() {
        if(DataSet!=null)return DataSet.getCount();
        return 0;
    }

    public void setData(Cursor data) {
        this.DataSet = data;
        notifyDataSetChanged();
    }

    public interface MoviesAdapterOnClickHandler{
        void onClick(int id);
        void onFavoriteClick(boolean favorite, int id);
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
                    DataSet.moveToPosition(position);
                    int id = DataSet.getInt(MovieContract.MovieEntry.INDEX_COLUMN_ID);
                    if(ib_favorite.isActivated()){
                        //Toast.makeText(TheContext, "Calling to remove favorite.", Toast.LENGTH_SHORT).show();
                        itemClickHandler.onFavoriteClick(false,id);
                    }
                    else {
                        //Toast.makeText(TheContext, "Calling to add favorite.", Toast.LENGTH_SHORT).show();
                        itemClickHandler.onFavoriteClick(true, id);
                    }
                    ib_favorite.setActivated(!ib_favorite.isActivated());
                }
            });
        }

        @Override
        public void onClick(View v) {
            //get which movie we are looking at.
            int position = getAdapterPosition();
            DataSet.moveToPosition(position);
            int id = DataSet.getInt(MovieContract.MovieEntry.INDEX_COLUMN_ID);
            itemClickHandler.onClick(id);
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
