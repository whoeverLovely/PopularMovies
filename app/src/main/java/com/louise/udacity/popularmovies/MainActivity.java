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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostersAdapter.ItemClickListener,
        AdapterView.OnItemSelectedListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    PostersAdapter mAdapter;
    RecyclerView mRecyclerView;
    List<Movie> mMovieList;
    ProgressBar progressBar;

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_ORIGINAL_TITLE = "originalTitle";
    public static final String EXTRA_RELEASE_DATE = "releaseDate";
    public static final String EXTRA_VOTE = "vote";
    public static final String EXTRA_OVERVIEW = "overview";
    public static final String EXTRA_PATH = "path";

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

        //Fetch data
        new FetchMoview(this).execute();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onItemClick(View view, int position) {
        Movie movie = mMovieList.get(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_TITLE, movie.getTitle());
        intent.putExtra(EXTRA_ORIGINAL_TITLE, movie.getOriginalTitle());
        intent.putExtra(EXTRA_RELEASE_DATE, movie.getReleaseDate());
        intent.putExtra(EXTRA_VOTE, movie.getVoteAverage());
        intent.putExtra(EXTRA_OVERVIEW, movie.getOverview());
        intent.putExtra(EXTRA_PATH, movie.getPosterPath());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(DefaultSharedPreferenceConstants.SORT_BY, parent.getItemAtPosition(position).toString());
        editor.apply();
        new FetchMoview(this).execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class FetchMoview extends AsyncTask<Void, Void, List<Movie>> {
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
    }
}
