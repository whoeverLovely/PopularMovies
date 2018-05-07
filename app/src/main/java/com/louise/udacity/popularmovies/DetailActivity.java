package com.louise.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get trailer id

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

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
