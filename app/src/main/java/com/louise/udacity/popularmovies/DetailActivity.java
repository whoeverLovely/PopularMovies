package com.louise.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements ItemClickListener {

    int id;
    RecyclerView mTrailerRecyclerList;
    TrailerAdapter mTrailerAdapter;
    List<Trailer> trailers;
    static boolean isFavorite;
    Movie movie;

    RecyclerView mReviewRecyclerList;
    ReviewAdapter mReviewAdapter;
    List<Review> reviews;

    TextView noTrailerTV;
    TextView noReviewTV;
    ProgressBar trailerPrograssBar;
    ProgressBar reviewPrograssBar;
    ImageButton favoriteButton;

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
        noTrailerTV = findViewById(R.id.no_trailer_msg);
        noReviewTV = findViewById(R.id.no_review_msg);
        trailerPrograssBar = findViewById(R.id.trailer_process_indicator);
        reviewPrograssBar = findViewById(R.id.review_process_indicator);
        favoriteButton = findViewById(R.id.imageButton_favorite);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);
        titleTV.setText(movie.getTitle());
        originalTitleTV.setText(movie.getOriginalTitle());
        releaseDateTV.setText(movie.getReleaseDate());
        voteTV.setText(movie.getVoteAverage());
        overviewTV.setText(movie.getOverview());
        Picasso.get().load(NetworkUtil.getFullImagePath(movie.getPosterPath())).into(posterImage);

        id = movie.getId();

        // Display trailer RecyclerView
        mTrailerRecyclerList = findViewById(R.id.trailer_recyclerView);
        mTrailerRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        mTrailerRecyclerList.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this, this);
        mTrailerRecyclerList.setAdapter(mTrailerAdapter);

        // Display review RecyclerView
        mReviewRecyclerList = findViewById(R.id.review_recyclerView);
        mReviewRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        mReviewAdapter = new ReviewAdapter(this);
        mReviewRecyclerList.setAdapter(mReviewAdapter);
        mReviewRecyclerList.setNestedScrollingEnabled(false);

        // Display correct imagebutton image
        Cursor cursor = getContentResolver().query(ContentUris.withAppendedId(FavMovieContract.FavMovieEntry.CONTENT_URI, id),
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            isFavorite = true;
            favoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
        } else {
            favoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFavorite) {
                    Log.d(TAG, "fav cancelled");
                    // Change ImageButton image
                    favoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    isFavorite = false;
                    // Remove the movie from the favmovies table
                    getContentResolver().delete(ContentUris.withAppendedId(FavMovieContract.FavMovieEntry.CONTENT_URI, id),
                            null,
                            null);
                } else {
                    Log.d(TAG, "fav added");
                    // Change ImageButton image
                    favoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    isFavorite = true;
                    // Add the movie to the favmovies table
                    ContentValues cv = new ContentValues();
                    cv.put(FavMovieContract.FavMovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
                    cv.put(FavMovieContract.FavMovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                    cv.put(FavMovieContract.FavMovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                    cv.put(FavMovieContract.FavMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                    cv.put(FavMovieContract.FavMovieEntry.COLUMN_TITLE, movie.getTitle());
                    cv.put(FavMovieContract.FavMovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
                    cv.put(FavMovieContract.FavMovieEntry._ID, id);
                    getContentResolver().insert(FavMovieContract.FavMovieEntry.CONTENT_URI, cv);

                }


            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getTrailers();
        getReviews();
    }

    @Override
    protected void onResume() {
        super.onResume();


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

        trailerPrograssBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(NetworkUtil.buildURL(NetworkUtil.VIDEOS, id),
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            trailers = NetworkUtil.getTrailers(response);
                            mTrailerAdapter.swapData(trailers);
                            if (trailers.size() == 0) {
                                mTrailerRecyclerList.setVisibility(View.INVISIBLE);
                                noTrailerTV.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        } finally {
                            trailerPrograssBar.setVisibility(View.INVISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        trailerPrograssBar.setVisibility(View.INVISIBLE);
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
        watchYoutubeVideo(trailers.get(position).getTrailerKey());
    }

    private void getReviews() {

        reviewPrograssBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(NetworkUtil.buildURL(NetworkUtil.REVIEWS, id),
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            reviews = NetworkUtil.getReviews(response);
                            mReviewAdapter.swapData(reviews);

                            if (reviews.size() == 0) {
                                mReviewRecyclerList.setVisibility(View.INVISIBLE);
                                noReviewTV.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        } finally {
                            reviewPrograssBar.setVisibility(View.INVISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        reviewPrograssBar.setVisibility(View.INVISIBLE);
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingletonVolley.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
