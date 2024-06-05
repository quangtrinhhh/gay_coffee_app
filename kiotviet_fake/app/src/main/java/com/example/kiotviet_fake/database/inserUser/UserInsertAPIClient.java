package com.example.kiotviet_fake.database.inserUser;

import com.example.kiotviet_fake.database.BasicAuthInterceptor;
import com.example.kiotviet_fake.database.insertOrders.OrderInsertService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UserInsertAPIClient {
    private static final String BASE_URL = "http://gaycoffee-001-site1.jtempurl.com/";

    public static UserInsert createService(String username, String password) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create()) // Chú ý sử dụng ScalarsConverterFactory nếu API trả về dữ liệu kiểu String
                .build();

        return retrofit.create(UserInsert.class);
    }
}
