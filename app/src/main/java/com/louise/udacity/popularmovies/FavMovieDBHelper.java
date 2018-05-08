package com.louise.udacity.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavMovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popular_movies.db";

    private static final int DATABASE_VERSION = 1;

    public FavMovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + FavMovieContract.FavMovieEntry.TABLE_NAME + " (" +
                FavMovieContract.FavMovieEntry._ID + " INTEGER PRIMARY KEY," +
                FavMovieContract.FavMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavMovieContract.FavMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavMovieContract.FavMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavMovieContract.FavMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavMovieContract.FavMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavMovieContract.FavMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavMovieContract.FavMovieEntry.COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavMovieContract.FavMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
