package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.NhanVienAdminAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.UserService;
import com.example.kiotviet_fake.models.Category;
import com.example.kiotviet_fake.models.NhanVien;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminNhanVienDetailActivity extends AppCompatActivity {
    TextView txtUserName, txtPassword, txtRole;
    ImageView btnCancel, btnUpdate,btnDelete;

    String userName, password, role;
    int id,userId;

    // tránh quay lại chính nó
    private BroadcastReceiver closeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_nhan_vien_detail);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", 0);

        // Đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(closeReceiver, new IntentFilter("FINISH_ACTIVITY_NHAN_VIEN_DETAIL"));

        addControl();
        updateUI();
        btnClick();
    }

    @Override
    public void onDestroy() {
        // Hủy đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(closeReceiver);
        super.onDestroy();
    }

    private void addControl() {
        txtUserName = (TextView) findViewById(R.id.tv_userName);
        txtPassword = (TextView) findViewById(R.id.tv_password);
        txtRole = (TextView) findViewById(R.id.tv_role);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        btnUpdate = (ImageView) findViewById(R.id.btnUpdate);
        btnDelete = (ImageView) findViewById(R.id.btnDelete);
    }

    private void updateUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        userName = intent.getStringExtra("userName");
        password = intent.getStringExtra("password");
        role = intent.getStringExtra("role");

        txtUserName.setText(userName);
        txtPassword.setText(password);
        txtRole.setText(role);
    }
    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runInitViewNhanVien();
                finish();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminNhanVienDetailActivity.this, AdminNhanVienUpdateAndInsertActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("userName",userName);
                intent.putExtra("password",password);
                intent.putExtra("role",role);
                intent.putExtra("checkFlat", "update");
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotificationDialog();
            }
        });
    }

    public void openNotificationDialog() {
        Dialog dialog = new Dialog(AdminNhanVienDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtContent = (TextView) dialog.findViewById(R.id.tv_content);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.tv_title);
        Button btnHuy = (Button) dialog.findViewById(R.id.btn_huy);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btn_xacNhan);

        txtContent.setText("Dữ liệu có thể bị thay đổi, bạn nhận xoá nhân viên - " + userName);
        txtTitle.setVisibility(View.GONE);

        dialog.show();

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
                dialog.dismiss();
            }
        });


    }
    private void deleteUser(){
        //select data from api
        UserService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(UserService.class);
        Call<String> call = apiService.deleteUser(id,userId);
        Log.d("TAG", "deleteUser: "+id +"-"+userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    runInitViewNhanVien();
                    finish();
                    Toast.makeText(AdminNhanVienDetailActivity.this, "Đã thêm", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "Failed to fetch data: " + t.getMessage());
            }
        });
    }
    private void runInitViewNhanVien() {
        Intent intent = new Intent("RUN_INIT_VIEW_NHAN_VIEN");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}