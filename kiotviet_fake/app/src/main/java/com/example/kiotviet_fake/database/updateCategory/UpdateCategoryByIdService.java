package com.example.kiotviet_fake.database.updateCategory;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateCategoryByIdService {
    @FormUrlEncoded
    @POST("categories/update.php")
    Call<String> updateCategoryById(
            @Field("id") int id,
            @Field("name") String name
    );
}
