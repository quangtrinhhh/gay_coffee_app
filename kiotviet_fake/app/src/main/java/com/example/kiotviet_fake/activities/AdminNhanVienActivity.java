package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.NhanVienAdminAdapter;
import com.example.kiotviet_fake.adapters.ProductAdminAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.UserService;
import com.example.kiotviet_fake.database.select.ProductSelectService;
import com.example.kiotviet_fake.fragments.FragmentAdminHangHoa;
import com.example.kiotviet_fake.models.NhanVien;
import com.example.kiotviet_fake.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminNhanVienActivity extends AppCompatActivity {
    private NhanVienAdminAdapter nhanVienAdminAdapter;
    private ArrayList<NhanVien> arrayNhanViens = new ArrayList<>();

    RecyclerView recyclerView;
    ImageView btnThem,btnCancel;

    String isShopId;

    // chạy lại initview()
    private BroadcastReceiver closeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initView();
            Log.d("TAG", "onReceive: nhan vien ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_nhan_vien);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isShopId = sharedPreferences.getString("shop_id", "");

        btnThem = findViewById(R.id.btnThem);
        btnCancel = findViewById(R.id.btnCancel);
        recyclerView = findViewById(R.id.recycler_view); // Đảm bảo RecyclerView đã được tìm thấy trong layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplication(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // Thêm đường phân chia giữa các mục trong RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(closeReceiver, new IntentFilter("RUN_INIT_VIEW_NHAN_VIEN"));

        initView();
        btnClick();
    }

    @Override
    public void onDestroy() {
        // Hủy đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(closeReceiver);
        super.onDestroy();
    }

    private void btnClick() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminNhanVienActivity.this, AdminNhanVienUpdateAndInsertActivity.class);
                intent.putExtra("checkFlat", "add");
                startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initView() {
        //select data from api
        UserService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(UserService.class);
        Call<String> call = apiService.getUsers();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<NhanVien> nhanViens = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject userObject = jsonArray.getJSONObject(i);
                            int shop_id = userObject.getInt("shop_id");
                            int user_id = userObject.getInt("user_id");
                            String restaurant_name = userObject.getString("name");
                            String usersName = userObject.getString("user_name");
                            String password = userObject.getString("password");
                            String role = userObject.getString("role");
                            Log.d("TAG", "onResponse: " + usersName);
                            if (shop_id == Integer.parseInt(isShopId) && !role.equals("admin")) {
                                NhanVien nhanVien = new NhanVien(user_id, usersName, password, role, shop_id);
                                nhanViens.add(nhanVien);
                                arrayNhanViens.add(nhanVien);
                            }

                        }
                        nhanVienAdminAdapter = new NhanVienAdminAdapter(nhanViens, AdminNhanVienActivity.this);
                        recyclerView.setAdapter(nhanVienAdminAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "Failed to fetch data: " + t.getMessage());
            }
        });

    }
}