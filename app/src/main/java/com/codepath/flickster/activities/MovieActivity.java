package com.codepath.flickster.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.flickster.R;
import com.codepath.flickster.adapters.MovieArrayAdapter;
import com.codepath.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    // Automatically finds each field by the specified ID.
    @BindView(R.id.lvMovies) ListView lvItems;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    String url;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        //Bind the views
        ButterKnife.bind(this);
        url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        client = new AsyncHttpClient();
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);
        loadMovies();
        swipeContainer.setOnRefreshListener(movieReferesh);
        setupListViewListener();
    }

    SwipeRefreshLayout.OnRefreshListener movieReferesh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadMovies();
        }
    };

    public void loadMovies() {
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults;
                movies.clear();
                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    Log.d("FLICK", movies.toString());
                    //Signal refreshing has stopped
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(MovieActivity.this, "success", Toast.LENGTH_SHORT).show();
                Log.d("FLICK", "success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MovieActivity.this, "failed", Toast.LENGTH_SHORT).show();
                Log.d("FLICK", "failed");
            }
        });
    }

    private void setupListViewListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        Movie movie = movies.get(pos);
                        Intent i;
                        if (movie.ispopular()) {
                            i = new Intent(MovieActivity.this, PlayTrailerActivity.class);
                            i.putExtra("movieid", movie.getId());
                            i.putExtra("playmovie", 1);
                        } else {
                            i = new Intent(MovieActivity.this, MovieDetailsActivity.class);
                            //put movie details into extras
                            i.putExtra("title", movie.getOriginalTitle());
                            i.putExtra("imageurl", movie.getPosterpath(Configuration.ORIENTATION_LANDSCAPE));
                            i.putExtra("rating", movie.getCurrentpopularity());
                            i.putExtra("releasedate", movie.getReleasedate());
                            i.putExtra("overview", movie.getOverview());
                            i.putExtra("movieid", movie.getId());
                        }
                        //start detail activity
                        startActivity(i);
                    }
                });

    }

}
