package com.songjumin.moviereview;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.songjumin.moviereview.adapter.MyReviewAdapter;
import com.songjumin.moviereview.adapter.ReviewAdapter;
import com.songjumin.moviereview.model.MyReview;
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
    MyReviewAdapter myReviewAdapter;
    ArrayList<MyReview> myReviewArrayList = new ArrayList<>();

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

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);


        requestQueue = Volley.newRequestQueue(MyReviewList.this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyReviewList.this));

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
                        getNetworkData();
                    }
                }
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        myReviewArrayList.clear();
        offset = 0;
        order = "desc";
        cnt = 0;
        getNetworkData();
    }
        public void getNetworkData() {
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
                                    int reply_id = jsonObject.getInt("reply_id");
                                    String title = jsonObject.getString("title");
                                    String content = jsonObject.getString("content");
                                    int rating = jsonObject.getInt("rating");

                                    MyReview myReview = new MyReview(reply_id, title, content, rating);
                                    myReviewArrayList.add(myReview);
                                }

                                myReviewAdapter = new MyReviewAdapter(MyReviewList.this, myReviewArrayList);
                                recyclerView.setAdapter(myReviewAdapter);

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
                            Toast.makeText(MyReviewList.this, "본인 리뷰만 삭제 가능합니다.",
                                    Toast.LENGTH_SHORT).show();
                            return;
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
        return super.onOptionsItemSelected(item);
    }
}