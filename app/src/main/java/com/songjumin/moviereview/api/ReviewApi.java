package com.songjumin.moviereview.api;


import com.songjumin.moviereview.model.Review;
import com.songjumin.moviereview.model.ReviewRes;
import com.songjumin.moviereview.model.UserRes;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ReviewApi {

    // 댓글 작성
    @POST("/api/v1/reply")
    Call<ReviewRes> addReply(@Header("Authorization") String token,
                           @Body Review review);

}
