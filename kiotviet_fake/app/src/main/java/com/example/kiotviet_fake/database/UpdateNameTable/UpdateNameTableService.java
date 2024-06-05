package com.example.kiotviet_fake.database.UpdateNameTable;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateNameTableService {
    @FormUrlEncoded
    @POST("tables/update_name_table.php")
    Call<String> updateNameTable(
            @Field("table_name") String table_name,
            @Field("table_id") String table_id
    );
}
