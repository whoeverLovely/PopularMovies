package com.louise.udacity.popularmovies;

import android.content.Intent;
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
        titleTV.setText(intent.getStringExtra(MainActivity.EXTRA_TITLE));
        originalTitleTV.setText(intent.getStringExtra(MainActivity.EXTRA_ORIGINAL_TITLE));
        releaseDateTV.setText(intent.getStringExtra(MainActivity.EXTRA_RELEASE_DATE));
        voteTV.setText(intent.getStringExtra(MainActivity.EXTRA_VOTE));
        overviewTV.setText(intent.getStringExtra(MainActivity.EXTRA_OVERVIEW));
        Picasso.get().load(NetworkUtil.getFullImagePath(intent.getStringExtra(MainActivity.EXTRA_PATH))).into(posterImage);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
}
