package com.songjumin.moviereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.songjumin.moviereview.util.Util;

public class DetailActivity extends AppCompatActivity {

    TextView txtTitle;
    TextView txtOriginalTitle;
    TextView txtReleaseDate;
    TextView txtOverview;
    ImageView imgPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String poster_path = intent.getStringExtra("poster_path");
        String title = intent.getStringExtra("title");
        String original_title = intent.getStringExtra("original_title");
        String release_date = intent.getStringExtra("release_date");
        String overview = intent.getStringExtra("overview");

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(title);

        txtOriginalTitle = findViewById(R.id.txtOriginalTitle);
        txtOriginalTitle.setText(original_title);

        imgPoster = findViewById(R.id.imgPoster);
        Glide.with(this).load(Util.BASE_IMAGE_URL + poster_path).into(imgPoster);

        txtOverview = findViewById(R.id.txtOverview);
        txtOverview.setText(overview);

        txtReleaseDate = findViewById(R.id.txtReleaseDate);
        txtReleaseDate.setText(release_date);


    }
}