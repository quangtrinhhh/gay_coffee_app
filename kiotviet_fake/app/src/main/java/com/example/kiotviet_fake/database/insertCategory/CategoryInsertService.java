package com.example.kiotviet_fake.database.insertCategory;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CategoryInsertService {
    @FormUrlEncoded
    @POST("categories/insert.php")
    Call<String> insertCategory(
            @Field("name") String name,
            @Field("shop_id") String shop_id
    );
}
