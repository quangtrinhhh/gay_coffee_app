package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetRevenue {
    @FormUrlEncoded
    @POST("bills/bill_total_revenue.php")
    Call<String> GetRevenue_Admin(
            @Field("shop_id") String id_shop
    );
}
