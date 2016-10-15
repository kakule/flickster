package com.codepath.flickster.models;

import android.content.res.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created on 10/14/2016.
 */
public class Movie {

    String posterpath;
    String backdroppath;
    String originalTitle;
    String overview;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterpath = jsonObject.getString("poster_path");
        this.backdroppath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");

    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;

    }

    public String getPosterpath(int orientation) {
        String imageType;
        String imageSize;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                //landscape image
                imageSize = "w780";
                imageType = backdroppath;
                break;
            default:
                //return portrait for all other requests
                imageSize = "w342";
                imageType = posterpath;
        }
        return String.format("https://image.tmdb.org/t/p/%s/%s",imageSize, imageType);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }
}
