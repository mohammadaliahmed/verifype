package com.appsinventiv.verifype.Utils;


import com.appsinventiv.verifype.Models.ApiResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserClient {

    @POST("api/uploadFile")
    @Multipart
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part file, @Part("photo") RequestBody name

    );


    @Headers("Content-Type: application/json")
    @POST("verify")
    Call<ApiResponse> verify(
            @Body JsonElement jsonObject
    );

    @Headers("Content-Type: application/json")
    @POST("report")
    Call<ApiResponse> report(
            @Body JsonElement jsonObject
    );

    @Headers("Content-Type: application/json")
    @POST("score")
    Call<ApiResponse> score(
            @Body JsonElement jsonObject
    );

    @Headers("Content-Type: application/json")
    @GET("score")
    Call<ApiResponse> getScore(
    );

    @Headers("Content-Type: application/json")
    @GET("psycho")
    Call<ApiResponse> psycho(

    );


}
