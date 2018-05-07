package com.louise.udacity.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

    private final static String TAG = FetchMoviesTask.class.getSimpleName();

    public interface AsyncResponse {
        void processFinish(List<Movie> movieList);
    }

    private AsyncResponse delegate;
    private Context context;


    public FetchMoviesTask(AsyncResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
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
            e.printStackTrace();
            /*Toast.makeText(context, "Network error!", Toast.LENGTH_LONG).show();*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "movie count: " + movieList.size());
        return movieList;
    }

    @Override
    protected void onPostExecute(List<Movie> movieList) {
        delegate.processFinish(movieList);
    }
}
