package com.example.kiotviet_fake.database.inserUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserInsert {
    @FormUrlEncoded
    @POST("users/insert.php")
    Call<String> insertUser(
            @Field("name_shop") String nameShop,
            @Field("name_user") String nameUser,
            @Field("password") String password
    );
}
