package com.louise.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements ItemClickListener{

    int id;
    RecyclerView trailerList;
    TrailerAdapter mAdapter;
    List<String> trailerIdList;

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView titleTV = findViewById(R.id.textView_title);
        TextView originalTitleTV = findViewById(R.id.textView_original_title);
        TextView releaseDateTV = findViewById(R.id.textView_release_date);
        TextView voteTV = findViewById(R.id.textView_vote_average);
        TextView overviewTV = findViewById(R.id.textView_overview);
        ImageView posterImage = findViewById(R.id.imageView_poster);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);
        titleTV.setText(movie.getTitle());
        originalTitleTV.setText(movie.getOriginalTitle());
        releaseDateTV.setText(movie.getReleaseDate());
        voteTV.setText(movie.getVoteAverage());
        overviewTV.setText(movie.getOverview());
        Picasso.get().load(NetworkUtil.getFullImagePath(movie.getPosterPath())).into(posterImage);

        // Display trailer recycler list
        id = movie.getId();
        trailerList = findViewById(R.id.trailer_recyclerView);
        trailerList.setLayoutManager(new LinearLayoutManager(this));
        trailerList.setHasFixedSize(true);
        mAdapter = new TrailerAdapter(this, this);
        trailerList.setAdapter(mAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get trailer id
        getTrailers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                throw new RuntimeException("The menu item selected is unknown.");
        }
    }

    private void getTrailers() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(NetworkUtil.buildURL(NetworkUtil.VIDEOS, id),
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final List<String> trailerIds = NetworkUtil.getTrailerIds(response);
                            trailerIdList = trailerIds;
                            mAdapter.swapData(trailerIds.size());

                        } catch (JSONException e) {
                            Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingletonVolley.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        watchYoutubeVideo(trailerIdList.get(position));
    }
}
