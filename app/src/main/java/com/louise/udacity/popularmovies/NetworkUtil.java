package com.louise.udacity.popularmovies;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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

    private final static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private final static String API_BASE_URL = "https://api.themoviedb.org/3/movie";

    public final static String POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    public final static String VIDEOS = "videos";
    public final static String REVIEWS = "reviews";

    public static String buildURL(String APIType, int movieId) {
        Uri uri = Uri.parse(API_BASE_URL).buildUpon().appendQueryParameter("api_key", Keys.API_KEY).build();
        switch (APIType) {
            case POPULAR:
                uri = uri.buildUpon().appendPath(POPULAR).build();
                break;
            case TOP_RATED:
                uri = uri.buildUpon().appendPath(TOP_RATED).build();
                break;
            case VIDEOS:
                uri = uri.buildUpon().appendPath(String.valueOf(movieId)).appendPath(VIDEOS).build();
                break;
            case REVIEWS:
                uri = uri.buildUpon().appendPath(String.valueOf(movieId)).appendPath(REVIEWS).build();
                break;
            default:
                throw new RuntimeException("The API type doesn't exist.");
        }
        Log.d(TAG, "uri to string is " + uri.toString());
        return uri.toString();
    }

    public static List<Movie> getMovieList(JSONObject jsonObject) throws JSONException {
        JSONArray moviesJson = jsonObject.getJSONArray("results");
        JSONObject movieJson;
        Movie movie;
        List<Movie> movieList = new LinkedList<>();
        for (int i = 0; i < moviesJson.length(); i++) {
            movieJson = moviesJson.getJSONObject(i);
            movie = new Movie();
            movie.setOriginalTitle(movieJson.getString("original_title"));
            movie.setOverview(movieJson.getString("overview"));
            movie.setPosterPath(movieJson.getString("poster_path"));
            movie.setReleaseDate(movieJson.getString("release_date"));
            movie.setVoteAverage(movieJson.getString("vote_average"));
            movie.setTitle(movieJson.getString("title"));
            movie.setId(movieJson.getInt("id"));
            movieList.add(movie);
        }
        return movieList;
    }

    public static List<Trailer> getTrailers(JSONObject jsonObject) throws JSONException {
        JSONArray videoArray = jsonObject.getJSONArray("results");
        List<Trailer> trailerList = new LinkedList<Trailer>();
        Trailer trailer;
        for (int i = 0; i < videoArray.length(); i++) {
            if ("Trailer".equals(videoArray.getJSONObject(i).getString("type"))) {
                trailer = new Trailer();
                trailer.setTrailerKey(videoArray.getJSONObject(i).getString("key"));
                trailer.setTrailerName(videoArray.getJSONObject(i).getString("name"));
                trailerList.add(trailer);
            }
        }
        return trailerList;
    }

    public static List<Review> getReviews(JSONObject jsonObject) throws JSONException {
        JSONArray reviewArray = jsonObject.getJSONArray("results");
        List<Review> reviewList = new LinkedList<Review>();

        if(reviewArray != null && reviewArray.length()> 0) {
            Review review;
            for(int i = 0; i < reviewArray.length(); i++) {
                review = new Review();
                review.setReviewContent(reviewArray.getJSONObject(i).getString("content"));
                review.setReviewerName(reviewArray.getJSONObject(i).getString("author"));
                reviewList.add(review);
            }

        }

        return reviewList;
    }

    public static String getFullImagePath(String path) {
        String url = IMAGE_BASE_URL + "w342" + path;
        Log.d(TAG, "Image url: " + url);
        return url;
    }


}
