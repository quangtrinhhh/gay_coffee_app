package com.example.kiotviet_fake.database.insertProduct;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ProductInsert {
    @FormUrlEncoded
    @POST("products/insert.php")
    Call<String> insertProduct(
            @Field("name") String name,
            @Field("price") String price,
            @Field("quantity") int quantity,
            @Field("categories_id") int categories_id
    );
}
