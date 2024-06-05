package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProductSelectService {
    @FormUrlEncoded
    @POST("products/select.php")
    Call<String> getProducts(
            @Field("shop_id") String shop_id
    );
}
