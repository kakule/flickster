package com.codepath.flickster.activities;

import android.os.Bundle;
import android.util.Log;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 10/16/2016.
 */
public class PlayTrailerActivity extends YouTubeBaseActivity{
    String youtube_api_key = "AIzaSyDw6Ee9kSr4no25mm2dy3xi8WTqocDVzhg";
    String url_base = "https://api.themoviedb.org/3/movie";
    String api_key = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    String movieid;
    String youtubeid;
    int playmovie = 0;
    OkHttpClient client = new OkHttpClient();
    @BindView(R.id.vidplayer) YouTubePlayerView youtubeplayer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.item_video);
        ButterKnife.bind(this);
        movieid = getIntent().getStringExtra("movieid");
        playmovie = getIntent().getIntExtra("playmovie", 0);
        client = new OkHttpClient();
        getYoutubeVidId();

    }

    public void getYoutubeVidId ( ) {
        String video_url = String.format("%s/%s/videos?api_key=%s", url_base, movieid, api_key);
        Request request = new Request.Builder()
                .url(video_url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();

                PlayTrailerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(responseData);
                            youtubeid = Movie.trailerfromJSONArray(json.getJSONArray("results"));
                            Log.d("FLICKER", "The youtube id is " + youtubeid);

                            youtubeplayer.initialize(youtube_api_key, new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                    YouTubePlayer youTubePlayer, boolean b) {
                                    Log.d("FLICKER", "The youtube again just before api call is " + youtubeid);
                                    // do any work here to cue video, play video, etc.
                                    if (playmovie == 0) {
                                        youTubePlayer.cueVideo(youtubeid);
                                    } else {
                                        youTubePlayer.loadVideo(youtubeid);
                                    }
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

}
