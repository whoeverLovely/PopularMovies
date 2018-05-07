package com.louise.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PostersAdapter.ItemClickListener,
        AdapterView.OnItemSelectedListener, FetchMoviesTask.AsyncResponse{
    private final static String TAG = MainActivity.class.getSimpleName();

    PostersAdapter mAdapter;
    RecyclerView mRecyclerView;
    List<Movie> mMovieList;
    ProgressBar progressBar;

    public static final String EXTRA_MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.process_indicator);

        /*actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(getString(R.string.sort_by));
        }*/

        Spinner spinner = findViewById(R.id.sort_by_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_by_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // set up the RecyclerView
        mRecyclerView = findViewById(R.id.posters_grid_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new PostersAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        //execute the async task
        new FetchMoviesTask(this, this).execute();

    }

    @Override
    public void onItemClick(View view, int position) {
        Movie movie = mMovieList.get(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(DefaultSharedPreferenceConstants.SORT_BY, parent.getItemAtPosition(position).toString());
        editor.apply();
        new FetchMoviesTask(this, this).execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void processFinish(List<Movie> movieList) {
        mMovieList = movieList;
        mAdapter.swapData(movieList);
        /*progressBar.setVisibility(View.INVISIBLE);*/
    }

    /*class FetchMoview extends AsyncTask<Void, Void, List<Movie>> {
        Context context;

        FetchMoview(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {

            List<Movie> movieList = null;
            try {
                String[] sortByArr = context.getResources().getStringArray(R.array.sort_by_array);
                String sortByDefault = sortByArr[0];
                String sortBy = PreferenceManager.getDefaultSharedPreferences(context)
                        .getString(DefaultSharedPreferenceConstants.SORT_BY,
                                sortByDefault);

                String url = NetworkUtil.POPULAR_URL;
                if (sortBy.equals(sortByArr[1]))
                    url = NetworkUtil.TOP_RATED_URL;

                movieList = NetworkUtil.fetchMovieList(url);
            } catch (IOException e) {
                Toast.makeText(context, "Network error!", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "movie count: " + movieList.size());
            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mMovieList = movies;
            mAdapter.swapData(movies);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }*/
}
