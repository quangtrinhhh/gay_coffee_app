package com.example.kiotviet_fake.database;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {
    private String credentials;

    public BasicAuthInterceptor(String username, String password) {
        this.credentials = okhttp3.Credentials.basic(username, password);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request authenticatedRequest = chain.request().newBuilder()
                .header("Authorization", credentials)
                .build();
        return chain.proceed(authenticatedRequest);
    }
}