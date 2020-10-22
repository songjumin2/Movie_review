package com.songjumin.moviereview.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.songjumin.moviereview.Login;
import com.songjumin.moviereview.R;
import com.songjumin.moviereview.UpdateReview;
import com.songjumin.moviereview.model.MyReview;
import com.songjumin.moviereview.util.Util;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ViewHolder> {

    Context context;
    ArrayList<MyReview> myReviewArrayList;

    public MyReviewAdapter(Context context, ArrayList<MyReview> myReviewArrayList) {
        this.context = context;
        this.myReviewArrayList = myReviewArrayList;
    }

    @NonNull
    @Override
    public MyReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myreview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewAdapter.ViewHolder holder, int position) {
        MyReview myReview = myReviewArrayList.get(position);

        String title = myReview.getTitle();
        int rating = myReview.getRating();
        String content = myReview.getContent();

        holder.txtTitle.setText(title);
        holder.ratingBar.setRating(rating);
        holder.txtAverage.setText("평점 : "+rating);
        holder.txtReview.setText(content);

        // 리뷰카드뷰 클릭하면 리뷰작성 페이지로 넘어가면서 데이터 그대로 넘어가고 수정페이지로감.
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = context.getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);
                Log.i("AAA", "token : " + token);
                if (token == null) {
                    // 로그인 액티비티를 띄운다.
                    Toast.makeText(context,"로그인을 해주세요", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }else {
                    Intent i = new Intent(context, UpdateReview.class);
                    i.putExtra("title", myReviewArrayList.get(position).getTitle());
                    i.putExtra("reply_id", myReviewArrayList.get(position).getReply_id());
                    i.putExtra("rating", myReviewArrayList.get(position).getRating());
                    i.putExtra("content", myReviewArrayList.get(position).getContent());
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myReviewArrayList.size();
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
