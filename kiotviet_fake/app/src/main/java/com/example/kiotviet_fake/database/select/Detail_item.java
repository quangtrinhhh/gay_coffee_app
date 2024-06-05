package com.example.kiotviet_fake.database.select;

import com.example.kiotviet_fake.models.DetailBill;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface Detail_item {
    @FormUrlEncoded
    @POST("bill_items/detail_bill.php")
    Call<String> getDetail_item(
            @Field("bill_id_post") int bill_id_post
    );
}
