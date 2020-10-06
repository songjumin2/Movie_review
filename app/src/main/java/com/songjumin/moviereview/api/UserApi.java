package com.songjumin.moviereview.api;

import com.songjumin.moviereview.model.UserReq;
import com.songjumin.moviereview.model.UserRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    // 회원가입
    @POST("/api/v1/users")
    Call<UserRes> createUser(@Body UserReq userReq);

    // 로그인
    @POST("/api/v1/users/login")
    Call<UserRes> loginUser(@Body UserReq userReq);

    // 로그아웃
    @DELETE("/api/v1/users/logout")
    Call<UserRes> logoutUser(@Header("Authorization") String token);

    // 모든 기기에서 로그아웃
    @DELETE("/api/v1/users/logoutAll")
    Call<UserRes> logoutAll(@Header("Authorization") String token);

    // 비밀번호 변경
    @POST("/api/v1/users/change")
    Call<UserRes> changePasswd (@Body UserReq userReq);

    // 비밀번호 분실
    @POST("/api/v1/users/forgotpasswd")
    Call<UserRes> forgotPasswd (@Body UserReq userReq);

    // 비밀번호 찾기
    @POST("/api/v1/users/resetPasswd/:resetPasswdToken")
    Call<UserRes> resetPasswd (@Body UserReq userReq);

    // 내 정보 가져오기
    @GET("/api/v1/users")
    Call<UserRes> getMyInfo(@Header("Authorization") String token,
                            @Query("offset") int offset,
                            @Query("limit") int limit);

    // 회원탈퇴
    @DELETE("/api/v1/users")
    Call<UserRes> deleteUser(@Header("Authorization") String token);

}
