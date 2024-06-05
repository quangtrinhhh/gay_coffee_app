package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiotviet_fake.R;

public class InformationOderActivity extends AppCompatActivity {
    ImageView btnCancel;
    TextView time_date,name_table;
    String date_time,InterntName_table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_oder);
        Intent intent =  getIntent();
        date_time =   intent.getStringExtra("dateTime");
        InterntName_table = intent.getStringExtra("nameTable");
        addControl();
        RenderView();
        BtnOnclick();
    }

    private void RenderView() {
        System.out.println("test time : " + date_time);
        time_date.setText(date_time);
        name_table.setText(InterntName_table);
    }

    private void BtnOnclick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        time_date = (TextView) findViewById(R.id.time_date);
        name_table = (TextView) findViewById(R.id.name_table);
    }
}