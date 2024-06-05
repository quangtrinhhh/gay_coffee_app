package com.example.kiotviet_fake.database.deleteCategory;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CategoryDeleteService {
    @FormUrlEncoded
    @POST("categories/delete.php")
    Call<String> deleteCategory(
            @Field("id") int id
    );
}