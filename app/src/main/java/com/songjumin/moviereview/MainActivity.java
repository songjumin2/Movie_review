package com.songjumin.moviereview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    int offset = 0;
    int limit = 25;
    String order = "desc";
    int cnt;

    String path = "/api/v1/movies";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if ((lastPosition + 1) == totalCount){
                    if(cnt == limit) {
                        // 네트워크 통해서 데이터를 더 불러오면 된다.
                        addNetworkData(path);
                    }
                }
            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);

        if (token != null){
            path = "/api/v1/movies/auth";
        }else {
            path = "/api/v1/movies";
        }
        getNetworkData(path);


    }
    @Override
    protected void onResume() {
        super.onResume();
        movieArrayList.clear();
        offset = 0;
        order = "desc";
        cnt = 0;
        getNetworkData(path);
    }
    private void getNetworkData(String path){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + path + "?offset="+offset + "&limit="+limit,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA", response.toString());

                        // results 제이슨 어레이로 오니까 아래처럼
                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false){
                                return;
                            }
                            // 어레이 안에있는거 포 루프 돈다
                            JSONArray items = response.getJSONArray("items");
                            for(int i = 0; i < items.length(); i++){
                                //제이슨 오브젝트로 가져옴
                                int id = items.getJSONObject(i).getInt("id");
                                int vote_count = items.getJSONObject(i).getInt("vote_count");
                                //Double vote_average = jsonObject.getDouble("vote_average");
                                Double vote_average;
                                if (items.getJSONObject(i).isNull("vote_average")){
                                    vote_average = 0.0;
                                }else {
                                    vote_average = items.getJSONObject(i).getDouble("vote_average");
                                }
                                String title = items.getJSONObject(i).getString("title");
                                String original_title = items.getJSONObject(i).getString("original_title");
                                String release_date = items.getJSONObject(i).getString("release_date");
                                String overview = items.getJSONObject(i).getString("overview");
                                String poster_path = items.getJSONObject(i).getString("poster_path");

                                int is_favorite;
                                if (items.getJSONObject(i).isNull("is_favorite")) {
                                    is_favorite = 0;
                                }else {
                                    is_favorite = items.getJSONObject(i).getInt("is_favorite");
                                }

                                Movie movie = new Movie(id, vote_count, vote_average, title, original_title, release_date, overview, poster_path, is_favorite);
                                movieArrayList.add(movie);
                            }

                            adapter = new RecyclerViewAdapter(MainActivity.this, movieArrayList);
                            recyclerView.setAdapter(adapter);

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

                    }
                }
        ){
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
        requestQueue.add(request);
    }
    private void addNetworkData(String path) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + path + "?offset=" + offset + "&limit=" + limit,
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
                            // 어레이 안에있는거 포 루프 돈다
                            JSONArray items = response.getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                //제이슨 오브젝트로 가져옴
                                int id = items.getJSONObject(i).getInt("id");
                                int vote_count = items.getJSONObject(i).getInt("vote_count");
                                //Double vote_average = jsonObject.getDouble("vote_average");
                                Double vote_average;
                                if (items.getJSONObject(i).isNull("vote_average")) {
                                    vote_average = 0.0;
                                } else {
                                    vote_average = items.getJSONObject(i).getDouble("vote_average");
                                }
                                String title = items.getJSONObject(i).getString("title");
                                String original_title = items.getJSONObject(i).getString("original_title");
                                String release_date = items.getJSONObject(i).getString("release_date");
                                String overview = items.getJSONObject(i).getString("overview");
                                String poster_path = items.getJSONObject(i).getString("poster_path");

                                int is_favorite;
                                if (items.getJSONObject(i).isNull("is_favorite")) {
                                    is_favorite = 0;
                                } else {
                                    is_favorite = items.getJSONObject(i).getInt("is_favorite");
                                }

                                Movie movie = new Movie(id, vote_count, vote_average, title, original_title, release_date, overview, poster_path, is_favorite);
                                movieArrayList.add(movie);
                            }
                            adapter.notifyDataSetChanged();

                            // 페이징을 위해서 오프셋을 변경시켜놔야 한다.
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
        requestQueue.add(request);
    }

    public void addFavorite (final int position) {
        Movie movie = movieArrayList.get(position);
        int movie_id = movie.getId();

        JSONObject body = new JSONObject();
        try {
            body.put("movie_id", movie_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("AAA", "" + movie_id);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Util.BASE_URL + "/api/v1/favorites",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA","add favorite : " + response.toString());
                        Movie movie = movieArrayList.get(position);
                        movie.setIs_favorite(1);
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("AAA",error.toString());
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
        requestQueue.add(request);
    }
    public void deleteFavorite (final int position) {
        Movie movie = movieArrayList.get(position);
        int movie_id = movie.getId();

        JSONObject body = new JSONObject();
        try {
            body.put("movie_id", movie_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("AAA", "" + movie_id);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Util.BASE_URL + "/api/v1/favorites/delete",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA", "add favorite : " + response.toString());
                        Movie movie = movieArrayList.get(position);
                        movie.setIs_favorite(0);
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("AAA", error.toString());
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
