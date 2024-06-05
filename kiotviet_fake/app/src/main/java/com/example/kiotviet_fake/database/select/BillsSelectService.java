package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BillsSelectService {
    @FormUrlEncoded
    @POST("bills/select.php")
    Call<String> getBills(
            @Field("shop_id") String shop_id
    );
}


