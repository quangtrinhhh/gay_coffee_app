package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminNhanVienUpdateAndInsertActivity extends AppCompatActivity {
    EditText txtUserName, txtPassword, txtRole;
    TextView btnLuu, txtTitle;
    ImageView btnCancel;

    String userName, password, role, checkFlat,isShopId;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_nhan_vien_update_and_insert);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isShopId = sharedPreferences.getString("shop_id", "");

        addControl();
        updateUI();
        btnClick();
    }

    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (checkFlat) {
                    case "update":
                        updateUser();
                        Toast.makeText(AdminNhanVienUpdateAndInsertActivity.this, "Đã cập nhật", Toast.LENGTH_LONG).show();
                        break;
                    case "add":
                        insertUser();
                        Toast.makeText(AdminNhanVienUpdateAndInsertActivity.this, "Đã thêm", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });

        txtRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectRoleDialog();
            }
        });
    }

    private void updateUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        userName = intent.getStringExtra("userName");
        password = intent.getStringExtra("password");
        role = intent.getStringExtra("role");
        checkFlat = intent.getStringExtra("checkFlat");

        switch (checkFlat) {
            case "update":
                txtUserName.setText(userName);
                txtPassword.setText(password);
                txtRole.setText(role);
                txtTitle.setText("Cập nhật nhân viên");
                break;
            case "add":
                txtTitle.setText("Thêm nhân viên");
                break;
            default:
                break;
        }

    }

    private void addControl() {
        txtUserName = (EditText) findViewById(R.id.et_userName);
        txtPassword = (EditText) findViewById(R.id.et_password);
        txtRole = (EditText) findViewById(R.id.et_role);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        btnLuu = (TextView) findViewById(R.id.btnLuu);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
    }

    private void updateUser() {
        //select data from api
        UserService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(UserService.class);
        Call<String> call = apiService.updateUser(id, txtUserName.getText().toString(), txtPassword.getText().toString(), txtRole.getText().toString());
        Log.d("TAG", "updateUser: " + id + "-" + txtUserName.getText().toString() + "-" + txtPassword.getText().toString() + "_" + txtRole.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(AdminNhanVienUpdateAndInsertActivity.this, AdminNhanVienDetailActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("userName",txtUserName.getText().toString());
                    intent.putExtra("password",txtPassword.getText().toString());
                    intent.putExtra("role",txtRole.getText().toString());
                    startActivity(intent);
                    runFinishNhanVien();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    private void insertUser() {
        //select data from api
        UserService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(UserService.class);
        Call<String> call = apiService.insertUserNhanVien(txtUserName.getText().toString(), txtPassword.getText().toString(), txtRole.getText().toString(),isShopId);
        Log.d("TAG", "updateUser: " + id + "-" + txtUserName.getText().toString() + "-" + txtPassword.getText().toString() + "_" + txtRole.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    runInitViewNhanVien();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    public void openSelectRoleDialog() {
        Dialog dialog = new Dialog(AdminNhanVienUpdateAndInsertActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_role);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CheckBox checkOrder = dialog.findViewById(R.id.cb_order);
        CheckBox checkThuNgan = dialog.findViewById(R.id.cb_thuNgan);
        dialog.show();

        checkOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtRole.setText(checkOrder.getText().toString().toLowerCase());
                    dialog.dismiss();
                }
            }
        });
        checkThuNgan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtRole.setText(checkThuNgan.getText().toString().toLowerCase());
                    dialog.dismiss();
                }
            }
        });

    }
    private void runInitViewNhanVien() {
        Intent intent = new Intent("RUN_INIT_VIEW_NHAN_VIEN");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private void runFinishNhanVien() {
        Intent intent = new Intent("FINISH_ACTIVITY_NHAN_VIEN_DETAIL");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}