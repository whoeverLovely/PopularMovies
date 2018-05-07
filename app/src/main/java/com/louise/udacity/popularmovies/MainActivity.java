package com.louise.udacity.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PostersAdapter.ItemClickListener,
        AdapterView.OnItemSelectedListener{
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

        fetchMovies();

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
        fetchMovies();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void fetchMovies() {
        progressBar.setVisibility(View.VISIBLE);

        // Get sort by
        String[] sortByArr = getResources().getStringArray(R.array.sort_by_array);
        String sortByDefault = sortByArr[0];
        String sortBy = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(DefaultSharedPreferenceConstants.SORT_BY,
                        sortByDefault);

        String APIType = NetworkUtil.POPULAR;
        if(sortBy.equals(sortByArr[1]))
            APIType = NetworkUtil.TOP_RATED;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(NetworkUtil.buildURL(APIType, 0),
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            mMovieList = NetworkUtil.getMovieList(response);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                        mAdapter.swapData(mMovieList);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingletonVolley.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
