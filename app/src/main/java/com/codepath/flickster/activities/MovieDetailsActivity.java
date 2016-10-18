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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 10/15/2016.
 */
public class MovieDetailsActivity extends AppCompatActivity{
    String movieid;
    @BindView(R.id.tvDetailTitle) TextView tvTitle;
    @BindView(R.id.tvDetailRelease) TextView tvReleaseDate;
    @BindView(R.id.tvDetailOverview) TextView tvOverview;
    @BindView(R.id.ivDetail) ImageView ivImage;
    @BindView(R.id.rbDetail) RatingBar brDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        //Bind the views
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        String imgurl = getIntent().getStringExtra("imageurl");
        float rating = getIntent().getFloatExtra("rating", 0);
        String releasedate = getIntent().getStringExtra("releasedate");
        String overview = getIntent().getStringExtra("overview");
        movieid = getIntent().getStringExtra("movieid");
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
