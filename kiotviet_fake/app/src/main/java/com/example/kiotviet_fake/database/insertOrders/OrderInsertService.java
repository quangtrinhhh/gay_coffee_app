package com.example.kiotviet_fake.database.insertOrders;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderInsertService {
    @FormUrlEncoded
    @POST("orders/insert.php")
    Call<String> insertOrder(
            @Field("dateTime") String dateTime,
            @Field("code") String code,
            @Field("table_id") int tableId,
            @Field("user_id") int userId);
}