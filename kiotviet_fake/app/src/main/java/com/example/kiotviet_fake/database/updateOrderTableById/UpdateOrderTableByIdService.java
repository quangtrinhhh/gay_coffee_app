package com.example.kiotviet_fake.database.updateOrderTableById;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateOrderTableByIdService {
    @FormUrlEncoded
    @POST("orders/update_order_table_by_id.php")
    Call<String> updateOrderTableById(
            @Field("id") int id,
            @Field("table_id") int table_id
    );
}
