package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.HomePagerAdapter;
import com.example.kiotviet_fake.adapters.NotificationPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Notification extends AppCompatActivity {
    private ViewPager pager;
    private TabLayout tabLayout;

    ImageView btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        addControl();
        btnClick();
    }

    private void addControl() {
        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);

        FragmentManager manager = getSupportFragmentManager();
        NotificationPagerAdapter adapter = new NotificationPagerAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }

    public void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Không thực hiện hành động nào khi nút quay trở lại được nhấn
//        super.onBackPressed();
    }
}