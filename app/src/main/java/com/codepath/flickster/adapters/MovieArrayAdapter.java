package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 10/14/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie>{
    //View lookup cache for unpopular movie
    static class ViewHolderOne {
        @BindView(R.id.ivMovieImage) ImageView ivImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;

        public ViewHolderOne(View v) {
            ButterKnife.bind(this, v);
        }
    }

    //View lookup cache for popular movie
    static class ViewHolderTwo {
        @BindView(R.id.ivPopularMovieImage) ImageView ivImage;

        public ViewHolderTwo(View v) {
            ButterKnife.bind(this, v);
        }
    }
    public MovieArrayAdapter(Context context, List<Movie>movies) {
        super (context, android.R.layout.simple_list_item_1, movies);
    }

    // Return an integer representing the type. 1 - portrait and popular; 0 - otherwise
    @Override
    public int getItemViewType(int position) {
        return (getItem(position).ispopular() &&
                (getContext().getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_PORTRAIT)) ? 1 : 0;
    }


    // Total number of view types. We have only 2
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        ViewHolderOne viewholderone;
        ViewHolderTwo viewholdertwo;
        int orientation = getContext().getResources().getConfiguration().orientation;
        int type = getItemViewType(position);
        if (convertView == null) {
            //Get item type
            convertView = getInflatedLayoutForType(type, parent);
            if (type == 0) {
                viewholderone = new ViewHolderOne(convertView);
                //cache the viewHolder object inside fresh view
                 convertView.setTag(viewholderone);
            } else {
                viewholdertwo = new ViewHolderTwo(convertView);
                //cache the viewHolder object inside fresh view
                 convertView.setTag(viewholdertwo);
            }

        }

        if (type == 0) {
            viewholderone = (ViewHolderOne) convertView.getTag();
            //clear image from convertView
            viewholderone.ivImage.setImageResource(0);
            //populate new data into listview
            viewholderone.tvTitle.setText(movie.getOriginalTitle());
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                viewholderone.tvTitle.setTextAppearance(parent.getContext(),
                        android.R.style.TextAppearance_Medium);
                viewholderone.tvTitle.setTextColor(Color.parseColor("#1ab9dc"));
            }
            viewholderone.tvOverview.setText(movie.getOverview());

            Picasso.with(getContext()).load(movie.getPosterpath(orientation))
                    .placeholder(R.drawable.flicker_placeholder_r)
                    .error(R.drawable.flicker_not_found_r2)
                    .into(viewholderone.ivImage);
        } else {
            viewholdertwo = (ViewHolderTwo) convertView.getTag();
            //clear image from convertView
            viewholdertwo.ivImage.setImageResource(0);

            Picasso.with(getContext()).load(movie.getPosterpath(orientation))
                    .placeholder(R.drawable.flicker_placeholder_r)
                    .error(R.drawable.flicker_not_found_r2)
                    .into(viewholdertwo.ivImage);
        }

        //return the view
        return convertView;

    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type, ViewGroup parent) {
        if (type == 1) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_popular, parent, false);
        } else {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }
    }
}
