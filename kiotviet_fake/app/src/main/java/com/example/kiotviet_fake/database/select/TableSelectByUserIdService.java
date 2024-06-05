package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TableSelectByUserIdService {
    @FormUrlEncoded
    @POST("tables/select_by_user_id.php")
    Call<String> getTable(
            @Field("shop_id") String shop_id,
            @Field("table_name") String table_name
    );
}


