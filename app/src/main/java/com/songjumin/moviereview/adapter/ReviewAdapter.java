package com.songjumin.moviereview.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.songjumin.moviereview.DetailActivity;
import com.songjumin.moviereview.Login;
import com.songjumin.moviereview.MainActivity;
import com.songjumin.moviereview.MyPage;
import com.songjumin.moviereview.MyReviewList;
import com.songjumin.moviereview.R;
import com.songjumin.moviereview.ReviewPage;
import com.songjumin.moviereview.UpdateReview;
import com.songjumin.moviereview.model.Review;
import com.songjumin.moviereview.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    Context context;
    ArrayList<Review> reviewArrayList;

    public ReviewAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {

        Review review = reviewArrayList.get(position);

        String title = review.getTitle();
        int rating = review.getRating();
        String content = review.getContent();

        holder.txtTitle.setText(title);
        holder.ratingBar.setRating(rating);
        holder.txtAverage.setText("평점 : "+rating);
        holder.txtReview.setText(content);

    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtAverage;
        TextView txtReview;
        RatingBar ratingBar;
        TextView txtTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtAverage = itemView.findViewById(R.id.txtAverage);
            txtReview = itemView.findViewById(R.id.txtReview);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            txtTitle = itemView.findViewById(R.id.txtTitle);

            ratingBar.setRating(0);

            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        }
    }
}


