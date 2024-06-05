package com.example.kiotviet_fake.database.InserTable;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TableInsertService {
    @FormUrlEncoded
    @POST("tables/insert_table_admin.php")
    Call<String> insertTable(
            @Field("table_name") String total_price,
            @Field("user_id") int user_id,
            @Field("shop_id") String shop_id
    );
}