package com.example.kiotviet_fake.database.deleteOrderItems;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderDeleteItemsService {
    @FormUrlEncoded
    @POST("order_items/delete_by_orderId.php")
    Call<String> deleteOrderItem(
            @Field("id") int id
    );
}