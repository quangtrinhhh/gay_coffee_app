package com.example.kiotviet_fake.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.fragments.FragmentTatCa;
import com.example.kiotviet_fake.fragments.FragmentHome;
import com.example.kiotviet_fake.session.SessionCategories;
import com.example.kiotviet_fake.session.SessionProducts;
import com.example.kiotviet_fake.workers.ApiWorker;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView btnNotification;

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    String shopName;
    int userId;
    String infoUserName, role;
    private LinearLayout headerA, inputSearch;
    private ImageView btnSearch, btnClose;
    private EditText searchEditText;
    FragmentTatCa fragmentTatCa;
    FragmentHome framentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // chạy api khi mainActivity dc truy cập
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ApiWorker.class).build();
        WorkManager.getInstance(this).enqueue(request);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, android.R.color.black));

        // Đặt mục "Phòng Ban" làm mục được chọn
        navigationView.setCheckedItem(R.id.PhongBan);

        if (savedInstanceState == null) {
            framentHome = new FragmentHome();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, framentHome).commit();
        }

        // lấy ra role
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        shopName = sharedPreferences.getString("shopName", "");
        userId = sharedPreferences.getInt("userId", 0);
        infoUserName = sharedPreferences.getString("infoUserName", "");
        role = sharedPreferences.getString("role", "");

        Menu menu = navigationView.getMenu();
        MenuItem quanLyItem = menu.findItem(R.id.QuanLy);
        MenuItem baoCaoCuoiNgayItem = menu.findItem(R.id.BaoCaoCuoiNgay);
        switch (role) {
            case "order":
                baoCaoCuoiNgayItem.setVisible(false);
                quanLyItem.setVisible(false);
                break;
            case "thungan":
                quanLyItem.setVisible(false);
                break;
        }

        addControl();
        btnClick();
        search();

    }

    private void search() {
        headerA = findViewById(R.id.header_A);
        inputSearch = findViewById(R.id.inputSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnClose = findViewById(R.id.btnClose);
        searchEditText = findViewById(R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setVisibility(View.VISIBLE);
                headerA.setVisibility(View.GONE);
                searchEditText.setText("");
                searchEditText.requestFocus();

                // Hiển thị bàn phím
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);

                //ẩn fragmentHome
                if (framentHome != null) {
                    getSupportFragmentManager().beginTransaction().hide(framentHome).commit();
                }

                // hiển thị fragmentTatCa
                if (fragmentTatCa == null) {
                    fragmentTatCa = new FragmentTatCa();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, fragmentTatCa)
                            .commit();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setVisibility(View.GONE);
                headerA.setVisibility(View.VISIBLE);
                searchEditText.setText("");

                // Ẩn bàn phím
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

                //ẩn fragmentTatCa
                if (fragmentTatCa != null) {
                    getSupportFragmentManager().beginTransaction().hide(fragmentTatCa).commit();
                }

                // Hiển thị lại fragment home
                if (framentHome == null) {
                    framentHome = new FragmentHome(); // Tạo mới fragment home
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, framentHome, "fragment_home")
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .show(framentHome)
                            .commit();
                }
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                System.out.println("ERRRR MainActivity: " + keyword);
                if (fragmentTatCa != null) {
                    getSupportFragmentManager().beginTransaction().show(fragmentTatCa).commit();
                    fragmentTatCa.performSearch(keyword);
                } else {
                    Log.e("ERRRR MainActivity:", "FragmentTatCa is null");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addControl() {
        btnNotification = findViewById(R.id.btnNotification);
        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        TextView txtNameShop = headerView.findViewById(R.id.txtNameShop);
        TextView txtNameRole = headerView.findViewById(R.id.txtNameRole);

        if (role.equals("thungan")) {
            txtNameRole.setText("Thu ngân");
        } else {
            txtNameRole.setText(role);
        }
        txtNameShop.setText(shopName);

        // start đổi màu item menu đăng xuất

        NavigationView navigationView = findViewById(R.id.nav_view);// Lấy reference đến NavigationView
        MenuItem menuItem = navigationView.getMenu().findItem(R.id.DangXuat);// Lấy reference đến MenuItem của mục "Đăng xuất"

        // Tạo một SpannableString để thiết lập màu chữ
        SpannableString spannableString = new SpannableString(menuItem.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuItem.setTitle(spannableString);  // Thiết lập SpannableString vào MenuItem

        // end đổi màu item menu đăng xuất
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.PhongBan) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (item.getItemId() == R.id.DongBo) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.BaoCaoCuoiNgay) {
            Intent intent = new Intent(this, EndOfDayReportActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.QuanLy) {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.DangXuat) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", 0);
            editor.putString("shopName", "");
            editor.putString("role", "");
            editor.putString("shop_id", "");
            editor.apply();

            //gửi dữ liệu để lưu trữ tên đăng nhập và tên shop
            SharedPreferences sharedPreferences_infoUser = getSharedPreferences("infoUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor_infoUser = sharedPreferences_infoUser.edit();
            editor_infoUser.putString("shopName", shopName);
            editor_infoUser.putString("infoUserName", infoUserName);
            editor_infoUser.apply();

            //xoá sessionCategories và sessionProducts
            SessionCategories sessionCategories = new SessionCategories(this);
            sessionCategories.clearCategories();
            SessionProducts sessionProducts = SessionProducts.getInstance();
            sessionProducts.removeProductAll();
        }
        if (item.getItemId() == R.id.ThongBaoBep) {
            Intent intent = new Intent(MainActivity.this, Notification.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.ThietLap) {
            Toast.makeText(MainActivity.this, "Chức năng đang được cập nhật", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.HuongDan) {
            Toast.makeText(MainActivity.this, "Chức năng đang được cập nhật", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.DieuKhoan) {
            Toast.makeText(MainActivity.this, "Chức năng đang được cập nhật", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.HoTro) {
            Toast.makeText(MainActivity.this, "Chức năng đang được cập nhật", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.NgonNgu) {
            Toast.makeText(MainActivity.this, "Chức năng đang được cập nhật", Toast.LENGTH_LONG).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    public void btnClick() {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notification.class);
                startActivity(intent);
            }
        });
    }
}
