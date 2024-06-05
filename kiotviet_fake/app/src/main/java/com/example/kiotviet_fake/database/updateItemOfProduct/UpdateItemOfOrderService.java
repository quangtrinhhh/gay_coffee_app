package com.example.kiotviet_fake.database.updateItemOfProduct;

import android.widget.TextView;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateItemOfOrderService {
    @FormUrlEncoded
    @POST("order_items/update_item_of_order.php")
    Call<String> updateItemOfOrder(
            @Field("id") int id,
            @Field("price") String price,
            @Field("quantity") String quantity
            // Thêm các trường và giá trị cần update tương ứng
    );
}
