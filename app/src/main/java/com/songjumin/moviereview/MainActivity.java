package com.songjumin.moviereview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.songjumin.moviereview.adapter.RecyclerViewAdapter;
import com.songjumin.moviereview.model.Movie;
import com.songjumin.moviereview.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<Movie> movieArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                            // 어레이 안에있는거 포루프 돈다
                            for(int i = 0; i < results.length(); i++){
                                //제이슨 오브젝트로 가져옴
                                JSONObject jsonObject = results.getJSONObject(i);
                                int vote_count = jsonObject.getInt("vote_count");
                                Double vote_average = jsonObject.getDouble("vote_average");
                                String title = jsonObject.getString("title");
                                String release_date = jsonObject.getString("release_date");
                                String overview = jsonObject.getString("overview");
                                String poster_path = jsonObject.getString("poster_path");


                                Movie movie = new Movie(vote_count, vote_average, title, release_date, overview, poster_path);
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
}