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
import com.songjumin.moviereview.adapter.ReviewAdapter;
import com.songjumin.moviereview.model.Review;
import com.songjumin.moviereview.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyReviewList extends AppCompatActivity {

    RecyclerView recyclerView;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewArrayList = new ArrayList<>();

    RequestQueue requestQueue;

    int offset = 0;
    int limit = 25;
    int cnt;

    String order = "desc";

    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(MyReviewList.this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyReviewList.this));

        getNetworkData();

    }

        private void getNetworkData() {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    Util.BASE_URL + "/api/v1/reply/review" + "?offset=" + offset + "&limit=" + limit + "&order=" + order,
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

                                    String content = jsonObject.getString("content");
                                    int rating = jsonObject.getInt("rating");

                                    Review review = new Review(content, rating);
                                    reviewArrayList.add(review);
                                }

                                reviewAdapter = new ReviewAdapter(MyReviewList.this, reviewArrayList);
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
                Intent i = new Intent(MyReviewList.this, MyPage.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(MyReviewList.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.Home){
            Intent i = new Intent(MyReviewList.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.MyFavorites){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(MyReviewList.this, MyFavorite.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(MyReviewList.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.MyReview){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(MyReviewList.this, MyReviewList.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(MyReviewList.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}