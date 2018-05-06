package com.louise.udacity.popularmovies;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtil {

    private final static String TAG = NetworkUtil.class.getSimpleName();

    private final static String BASE_URL = "https://image.tmdb.org/t/p/";
    public final static String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    public final static String TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated";

    private static URL buildUrlWithAPIKey(String queryUrl) {
        System.out.print(queryUrl);
        Uri uri = Uri.parse(queryUrl).buildUpon()
                .appendQueryParameter("api_key", Keys.API_KEY)
                .build();
        try {
            URL url = new URL(uri.toString());
            Log.v(TAG, "URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(String urlStr) throws IOException {
        URL url = buildUrlWithAPIKey(urlStr);
        if (url != null) {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                String response = null;
                if (hasInput) {
                    response = scanner.next();
                }
                scanner.close();
                return response;
            } finally {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public static List<Movie> fetchMovieList(String urlStr) throws IOException, JSONException {
        String resp = getResponseFromHttpUrl(urlStr);
        JSONObject respJson = new JSONObject(resp);
        JSONArray moviesJson = respJson.getJSONArray("results");
        JSONObject movieJson;
        Movie movie;
        List<Movie> movieList = new LinkedList<>();
        for(int i = 0; i < moviesJson.length(); i++) {
            movieJson = moviesJson.getJSONObject(i);
            movie = new Movie();
            movie.setOriginalTitle(movieJson.getString("original_title"));
            movie.setOverview(movieJson.getString("overview"));
            movie.setPosterPath(movieJson.getString("poster_path"));
            movie.setReleaseDate(movieJson.getString("release_date"));
            movie.setVoteAverage(movieJson.getString("vote_average"));
            movie.setTitle(movieJson.getString("title"));
            movieList.add(movie);
        }
        return movieList;
    }

    public static String getFullImagePath(String path) {
        String url = BASE_URL + "w342" + path;
        Log.d(TAG, "Image url: " + url);
        return url;
    }
}
