package com.example.kiotviet_fake.database.updateProduct;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateProductService {
    @FormUrlEncoded
    @POST("products/update.php")
    Call<String> updateProduct(
            @Field("id") int id,
            @Field("name") String name,
            @Field("price") String price,
            @Field("quantity") int quantity,
            @Field("categories_id") int categories_id
    );
}
