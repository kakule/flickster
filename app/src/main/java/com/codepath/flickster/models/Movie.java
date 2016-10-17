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

    String  posterpath;
    String  backdroppath;
    String  originaltitle;
    String  overview;
    String  releasedate;
    String  currentpopularity;
    String  id;
    String  videoid = "";
    int     popularitynumber = 5;
    boolean ispopular;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterpath = jsonObject.getString("poster_path");
        this.backdroppath = jsonObject.getString("backdrop_path");
        this.originaltitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.releasedate = jsonObject.getString("release_date");
        this.id = jsonObject.getString("id");
        this.currentpopularity = jsonObject.getString("vote_average");
        if ((int)Float.parseFloat(currentpopularity) >= popularitynumber) {
            this.ispopular = true;
        }

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

    public static String trailerfromJSONArray(JSONArray array) {
        String name;
        String site;
        String id = null;
        for (int i = 0; i < array.length(); i++) {
            // Here the official trailer is our preference but if we don't get it
            // let's return any youtube video available
            try {
                name = array.getJSONObject(i).getString("name");
                site = array.getJSONObject(i).getString("site");

                if (site.equals("YouTube")) {
                    id = array.getJSONObject(i).getString("key");
                }

                if (name.equals("Official Trailer") && site.equals("YouTube")) {
                    id = array.getJSONObject(i).getString("key");
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public String getPosterpath(int orientation) {
        String imageType;
        String imageSize;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE ||
            this.ispopular()) {
            //landscape image or popular image
            imageSize = "w780";
            imageType = backdroppath;
        } else {
                imageSize = "w342";
                imageType = posterpath;
        }
        return String.format("https://image.tmdb.org/t/p/%s/%s",imageSize, imageType);
    }

    public void setVideoId(String videoid) {
        this.videoid = videoid;
    }

    public String getOriginalTitle() {
        return originaltitle;
    }

    public String getOverview() {
        return overview;
    }

    public boolean ispopular() {
        return ispopular;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public String getId() {
        return id;
    }

    public String getVideoId() {
        return videoid;
    }

    public float getCurrentpopularity() {
        return  Float.parseFloat(currentpopularity);
    }
}
