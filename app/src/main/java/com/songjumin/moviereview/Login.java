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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.songjumin.moviereview.api.NetworkClient;
import com.songjumin.moviereview.api.UserApi;
import com.songjumin.moviereview.model.UserReq;
import com.songjumin.moviereview.model.UserRes;
import com.songjumin.moviereview.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {

    EditText editEmail;
    EditText editPasswd;
    Button btnLogin;
    Button btnRegister;
    Button btnCancel;
    CheckBox autoLogin;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editEmail = findViewById(R.id.editEmail);
        editPasswd = findViewById(R.id.editPasswd1);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);
        autoLogin = findViewById(R.id.autoLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                String passwd = editPasswd.getText().toString().trim();

                if (email.contains("@") == false) {
                    Toast.makeText(Login.this, "이메일형식이 올바르지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwd.isEmpty() || passwd.length() < 4 || passwd.length() > 12) {
                    Toast.makeText(Login.this, "비밀번호 규칙에 맞지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.isEmpty() || passwd.isEmpty()) {
                    Toast.makeText(Login.this, "이메일과 비밀번호 둘 다 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                UserReq userReq = new UserReq(email, passwd);

                Retrofit retrofit = NetworkClient.getRetrofitClient(Login.this);
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.loginUser(userReq);
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

                            Intent i = new Intent(Login.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(Login.this, "회원이 아닙니다. 회원가입을 해주세요",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, MainActivity.class);
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
                Intent i = new Intent(Login.this, MyPage.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(Login.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.Home){
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.MyFavorites){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(Login.this, MyFavorite.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(Login.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        if (id == R.id.MyReview){
            SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
            token = sp.getString("token", null);

            if (token != null) {
                Intent i = new Intent(Login.this, MyReviewList.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(Login.this, Login.class);
                startActivity(i);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}