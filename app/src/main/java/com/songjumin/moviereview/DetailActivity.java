package com.songjumin.moviereview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.songjumin.moviereview.adapter.RecyclerViewAdapter;
import com.songjumin.moviereview.adapter.ReviewAdapter;
import com.songjumin.moviereview.model.Movie;
import com.songjumin.moviereview.model.Review;
import com.songjumin.moviereview.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class DetailActivity extends AppCompatActivity {

    TextView txtTitle;
    TextView txtOriginalTitle;
    TextView txtReleaseDate;
    TextView txtOverview;
    ImageView imgPoster;
    Button btnReview;

    RecyclerView recyclerView;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewArrayList = new ArrayList<>();

    RequestQueue requestQueue;

    String order = "desc";

    int offset = 0;
    int limit = 25;
    int cnt;

    String token;

    int movie_id;
    String title;
    int reply_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Intent intent = getIntent();
        movie_id = intent.getIntExtra("id", 0);
        String poster_path = intent.getStringExtra("poster_path");
        title = intent.getStringExtra("title");
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

        requestQueue = Volley.newRequestQueue(DetailActivity.this);

        btnReview = findViewById(R.id.btnReview);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                token = sp.getString("token", null);

                if (token != null) {
                    Intent i = new Intent(DetailActivity.this, ReviewPage.class);
                    i.putExtra("title", title);
                    i.putExtra("id", movie_id);
                    startActivity(i);
                } else {
                    Intent i = new Intent(DetailActivity.this, Register.class);
                    startActivity(i);
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        reviewArrayList.clear();
        offset = 0;
        order = "desc";
        cnt = 0;
        getNetworkData();
    }

    private void getNetworkData() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + "/api/v1/reply" + "?movie_id=" + movie_id + "&offset=" + offset + "&limit=" + limit + "&order=" + order,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA", response.toString());

                        // results 제이슨 어레이로 오니까 아래처럼
                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                return;
                            }
                            JSONArray items = response.getJSONArray("items");
                            // 어레이 안에있는거 포 루프 돈다
                            for (int i = 0; i < items.length(); i++) {
                                //제이슨 오브젝트로 가져옴
                                JSONObject jsonObject = items.getJSONObject(i);
                                int reply_id = jsonObject.getInt("reply_id");
                                String title = jsonObject.getString("title");
                                String email = jsonObject.getString("email");
                                String content = jsonObject.getString("content");
                                int rating = jsonObject.getInt("rating");
                                String created_at = jsonObject.getString("created_at");

                                Review review = new Review(reply_id, title, email, content, rating, created_at);
                                reviewArrayList.add(review);
                            }

                            reviewAdapter = new ReviewAdapter(DetailActivity.this, reviewArrayList);
                            recyclerView.setAdapter(reviewAdapter);

                            offset = offset + response.getInt("cnt");
                            cnt = response.getInt("cnt");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("AAA", "error : " + error);
                    }
                }
        );
        requestQueue.add(request);


    }
    public void deleteReply (final int position) {
        Log.i("AAA", ""+position);
        Review review = reviewArrayList.get(position);
        int reply_id = review.getReply_id();

        JSONObject body = new JSONObject();
        try {
            body.put("reply_id", reply_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Util.BASE_URL + "/api/v1/reply/delete",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.MyPage) {
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(DetailActivity.this, MyPage.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(DetailActivity.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.Home){
            Intent i = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.MyFavorites){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(DetailActivity.this, MyFavorite.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(DetailActivity.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.MyReview){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(DetailActivity.this, MyReviewList.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(DetailActivity.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
