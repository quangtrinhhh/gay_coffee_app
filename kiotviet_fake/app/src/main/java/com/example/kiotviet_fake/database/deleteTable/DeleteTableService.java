package com.example.kiotviet_fake.database.deleteTable;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DeleteTableService {
    @FormUrlEncoded
    @POST("tables/delete.php")
    Call<String> deleteTable(
            @Field("id") String id
    );
}
