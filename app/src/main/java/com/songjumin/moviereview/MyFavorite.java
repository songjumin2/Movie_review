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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.songjumin.moviereview.adapter.FavoriteAdapter;
import com.songjumin.moviereview.adapter.RecyclerViewAdapter;
import com.songjumin.moviereview.adapter.ReviewAdapter;
import com.songjumin.moviereview.model.Favorite;
import com.songjumin.moviereview.model.Movie;
import com.songjumin.moviereview.model.Review;
import com.songjumin.moviereview.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyFavorite extends AppCompatActivity {


    RequestQueue requestQueue;
    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;
    ArrayList<Favorite> favoriteArrayList = new ArrayList<>();

    String token;
    int offset = 0;
    int limit = 25;
    int cnt;

    int movie_id;
    String title;
    String release_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Intent intent = getIntent();
        movie_id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        release_date = intent.getStringExtra("release_date");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyFavorite.this));

        getFavorite();

    }

    private void getFavorite() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + "/api/v1/favorites" + "?offset=" + offset + "&limit=" + limit,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA", response.toString());
                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                return;
                            }
                            JSONArray items = response.getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                //제이슨 오브젝트로 가져옴
                                JSONObject jsonObject = items.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                int favorite_id = jsonObject.getInt("favorite_id");
                                String title = jsonObject.getString("title");
                                String release_date = jsonObject.getString("release_date");
                                String poster_path = jsonObject.getString("poster_path");

                                Favorite favorite = new Favorite(id, favorite_id, title, release_date, poster_path);
                                favoriteArrayList.add(favorite);
                            }
                            favoriteAdapter = new FavoriteAdapter(MyFavorite.this, favoriteArrayList);
                            recyclerView.setAdapter(favoriteAdapter);

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
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // 토큰 가져오는 코드 아래 두줄 (해더셋팅함)
                SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }

        };
        Volley.newRequestQueue(MyFavorite.this).add(request);
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
                Intent i = new Intent(MyFavorite.this, MyPage.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(MyFavorite.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.Home){
            Intent i = new Intent(MyFavorite.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.MyFavorites){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(MyFavorite.this, MyFavorite.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(MyFavorite.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.MyReview){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(MyFavorite.this, MyReviewList.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(MyFavorite.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}