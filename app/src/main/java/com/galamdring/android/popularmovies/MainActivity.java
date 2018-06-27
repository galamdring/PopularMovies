package com.galamdring.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.galamdring.android.popularmovies.Data.Favorite;
import com.galamdring.android.popularmovies.Data.Movie;
import com.galamdring.android.popularmovies.Data.MovieViewModel;
import com.galamdring.android.popularmovies.Data.OurExecutors;
import com.galamdring.android.popularmovies.Sync.MoviesApi;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler,
        AdapterView.OnItemSelectedListener{
    private RecyclerView moviesRecyclerView;
    private MoviesAdapter moviesAdapter;
    private RecyclerView.LayoutManager moviesLayoutManager;
    private ProgressBar loadingIndicator;
    private final int LoaderId = 3145;
    private MovieViewModel movieViewModel;
    private LiveData<List<Movie>> movieData;
    private LiveData<List<Favorite>> favoriteData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);
        moviesRecyclerView.setHasFixedSize(true);

        moviesLayoutManager = new GridLayoutManager(this,2);
        moviesRecyclerView.setLayoutManager(moviesLayoutManager);

        loadingIndicator = findViewById(R.id.progress_bar);

        /*
        //Dummy data!
        List<Movie> dataSet = new ArrayList<Movie>();
        dataSet.add(new Movie(this,"http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","Interstellar"));
        dataSet.add(new Movie(this,"http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","Interstellar 2"));
        */

        moviesAdapter = new MoviesAdapter(this, this);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);


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
        syncMovies("top_rated");
    }

    private void syncPopular() {
        syncMovies("popular");
    }

    private void syncMovies(final String sortType){
        final Context context = this;
        moviesAdapter.setData(null);
        OurExecutors.getINSTANCE().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {
                MoviesApi.syncMovies(context, sortType);
            }
        });
        if (movieData != null && movieData.hasObservers()) {
            movieData.removeObservers(this);
        }
        movieData = movieViewModel.getMovieList();
        movieData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                moviesAdapter.setData(movies);
            }
        });
    }
    public void startSync(){syncPopular();}

    @Override
    public void onClick(Movie movie) {
        Intent movieDetailIntent = new Intent(this, DetailActivity.class);
        movieDetailIntent.putExtra("Movie", movie);
        startActivity(movieDetailIntent);
    }

    @Override
    public void onFavoriteClick(final boolean favorite, final Movie movie) {
        final Context context =this;
        OurExecutors.getINSTANCE().getDbIO().execute(new Runnable() {
            @Override
            public void run() {
                if(favorite) MoviesApi.addFavorite(context,movie);
                else MoviesApi.removeFavorite(context,movie);
                movie.setFavorite(favorite);
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] options = getResources().getStringArray(R.array.sortTypes);
        String selected = options[position];
        if(selected.equals(getString(R.string.mostPopularString))){

            syncPopular();
        }
        if(selected.equals(getString(R.string.highestRatedString))){
            syncHighRated();
        }
        if(selected.equals(getString(R.string.favoritesString))){
            loadFavorites();
        }
    }

    private void loadFavorites() {
        // Completed TODO: Load the movies that are flagged as favorites
        movieData.removeObservers(this);
        movieData = movieViewModel.getFavorites();
        movieData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favorites) {
                moviesAdapter.setData(favorites);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        syncPopular();
    }
}
