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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.songjumin.moviereview.DetailActivity;
import com.songjumin.moviereview.Login;
import com.songjumin.moviereview.MainActivity;
import com.songjumin.moviereview.R;
import com.songjumin.moviereview.ReviewPage;
import com.songjumin.moviereview.model.Movie;
import com.songjumin.moviereview.util.Util;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

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
        String original_title = movie.getOriginal_title();
        String release_date = movie.getRelease_date();
        String overview = movie.getOverview();
        String poster_path = movie.getPoster_path();

        // 화면에 표시하라
        holder.txtTitle.setText(title);
        holder.txtOriginalTitle.setText(original_title);
        holder.txtReleaseDate.setText(release_date);

        Glide.with(context).load(Util.BASE_IMAGE_URL + poster_path).into(holder.imgPoster);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", movieArrayList.get(position).getId());
                intent.putExtra("title", movieArrayList.get(position).getTitle());
                intent.putExtra("original_title", movieArrayList.get(position).getOriginal_title());
                intent.putExtra("poster_path", movieArrayList.get(position).getPoster_path());
                intent.putExtra("overview", movieArrayList.get(position).getOverview());
                intent.putExtra("release_date", movieArrayList.get(position).getRelease_date());
                context.startActivity(intent);
            }
        });

        if (movie.getIs_favorite() == 1) {
            holder.imgFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.imgFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }

    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public TextView txtTitle;
        public TextView txtOriginalTitle;
        public TextView txtReleaseDate;
        public ImageView imgPoster;
        public ImageView imgFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView= itemView.findViewById(R.id.cardView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtOriginalTitle = itemView.findViewById(R.id.txtOriginalTitle);
            txtReleaseDate = itemView.findViewById(R.id.txtReleaseDate);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);

            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.i("AAA", position+"");
                    SharedPreferences sp = context.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
                    String token = sp.getString("token", null);
                    Log.i("AAA", "token : " + token);
                    if (token == null){
                        // 로그인 액티비티를 띄운다.
                        Intent i = new Intent(context, Login.class);
                        context.startActivity(i);
                    }else {
                        // 정상적으로 별표 표시를 서버로 보낸다.
                        // 즐겨찾기 추가하는 API를 호출할건데 호출하는 코드는 메인 액티비티에 메소드 만들고
                        // 여기에서는 position 값만 넘겨주도록 한다.

                        int is_favorite = movieArrayList.get(position).getIs_favorite();
                        Log.i("AAA", "" + is_favorite);
                        if (is_favorite == 1) {
                            // 별표가 이미 있으면 즐겨찾기 삭제 함수 호출!(실행되게해주는코드임)
                            ((MainActivity) context).deleteFavorite(position);
                        } else {
                            // 별표가 없으면 즐겨찾기 추가 함수 호출!
                            ((MainActivity) context).addFavorite(position);
                        }
                    }
                }
            });

        }
    }

}
