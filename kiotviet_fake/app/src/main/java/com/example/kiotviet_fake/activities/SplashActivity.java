package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.kiotviet_fake.R;

public class SplashActivity extends AppCompatActivity {

    int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // lấy ra role
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");

        Intent intent;
        if (role.equals("order") || role.equals("thungan")) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else if (role.equals("admin")) {
            intent = new Intent(SplashActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        // Không thực hiện hành động nào khi nút quay trở lại được nhấn
//        super.onBackPressed();
    }
}