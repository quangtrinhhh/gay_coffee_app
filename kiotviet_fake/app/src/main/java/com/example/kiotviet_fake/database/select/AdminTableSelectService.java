package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AdminTableSelectService {
    @FormUrlEncoded
    @POST("tables/tables_admin.php")
    Call<String> getTable_Admin(
            @Field("shop_id") String shop_id
    );
}
