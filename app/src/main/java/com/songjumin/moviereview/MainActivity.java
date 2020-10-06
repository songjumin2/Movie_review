package com.songjumin.moviereview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.songjumin.moviereview.adapter.RecyclerViewAdapter;
import com.songjumin.moviereview.model.Movie;
import com.songjumin.moviereview.model.UserReq;
import com.songjumin.moviereview.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<Movie> movieArrayList = new ArrayList<>();

    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        requestQueue = Volley.newRequestQueue(MainActivity.this);


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_MOVIE_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA", response.toString());

                        // results 제이슨 어레이로 오니까 아래처럼
                        try {
                            JSONArray results = response.getJSONArray("results");
                            // 어레이 안에있는거 포 루프 돈다
                            for(int i = 0; i < results.length(); i++){
                                //제이슨 오브젝트로 가져옴
                                JSONObject jsonObject = results.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                int vote_count = jsonObject.getInt("vote_count");
                                //Double vote_average = jsonObject.getDouble("vote_average");
                                Double vote_average;
                                if (results.getJSONObject(i).isNull("vote_average")){
                                    vote_average = 0.0;
                                }else {
                                    vote_average = results.getJSONObject(i).getDouble("vote_average");
                                }
                                String title = jsonObject.getString("title");
                                String original_title = jsonObject.getString("original_title");
                                String release_date = jsonObject.getString("release_date");
                                String overview = jsonObject.getString("overview");
                                String poster_path = jsonObject.getString("poster_path");


                                Movie movie = new Movie(id, vote_count, vote_average, title, original_title, release_date, overview, poster_path);
                                movieArrayList.add(movie);
                            }

                            adapter = new RecyclerViewAdapter(MainActivity.this, movieArrayList);
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
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
                Intent i = new Intent(MainActivity.this, MyPage.class);
                startActivity(i);
                finish();
        } else {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
        }
            return true;
        }
        if (id == R.id.Home){
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.MyFavorites){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(MainActivity.this, MyFavorite.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.MyReview){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(MainActivity.this, MyReviewList.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



