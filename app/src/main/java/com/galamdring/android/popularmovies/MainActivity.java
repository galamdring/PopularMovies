package com.galamdring.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.galamdring.android.popularmovies.Data.MovieContract;
import com.galamdring.android.popularmovies.Sync.AddFavoriteTask;
import com.galamdring.android.popularmovies.Sync.MovieSyncTask;
import com.galamdring.android.popularmovies.Sync.ReviewSyncTask;
import com.galamdring.android.popularmovies.Sync.TrailerSyncTask;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,
        MoviesAdapter.MoviesAdapterOnClickHandler,
        AdapterView.OnItemSelectedListener{
    private RecyclerView moviesRecyclerView;
    private MoviesAdapter moviesAdapter;
    private RecyclerView.LayoutManager moviesLayoutManager;
    private ProgressBar loadingIndicator;
    private final int LoaderId = 3145;

    private Uri moviesUri = MovieContract.MovieEntry.MOVIES_CONTENT_URI;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesRecyclerView = (RecyclerView) findViewById(R.id.moviesRecyclerView);
        moviesRecyclerView.setHasFixedSize(true);

        moviesLayoutManager = new GridLayoutManager(this,2);
        moviesRecyclerView.setLayoutManager(moviesLayoutManager);

        loadingIndicator = findViewById(R.id.progress_bar);
        LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;
        getSupportLoaderManager().initLoader(LoaderId,null, callback);
        /*
        //Dummy data!
        List<Movie> dataSet = new ArrayList<Movie>();
        dataSet.add(new Movie(this,"http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","Interstellar"));
        dataSet.add(new Movie(this,"http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","Interstellar 2"));
        */

        moviesAdapter = new MoviesAdapter(this, this);
        moviesRecyclerView.setAdapter(moviesAdapter);
        startSync();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_type, menu);
        MenuItem item = menu.findItem(R.id.sort_type_spinner);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sortTypes, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return true;
    }


    private void syncHighRated() {
        Intent intentToStartSync = new Intent(this, MovieSyncTask.class);
        intentToStartSync.setAction("top_rated");
        this.startService(intentToStartSync);
    }

    private void syncPopular() {
        Intent intentToStartSync = new Intent(this, MovieSyncTask.class);
        intentToStartSync.setAction("popular");
        this.startService(intentToStartSync);
    }

    public void startSync(){syncPopular();}

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch(id){
            case LoaderId:


                return new CursorLoader(this, moviesUri,
                        MovieContract.MovieEntry.MovieSelectColumns,
                        null, null, null);
            default:
                throw new RuntimeException("These are not the droids you are looking for. (Loader not implemented.)"+id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        moviesAdapter.setData(data);
        moviesRecyclerView.setVisibility(View.VISIBLE);

        for(int i=0;i<data.getCount(); i++){
            data.moveToPosition(i);
            int movieId=data.getInt(MovieContract.MovieEntry.INDEX_COLUMN_ID);
            Intent intentReviewSync = new Intent(this, ReviewSyncTask.class);
            intentReviewSync.putExtra("MovieId",movieId);
            this.startService(intentReviewSync);
            Intent intentTrailerSync = new Intent(this, TrailerSyncTask.class);
            intentTrailerSync.putExtra("MovieId",movieId);
            this.startService(intentTrailerSync);
        }

    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    @Override
    public void onClick(int id) {
        Intent movieDetailIntent = new Intent(this,DetailActivity.class);
        Uri uriForMovieClicked = MovieContract.MovieEntry.GetContentUriForMovie(id);
        movieDetailIntent.setData(uriForMovieClicked);
        startActivity(movieDetailIntent);
    }

    @Override
    public void onFavoriteClick(boolean favorite, int id) {

        Intent addFavoriteIntent = new Intent(this,AddFavoriteTask.class);
        addFavoriteIntent.putExtra("id",id);
        addFavoriteIntent.putExtra("favorite",favorite);
        this.startService(addFavoriteIntent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] options = getResources().getStringArray(R.array.sortTypes);
        String selected = options[position];
        if(selected.equals(getString(R.string.mostPopularString))){
            moviesUri = MovieContract.MovieEntry.MOVIES_CONTENT_URI;
            syncPopular();
            getSupportLoaderManager().restartLoader(LoaderId,null, this);
        }
        if(selected.equals(getString(R.string.highestRatedString))){
            moviesUri = MovieContract.MovieEntry.MOVIES_CONTENT_URI;
            syncHighRated();
            getSupportLoaderManager().restartLoader(LoaderId,null, this);
        }
        if(selected.equals(getString(R.string.favoritesString))){
            moviesUri = MovieContract.MovieEntry.FAVORITES_CONTENT_URI;
            getSupportLoaderManager().restartLoader(LoaderId,null, this);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        syncPopular();
    }
}
