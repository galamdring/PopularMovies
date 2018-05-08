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

import com.galamdring.android.popularmovies.Data.MovieContract;
import com.galamdring.android.popularmovies.Sync.MovieSyncTask;

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

        ActionBar actionBar = getSupportActionBar();
        String[] options = getResources().getStringArray(R.array.sortTypes);


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

    public void startSync(){
        Intent intentToStartSync = new Intent(this, MovieSyncTask.class);
        intentToStartSync.setAction("popular");
        this.startService(intentToStartSync);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch(id){
            case LoaderId:

                Uri moviesUri = MovieContract.MovieEntry.MOVIES_CONTENT_URI;
                String sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY;
                return new CursorLoader(this, moviesUri,
                        MovieContract.MovieEntry.MainActivitySelectColumns,
                        null, null, sortOrder);
            default:
                throw new RuntimeException("These are not the droids you are looking for. (Loader not implemented.)"+id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        moviesAdapter.setData(data);
        moviesRecyclerView.setVisibility(View.VISIBLE);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] options = getResources().getStringArray(R.array.sortTypes);
        String selected = options[position];
        if(selected.equals("Most Popular")){
            syncPopular();
        }
        if(selected.equals("Highest Rated")){
            syncHighRated();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        syncPopular();
    }
}
