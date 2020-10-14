package com.songjumin.moviereview.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.songjumin.moviereview.model.Review;
import com.songjumin.moviereview.util.Util;

import org.w3c.dom.Text;

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

        holder.ratingBar.setNumStars(rating);
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
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtAverage = itemView.findViewById(R.id.txtAverage);
            txtReview = itemView.findViewById(R.id.txtReview);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            ratingBar = itemView.findViewById(R.id.ratingBar);


            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("리뷰 삭제").setMessage("리뷰 삭제 하시겠습니까?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int position = getAdapterPosition();
                            Log.i("AAA", position + "");
                            SharedPreferences sp = context.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
                            String token = sp.getString("token", null);
                            Log.i("AAA", "token : " + token);
                            if (token == null) {
                                // 로그인 액티비티를 띄운다.
                                Intent intent = new Intent(context, Login.class);
                                context.startActivity(intent);
                            } else {

                                ((DetailActivity)context).deleteReply(position);

                                reviewArrayList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), reviewArrayList.size());
                                return;
                            }
                        }
                    });
                    builder.setNegativeButton("NO", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });
        }
    }
}


