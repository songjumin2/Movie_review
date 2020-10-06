package com.songjumin.moviereview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.songjumin.moviereview.api.NetworkClient;
import com.songjumin.moviereview.api.UserApi;
import com.songjumin.moviereview.model.UserReq;
import com.songjumin.moviereview.model.UserRes;
import com.songjumin.moviereview.util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

    EditText editEmail;
    EditText editPasswd1;
    EditText editPasswd2;
    Button btnRegister;
    Button btnLogin;
    Button btnCancel;

    RequestQueue requestQueue;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(Register.this);

        editEmail = findViewById(R.id.editEmail);
        editPasswd1 = findViewById(R.id.editPasswd1);
        editPasswd2 = findViewById(R.id.editPasswd2);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = editEmail.getText().toString().trim();
                final String passwd1 = editPasswd1.getText().toString().trim();
                String passwd2 = editPasswd2.getText().toString().trim();

                if(email.contains("@") == false){
                    Toast.makeText(Register.this, "이메일 형식에 맞게 작성해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwd1.length() < 6 || passwd1.length() > 12){
                    Toast.makeText(Register.this, "비밀번호 길이는 6자리 이상, 12자리 이하로 작성해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwd1.compareTo(passwd2) != 0){
                    Toast.makeText(Register.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserReq userReq = new UserReq(email, passwd1);

                Retrofit retrofit = NetworkClient.getRetrofitClient(Register.this);
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.createUser(userReq);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        if (response.isSuccessful()){
                            boolean success = response.body().isSuccess();
                            String token = response.body().getToken();
                            Log.i("AAA","success : " + success + "token : " + token);

                            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("token", token);
                            editor.apply();

                            Intent i = new Intent(Register.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
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
                Intent i = new Intent(Register.this, MyPage.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.Home){
            Intent i = new Intent(Register.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.MyFavorites){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(Register.this, MyFavorite.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.MyReview){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(Register.this, MyReviewList.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
