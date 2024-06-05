package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesSelectService {
    @GET("categories/select.php")
    Call<String> getCategories();
}


