package com.example.kiotviet_fake.database.insertOrderItems;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderInsertItemsService {
    @FormUrlEncoded
    @POST("order_items/insert.php")
    Call<String> insertOrderItem(
            @Field("quantity") int quantity,
            @Field("price") float price,
            @Field("order_id") int orderId,
            @Field("product_id") int productId
    );
}