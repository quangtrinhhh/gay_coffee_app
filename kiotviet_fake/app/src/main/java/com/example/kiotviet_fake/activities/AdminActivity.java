package com.example.kiotviet_fake.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ViewPagerAdminAdapter;
import com.example.kiotviet_fake.workers.ApiWorker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Chạy api khi AdminActivity được truy cập
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ApiWorker.class).build();
        WorkManager.getInstance(this).enqueue(request);

        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdminAdapter adapter = new ViewPagerAdminAdapter(this);
        viewPager.setAdapter(adapter);

        // Đặt số trang được giữ
        viewPager.setOffscreenPageLimit(1); // Số trang được giữ: 1

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_tongQuan).setChecked(true);
                } else if (position == 1) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_hoaDon).setChecked(true);
                } else if (position == 2) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_hangHoa).setChecked(true);
                } else if (position == 3) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_thongBao).setChecked(true);
                } else if (position == 4) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_nhieuHon).setChecked(true);
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_tongQuan) {
                    viewPager.setCurrentItem(0, false);  // Chuyển ngay lập tức tới trang 0
                    return true;
                } else if (item.getItemId() == R.id.menu_hoaDon) {
                    viewPager.setCurrentItem(1, false);  // Chuyển ngay lập tức tới trang 1
                    return true;
                } else if (item.getItemId() == R.id.menu_hangHoa) {
                    viewPager.setCurrentItem(2, false);  // Chuyển ngay lập tức tới trang 2
                    return true;
                } else if (item.getItemId() == R.id.menu_thongBao) {
                    viewPager.setCurrentItem(3, false);  // Chuyển ngay lập tức tới trang 3
                    return true;
                } else if (item.getItemId() == R.id.menu_nhieuHon) {
                    viewPager.setCurrentItem(4, false);  // Chuyển ngay lập tức tới trang 4
                    return true;
                }
                return false;
            }
        });
    }
}
