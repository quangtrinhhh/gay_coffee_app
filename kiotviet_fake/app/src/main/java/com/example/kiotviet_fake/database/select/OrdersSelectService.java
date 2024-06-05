package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OrdersSelectService {
    @GET("orders/select.php")
    Call<String> getOrders();
}
