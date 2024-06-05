package com.example.kiotviet_fake.database.deleteItems;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DeleteItemOfOrderService {
    @FormUrlEncoded
    @POST("order_items/delete_item_of_order.php")
    Call<String> deleteItemOfOrder(
            @Field("id") int id
    );
}
