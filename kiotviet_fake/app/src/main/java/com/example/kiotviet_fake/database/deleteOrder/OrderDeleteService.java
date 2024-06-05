package com.example.kiotviet_fake.database.deleteOrder;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderDeleteService {
    @FormUrlEncoded
    @POST("orders/delete.php")
    Call<String> deleteOrder(
            @Field("id") int id
    );
}