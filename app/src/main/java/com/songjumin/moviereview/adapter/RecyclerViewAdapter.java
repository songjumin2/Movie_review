package com.songjumin.moviereview.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.songjumin.moviereview.DetailActivity;
import com.songjumin.moviereview.Login;
import com.songjumin.moviereview.MainActivity;
import com.songjumin.moviereview.R;
import com.songjumin.moviereview.ReviewPage;
import com.songjumin.moviereview.model.Movie;
import com.songjumin.moviereview.util.Util;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Movie> movieArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, final int position) {
        Movie movie = movieArrayList.get(position);
        int id = movie.getId();
        String title = movie.getTitle();
        String overview = movie.getOverview();
        String poster_path = movie.getPoster_path();
        // 화면에 표시하라
        holder.txtTitle.setText(title);
        holder.txtOverview.setText(overview);
        Glide.with(context).load(Util.BASE_IMAGE_URL + poster_path).into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id",movieArrayList.get(position).getId());
                intent.putExtra("title", movieArrayList.get(position).getTitle());
                intent.putExtra("original_title", movieArrayList.get(position).getOriginal_title());
                intent.putExtra("poster_path", movieArrayList.get(position).getPoster_path());
                intent.putExtra("overview", movieArrayList.get(position).getOverview());
                intent.putExtra("release_date", movieArrayList.get(position).getRelease_date());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTitle;
        public TextView txtOverview;
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtOverview = itemView.findViewById(R.id.txtOverview);
            img = itemView.findViewById(R.id.img);


        }
    }

}
