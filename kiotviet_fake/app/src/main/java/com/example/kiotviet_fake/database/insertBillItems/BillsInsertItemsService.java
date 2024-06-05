package com.example.kiotviet_fake.database.insertBillItems;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BillsInsertItemsService {
    @FormUrlEncoded
    @POST("bill_items/insert.php")
    Call<String> insertBillItems(
            @Field("quantity") int quantity,
            @Field("total_price") float total_price,
            @Field("product_id") int product_id,
            @Field("product_name") String product_name,
            @Field("bill_id") int bill_id
    );
}