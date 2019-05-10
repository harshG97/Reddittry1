package com.example.reddittry1.reddit_api;

import com.example.reddittry1.models.FeedResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("{type}")
    Call<FeedResponse> getFeed(@Header("Authorization") String token, @Header("User-Agent") String agent, @Path("type") String type);//@Query("api_key") String apiKey);

    @GET("best")
    Call<FeedResponse> getHomeFeed(@Header("Authorization") String token, @Header("User-Agent") String agent);//@Query("api_key") String apiKey);

    @GET("top")
    Call<FeedResponse> getTopFeed(@Header("Authorization") String token, @Header("User-Agent") String agent);//@Query("api_key") String apiKey);

    @GET("hot")
    Call<FeedResponse> getHotFeed(@Header("Authorization") String token, @Header("User-Agent") String agent);//@Query("api_key") String apiKey);

    @GET("best")
    Call<FeedResponse> getHomeFeed(@Header("Authorization") String token, @Header("User-Agent") String agent, @Query("after") String after);//@Query("api_key") String apiKey);

    @GET("top")
    Call<FeedResponse> getTopFeed(@Header("Authorization") String token, @Header("User-Agent") String agent, @Query("after") String after);

    @GET("hot")
    Call<FeedResponse> getHotFeed(@Header("Authorization") String token, @Header("User-Agent") String agent, @Query("after") String after);


    @GET("comments/{id}")
    Call<JSONArray> getComments(@Header("Authorization") String token, @Header("User-Agent") String agent, @Path("id") String id);
    @POST("api/vote")
    Call<JSONObject> postVote(@Header("Authorization") String token, @Query("dir") int dir, @Query("id") String name);

    @POST("api/comment")
    Call<JSONObject> postComment(@Header("Authorization") String token, @Query("thing_id") String id, @Query("text") String text);

//    @GET("movie/{id}")
//    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}


