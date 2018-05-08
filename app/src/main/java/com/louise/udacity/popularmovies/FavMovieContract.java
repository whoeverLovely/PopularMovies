package com.louise.udacity.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavMovieContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.louise.udacity.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "movies" directory
    public static final String PATH_MOVIES = "favmovies";

    public static final long INVALID_MOVIE_ID = -1;

    public static final class FavMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "favmovies";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_RELEASE_DATE= "releaseDate";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
