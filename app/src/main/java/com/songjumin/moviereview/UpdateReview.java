package com.songjumin.moviereview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.songjumin.moviereview.model.Review;
import com.songjumin.moviereview.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateReview extends AppCompatActivity {

    TextView txtTitle;
    RatingBar ratingBar;
    TextView txtRating;
    EditText editContent;
    Button btnCancel;
    Button btnReview;
    Button btnDelete;

    RequestQueue requestQueue;

    int movie_id;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_review);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);

        txtTitle = findViewById(R.id.txtTitle);
        ratingBar = findViewById(R.id.ratingBar);
        txtRating = findViewById(R.id.txtRating);
        editContent = findViewById(R.id.editContent);
        btnCancel = findViewById(R.id.btnCancel);
        btnReview = findViewById(R.id.btnReview);
        btnDelete = findViewById(R.id.btnDelete);


        final Intent intent = getIntent();
        int reply_id = intent.getIntExtra("reply_id", 0);
        movie_id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        int rating = intent.getIntExtra("rating",0);
        String content = intent.getStringExtra("content");


        txtTitle.setText(title);
        ratingBar.setRating(rating);
        txtRating.setText(""+rating+" 점");
        editContent.setText(content);

        requestQueue = Volley.newRequestQueue(UpdateReview.this);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtRating.setText("평점 : " + rating);
            }
        });

        ratingBar.setRating(0);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double rating = ratingBar.getRating();
                String content = editContent.getText().toString().trim();

                if (rating == 0 || content.isEmpty()) {
                    Toast.makeText(UpdateReview.this, "평점과 내용은 필수입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject body = new JSONObject();
                try {
                    body.put("reply_id", ""+ reply_id);
                    body.put("rating", "" + rating);
                    body.put("content", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("AAA",  " "+ reply_id + " " + rating + " " + content);

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.PUT,
                        Util.BASE_URL + "/api/v1/reply",
                        body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(UpdateReview.this,"리뷰가 수정되었습니다.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(UpdateReview.this,"본인 리뷰만 수정가능합니다.", Toast.LENGTH_LONG).show();
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
        });

        btnDelete. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateReview.this);
                builder.setTitle("리뷰 삭제").setMessage("리뷰 삭제 하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
                                        Log.i("AAA", "reply delete : " + response.toString());
                                        Toast.makeText(UpdateReview.this,"리뷰가 삭제되었습니다.", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(UpdateReview.this,"본인 리뷰만 삭제가능합니다.", Toast.LENGTH_LONG).show();
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
                });
                builder.setNegativeButton("NO", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

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
                Intent i = new Intent(UpdateReview.this, MyPage.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(UpdateReview.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.Home){
            Intent i = new Intent(UpdateReview.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
