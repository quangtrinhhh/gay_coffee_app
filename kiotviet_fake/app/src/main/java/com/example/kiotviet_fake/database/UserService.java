package com.example.kiotviet_fake.database;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @GET("users/select.php")
    Call<String> getUsers();

    @FormUrlEncoded
    @POST("users/delete.php")
    Call<String> deleteUser(@Field("id") int id, @Field("newUserId") int newUserId);

    @FormUrlEncoded
    @POST("users/update.php")
    Call<String> updateUser(
            @Field("id") int id,
            @Field("name") String name,
            @Field("password") String password,
            @Field("role") String role);

    @FormUrlEncoded
    @POST("users/insertUserNhanVien.php")
    Call<String> insertUserNhanVien(
            @Field("name") String name,
            @Field("password") String password,
            @Field("role") String role,
            @Field("shop_id") String shop_id);
}
