package com.example.kiotviet_fake.database.deleteProduct;

import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ProductDeleteService {
    @FormUrlEncoded
    @POST("products/delete.php")
    Call<String> deleteProduct(
            @Field("id") int id
    );
}