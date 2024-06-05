package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Orders_OrderItem_Product_SelectService {
    @FormUrlEncoded
    @POST("orders/selectfrom_order_orderitem_product.php")
    Call<String> getOrders(
            @Field("table_id") int table_id
    );
}
