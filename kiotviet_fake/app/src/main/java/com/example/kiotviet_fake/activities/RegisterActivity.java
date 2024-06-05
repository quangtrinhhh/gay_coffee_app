package com.example.kiotviet_fake.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.UserService;
import com.example.kiotviet_fake.database.inserUser.UserInsert;
import com.example.kiotviet_fake.database.inserUser.UserInsertAPIClient;
import com.example.kiotviet_fake.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    TextView DangKyTaiKhoan, txtFocusTenGianHang, txtFocusTenDangNhap, txtFocusMatKhau;
    ImageView GoBack;
    EditText nameRestaurant, userName, password;
    LinearLayout txtErrorTenGianHang, txtErrorTenDangNhap, txtErrorMatKhau;
    Button btnDangKy;
    ProgressBar progressBar;

    String checkNameShop, checkUserName;

    boolean isNameRestaurant = true;
    boolean isUserName = true;
    boolean isPassword = true;

    boolean isNameRestaurantSuccess = false;
    boolean isUserNameSuccess = false;
    boolean isPasswordSuccess = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Đăng ký");
        GoBack = (ImageView) findViewById(R.id.GoBack);
        nameRestaurant = (EditText) findViewById(R.id.TenGianHang);
        userName = (EditText) findViewById(R.id.TenDangNhap);
        password = (EditText) findViewById(R.id.MatKhau);
        btnDangKy = (Button) findViewById(R.id.buttonDangKy);
        txtFocusTenGianHang = findViewById(R.id.txtFocusTenGianHang);
        txtFocusTenDangNhap = findViewById(R.id.txtFocusTenDangNhap);
        txtFocusMatKhau = findViewById(R.id.txtFocusMatKhau);
        txtErrorTenGianHang = findViewById(R.id.txtErrorTenGianHang);
        txtErrorTenDangNhap = findViewById(R.id.txtErrorTenDangNhap);
        txtErrorMatKhau = findViewById(R.id.txtErrorMatKhau);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnClick();
        focusItem();
        isLoginFormValid();
    }

    private void submitSuccess() {
        if (isNameRestaurantSuccess && isUserNameSuccess && isPasswordSuccess) {
            btnDangKy.setBackgroundResource(R.drawable.bg_submit_login_success);
        } else {
            btnDangKy.setBackgroundResource(R.drawable.bg_submit_login);
        }
    }

    private void isLoginFormValid() {
        nameRestaurant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    nameRestaurant.setBackgroundResource(R.drawable.radius_input_active_error);
                    txtFocusTenGianHang.setTextColor(Color.parseColor("#FF0000"));
                    txtErrorTenGianHang.setVisibility(View.VISIBLE);

                    isNameRestaurant = false;
                    isNameRestaurantSuccess = false;
                    submitSuccess();
                } else {
                    nameRestaurant.setBackgroundResource(R.drawable.radius_input_active);
                    txtFocusTenGianHang.setTextColor(Color.parseColor("#3CA0F8"));
                    txtErrorTenGianHang.setVisibility(View.GONE);

                    isNameRestaurant = true;
                    isNameRestaurantSuccess = true;
                    submitSuccess();
                }
            }
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    userName.setBackgroundResource(R.drawable.radius_input_active_error);
                    txtFocusTenDangNhap.setTextColor(Color.parseColor("#FF0000"));
                    txtErrorTenDangNhap.setVisibility(View.VISIBLE);

                    isUserName = false;
                    isUserNameSuccess = false;
                    submitSuccess();
                } else {
                    userName.setBackgroundResource(R.drawable.radius_input_active);
                    txtFocusTenDangNhap.setTextColor(Color.parseColor("#3CA0F8"));
                    txtErrorTenDangNhap.setVisibility(View.GONE);

                    isUserName = true;
                    isUserNameSuccess = true;
                    submitSuccess();
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    password.setBackgroundResource(R.drawable.radius_input_active_error);
                    txtFocusMatKhau.setTextColor(Color.parseColor("#FF0000"));
                    txtErrorMatKhau.setVisibility(View.VISIBLE);

                    isPassword = false;
                    isPasswordSuccess = false;
                    submitSuccess();
                } else {
                    password.setBackgroundResource(R.drawable.radius_input_active);
                    txtFocusMatKhau.setTextColor(Color.parseColor("#3CA0F8"));
                    txtErrorMatKhau.setVisibility(View.GONE);

                    isPassword = true;
                    isPasswordSuccess = true;
                    submitSuccess();
                }
            }
        });
    }

    private void focusItem() {
        nameRestaurant.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Thay đổi background khi EditText được focus
                    if (isNameRestaurant) {
                        nameRestaurant.setBackgroundResource(R.drawable.radius_input_active);
                        txtFocusTenGianHang.setTextColor(Color.parseColor("#3CA0F8"));
                    } else {
                        nameRestaurant.setBackgroundResource(R.drawable.radius_input_active_error);
                        txtFocusTenGianHang.setTextColor(Color.parseColor("#FF0000"));
                    }
                    nameRestaurant.setHint("");
                    txtFocusTenGianHang.setVisibility(View.VISIBLE);
                } else {
                    // Thay đổi background khi EditText mất focus
                    nameRestaurant.setBackgroundResource(R.drawable.radius_input);
                    nameRestaurant.setHint("Tên gian hàng");
                    if (TextUtils.isEmpty(nameRestaurant.getText())) {
                        txtFocusTenGianHang.setVisibility(View.GONE);
                        if (!isNameRestaurant) {
                            nameRestaurant.setBackgroundResource(R.drawable.radius_input_error);
                        }
                    } else {
                        txtFocusTenGianHang.setTextColor(Color.parseColor("#C5C5C5"));
                    }
                }
            }
        });
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Thay đổi background khi EditText được focus
                    if (isUserName) {
                        userName.setBackgroundResource(R.drawable.radius_input_active);
                        txtFocusTenDangNhap.setTextColor(Color.parseColor("#3CA0F8"));
                    } else {
                        userName.setBackgroundResource(R.drawable.radius_input_active_error);
                        txtFocusTenDangNhap.setTextColor(Color.parseColor("#FF0000"));
                    }
                    userName.setHint("");
                    txtFocusTenDangNhap.setVisibility(View.VISIBLE);
                } else {
                    // Thay đổi background khi EditText mất focus
                    userName.setBackgroundResource(R.drawable.radius_input);
                    userName.setHint("Tên đăng nhập");
                    if (TextUtils.isEmpty(userName.getText())) {
                        txtFocusTenDangNhap.setVisibility(View.GONE);
                        if (!isUserName) {
                            userName.setBackgroundResource(R.drawable.radius_input_error);
                        }
                    } else {
                        txtFocusTenDangNhap.setTextColor(Color.parseColor("#C5C5C5"));
                    }
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Thay đổi background khi EditText được focus
                    if (isPassword) {
                        password.setBackgroundResource(R.drawable.radius_input_active);
                        txtFocusMatKhau.setTextColor(Color.parseColor("#3CA0F8"));
                    } else {
                        password.setBackgroundResource(R.drawable.radius_input_active_error);
                        txtFocusMatKhau.setTextColor(Color.parseColor("#FF0000"));
                    }
                    password.setHint("");
                    txtFocusMatKhau.setVisibility(View.VISIBLE);
                } else {
                    // Thay đổi background khi EditText mất focus
                    password.setBackgroundResource(R.drawable.radius_input);
                    password.setHint("Mật khẩu");
                    if (TextUtils.isEmpty(password.getText())) {
                        txtFocusMatKhau.setVisibility(View.GONE);
                        if (!isPassword) {
                            password.setBackgroundResource(R.drawable.radius_input_error);
                        }
                    } else {
                        txtFocusMatKhau.setTextColor(Color.parseColor("#C5C5C5"));
                    }
                }
            }
        });
    }

    private void btnClick() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameRestaurantSuccess && isUserNameSuccess && isPasswordSuccess) {
                    checkUser();
                }
            }
        });

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {
        String tenGianHangText = nameRestaurant.getText().toString().trim();
        String tenDangNhapText = userName.getText().toString().trim();
        String matKhauText = password.getText().toString().trim();

        if (!tenGianHangText.isEmpty() && !tenDangNhapText.isEmpty() && !matKhauText.isEmpty()) {
            UserInsert service = UserInsertAPIClient.createService("11177575", "60-dayfreetrial");
            Call<String> call = service.insertUser(tenGianHangText, tenDangNhapText, matKhauText);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        // Đăng ký thành công, chuyển hướng đến màn hình đăng nhập
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        // Xử lý lỗi khi đăng ký không thành công
                        Toast.makeText(RegisterActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Xử lý lỗi khi gọi API thất bại
                    Toast.makeText(RegisterActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Hiển thị thông báo cho người dùng nếu có trường nào đó không được điền
            Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUser() {
        UserService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(UserService.class);
        Call<String> call = apiService.getUsers();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        boolean isNameShopExist = false;
                        boolean isUserNameExist = false;
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject userObject = jsonArray.getJSONObject(i);
                            String nameShopAPI = userObject.getString("name");
                            String userNameAPI = userObject.getString("user_name");

                            // Kiểm tra xem tên gian hàng đã tồn tại chưa
                            if (nameShopAPI.equals(nameRestaurant.getText().toString().trim())) {
                                isNameShopExist = true;
                            }

                            // Kiểm tra xem tên tài khoản đã tồn tại chưa
                            if (userNameAPI.equals(userName.getText().toString().trim())) {
                                isUserNameExist = true;
                            }
                        }

                        // Kiểm tra kết quả
                        if (isNameShopExist || isUserNameExist) {
                            Toast.makeText(RegisterActivity.this, "Tên gian hàng hoặc tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            // thêm hiệu ứng loading
                            progressBar.setVisibility(View.VISIBLE);
                            // Tiến hành đăng ký
                            registerUser();
                        }

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
