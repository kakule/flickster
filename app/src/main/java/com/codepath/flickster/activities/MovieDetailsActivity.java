package com.codepath.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.squareup.picasso.Picasso;

/**
 * Created on 10/15/2016.
 */
public class MovieDetailsActivity extends AppCompatActivity{
    String movieid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        String title = getIntent().getStringExtra("title");
        String imgurl = getIntent().getStringExtra("imageurl");
        float rating = getIntent().getFloatExtra("rating", 0);
        String releasedate = getIntent().getStringExtra("releasedate");
        String overview = getIntent().getStringExtra("overview");
        movieid = getIntent().getStringExtra("movieid");
        TextView tvTitle = (TextView) findViewById(R.id.tvDetailTitle);
        TextView tvReleaseDate = (TextView) findViewById(R.id.tvDetailRelease);
        TextView tvOverview = (TextView) findViewById(R.id.tvDetailOverview);
        ImageView ivImage = (ImageView) findViewById(R.id.ivDetail);
        RatingBar brDetail = (RatingBar) findViewById(R.id.rbDetail);

        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvReleaseDate.setText("Release date: " + releasedate);
        brDetail.setRating(rating);
        Picasso.with(this).load(imgurl)
                .placeholder(R.drawable.flicker_placeholder_r)
                .error(R.drawable.flicker_not_found_r2)
                .into(ivImage);

        ivImage.setOnClickListener(imListener);
    }

    ImageView.OnClickListener imListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MovieDetailsActivity.this, PlayTrailerActivity.class);
            i.putExtra("movieid", movieid);
            i.putExtra("playmovie", 0);
            startActivity(i);
        }
    };
}
