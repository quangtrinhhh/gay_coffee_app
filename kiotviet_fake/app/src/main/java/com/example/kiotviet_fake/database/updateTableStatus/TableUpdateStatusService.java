package com.example.kiotviet_fake.database.updateTableStatus;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TableUpdateStatusService {
    @FormUrlEncoded
    @POST("tables/updateStatusById.php")
    Call<String> updateData(
            @Field("id") int id,
            @Field("status") double status,
            @Field("table_price") float table_price
            // Thêm các trường và giá trị cần update tương ứng
    );
}
