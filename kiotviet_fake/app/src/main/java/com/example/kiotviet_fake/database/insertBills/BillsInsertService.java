package com.example.kiotviet_fake.database.insertBills;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BillsInsertService {
    @FormUrlEncoded
    @POST("bills/insert.php")
    Call<String> insertBills(
            @Field("dateTime") String dateTime,
            @Field("dateTime_end") String dateTimeEnd,
            @Field("code") String code,
            @Field("table_id") int tableId,
            @Field("user_id") int userId,
            @Field("total_price") float total_price
    );
}