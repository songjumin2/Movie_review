package com.songjumin.moviereview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.songjumin.moviereview.adapter.FavoriteAdapter;
import com.songjumin.moviereview.adapter.MyReviewAdapter;
import com.songjumin.moviereview.api.NetworkClient;
import com.songjumin.moviereview.api.UserApi;
import com.songjumin.moviereview.model.UserRes;
import com.songjumin.moviereview.util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyPage extends AppCompatActivity {

    TextView txtEmail;
    Button btnLogout;
    Button btnMemberOut;
    Button btnFavorite;
    Button btnMyReview;

    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText("회원님 반갑습니다."+"\n"+"마이페이지 화면입니다."+"\n"+"좋아하는 영화 목록과 나의 리뷰 목록을 확인 할 수 있습니다.");

        btnLogout = findViewById(R.id.btnLogout);
        btnMemberOut = findViewById(R.id.btnMemberOut);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMyReview = findViewById(R.id.btnMyReview);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPage.this);
                builder.setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                        final String token = sharedPreferences.getString("token", null);
                        Log.i("AAA", token);

                        Retrofit retrofit = NetworkClient.getRetrofitClient(MyPage.this);
                        UserApi userApi = retrofit.create(UserApi.class);

                        Call<UserRes> call = userApi.logoutUser("Bearer "+token);
                        call.enqueue(new Callback<UserRes>() {
                            @Override
                            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                                if(response.isSuccessful()){
                                    if(response.body().isSuccess()){
                                        SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME,
                                                MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("token", null);
                                        editor.apply();
                                        Intent i = new Intent(MyPage.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<UserRes> call, Throwable t) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("NO", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        btnMemberOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPage.this);
                builder.setTitle("회원탈퇴").setMessage("회원탈퇴 하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                        final String token = sharedPreferences.getString("token", null);
                        Log.i("AAA", token);

                        Retrofit retrofit = NetworkClient.getRetrofitClient(MyPage.this);
                        UserApi userApi = retrofit.create(UserApi.class);

                        Call<UserRes> call = userApi.deleteUser("Bearer "+token);
                        call.enqueue(new Callback<UserRes>() {
                            @Override
                            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                                if(response.isSuccessful()){
                                    if(response.body().isSuccess()){
                                        SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME,
                                                MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("token", null);
                                        editor.apply();
                                        Intent i = new Intent(MyPage.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<UserRes> call, Throwable t) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("NO", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                token = sp.getString("token", null);

                if (token != null) {
                    Intent i = new Intent(MyPage.this, MyFavorite.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(MyPage.this, Login.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        btnMyReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                token = sp.getString("token", null);

                if (token != null) {
                    Intent i = new Intent(MyPage.this, MyReviewList.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(MyPage.this, Login.class);
                    startActivity(i);
                    finish();
                }
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
                Intent i = new Intent(MyPage.this, MyPage.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(MyPage.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.Home){
            Intent i = new Intent(MyPage.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
