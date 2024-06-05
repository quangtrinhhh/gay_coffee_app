package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SelectCategoriesService {
    @FormUrlEncoded
    @POST("categories/select.php")
    Call<String> getCategories(
            @Field("shop_id") String shop_id
    );
}
