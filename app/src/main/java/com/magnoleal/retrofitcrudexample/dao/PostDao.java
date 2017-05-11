package com.magnoleal.retrofitcrudexample.dao;

import com.magnoleal.retrofitcrudexample.model.Post;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by cti on 11/05/17.
 */

public interface PostDao {

    @GET("posts")
    Call<List<Post>> all();

    @GET("posts/{id}")
    Call<Post> find(@Path("id") int id);

    @POST("posts")
    Call<Post> create(@Body Post post);

    @POST("posts")
    @FormUrlEncoded
    Call<Post> create(@Field("title") String title, @Field("body") String body);

    @PUT("posts/{id}")
    Call<Post> update(@Path("id") int id, @Body Post post);

    @PUT("posts/{id}")
    @FormUrlEncoded
    Call<Post> update(@Path("id") int id, @Field("title") String title, @Field("body") String body);

    @DELETE("posts/{id}")
    Call<Post> delete(@Path("id") int id);

}
