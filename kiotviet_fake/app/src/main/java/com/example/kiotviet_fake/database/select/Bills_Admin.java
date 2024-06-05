package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Bills_Admin {
    @FormUrlEncoded
    @POST("bills/selcet_bill_admin.php")
    Call<String> getBills_Admin(
            @Field("shop_id") String id_shop
    );
}
