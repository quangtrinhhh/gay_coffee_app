package com.example.kiotviet_fake.database.updateOrderTables;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GroupTableService {
    @FormUrlEncoded
    @POST("tables/update_order_table.php")
    Call<String> GroupTableService(
            @Field("order_id_new") int order_id_new,
            @Field("order_id_old") int order_id_old

    );
}
