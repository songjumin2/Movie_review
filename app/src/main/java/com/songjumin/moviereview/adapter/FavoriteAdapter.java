package com.songjumin.moviereview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.songjumin.moviereview.R;
import com.songjumin.moviereview.model.Favorite;
import com.songjumin.moviereview.util.Util;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{

    Context context;
    ArrayList<Favorite> favoriteArrayList;

    public FavoriteAdapter(Context context, ArrayList<Favorite> favoriteArrayList) {
        this.context = context;
        this.favoriteArrayList = favoriteArrayList;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {

        Favorite favorite = favoriteArrayList.get(position);

        String title = favorite.getTitle();
        String release_date = favorite.getRelease_date();
        String poster_path = favorite.getPoster_path();

        holder.txtTitle.setText(title);
        holder.txtReleaseDate.setText(release_date);
        Glide.with(context).load(Util.BASE_IMAGE_URL + poster_path).into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return favoriteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPoster;
        TextView txtTitle;
        TextView txtOriginalTitle;
        TextView txtReleaseDate;
        ImageButton imgMovieFavorite;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtOriginalTitle = itemView.findViewById(R.id.txtOriginalTitle);
            txtReleaseDate = itemView.findViewById(R.id.txtReleaseDate);
            imgMovieFavorite = itemView.findViewById(R.id.imgMovieFavorite);
        }
    }

}
