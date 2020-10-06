package com.songjumin.moviereview.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.songjumin.moviereview.DetailActivity;
import com.songjumin.moviereview.Login;
import com.songjumin.moviereview.MainActivity;
import com.songjumin.moviereview.R;
import com.songjumin.moviereview.model.Review;
import com.songjumin.moviereview.util.Util;

import java.util.ArrayList;

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

        int rating = review.getRating();
        String content = review.getContent();

        holder.txtAverage.setText(""+rating+" 점");
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
        ImageButton imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtAverage = itemView.findViewById(R.id.txtAverage);
            txtReview = itemView.findViewById(R.id.txtReview);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

}


