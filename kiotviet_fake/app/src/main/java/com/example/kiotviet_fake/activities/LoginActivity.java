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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.UserService;
import com.example.kiotviet_fake.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView DangKyTaiKhoan, txtFocusTenGianHang, txtFocusTenDangNhap, txtFocusMatKhau;
    EditText userName, password, nameRestaurant;
    Button btnDangNhap;
    LinearLayout txtErrorTenGianHang, txtErrorTenDangNhap, txtErrorMatKhau;
    ProgressBar progressBar;

    ArrayList<Users> userList = new ArrayList<>();

    String usersName, userspassword, restaurant_name, shop_id, user_id, role;
    int userId;
    String shopName;
    String infoUserName;

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
        setContentView(R.layout.activity_login);
        DangKyTaiKhoan = findViewById(R.id.DangKy);
        userName = findViewById(R.id.TenDangNhap);
        password = findViewById(R.id.MatKhau);
        nameRestaurant = findViewById(R.id.TenGianHang);
        btnDangNhap = findViewById(R.id.DangNhap);
        txtFocusTenGianHang = findViewById(R.id.txtFocusTenGianHang);
        txtFocusTenDangNhap = findViewById(R.id.txtFocusTenDangNhap);
        txtFocusMatKhau = findViewById(R.id.txtFocusMatKhau);
        txtErrorTenGianHang = findViewById(R.id.txtErrorTenGianHang);
        txtErrorTenDangNhap = findViewById(R.id.txtErrorTenDangNhap);
        txtErrorMatKhau = findViewById(R.id.txtErrorMatKhau);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        UserService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(UserService.class);
        Call<String> call = apiService.getUsers();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject userObject = jsonArray.getJSONObject(i);
                            shop_id = userObject.getString("shop_id");
                            user_id = userObject.getString("user_id");
                            restaurant_name = userObject.getString("name");
                            usersName = userObject.getString("user_name");
                            userspassword = userObject.getString("password");
                            role = userObject.getString("role");
                            Users user = new Users(shop_id, user_id, restaurant_name, usersName, userspassword, role);
                            userList.add(user);
                        }

                        // Sau khi đã thêm dữ liệu vào userList, thực hiện kiểm tra đăng nhập
                        performLogin();

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

        // lấy ra tên shop vừa dc truyền khi login thành công
        SharedPreferences sharedPreferences = getSharedPreferences("infoUser", Context.MODE_PRIVATE);
        String shopName = sharedPreferences.getString("shopName", "");
        String infoUserName = sharedPreferences.getString("infoUserName", "");

        Log.d("TAG", "onCreate: " + shopName.isEmpty());
        if (!shopName.isEmpty() && !infoUserName.isEmpty()) {
            userName.setText(infoUserName);
            nameRestaurant.setText(shopName);

            isNameRestaurantSuccess = true;
            isUserNameSuccess = true;

            txtFocusTenGianHang.setVisibility(View.VISIBLE);
            txtFocusTenDangNhap.setVisibility(View.VISIBLE);
        }

        btnClick();
        focusItem();
        isLoginFormValid();
    }

    private void submitSuccess() {
        if (isNameRestaurantSuccess && isUserNameSuccess && isPasswordSuccess) {
            btnDangNhap.setBackgroundResource(R.drawable.bg_submit_login_success);
        } else {
            btnDangNhap.setBackgroundResource(R.drawable.bg_submit_login);
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
        DangKyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void performLogin() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameRestaurantSuccess && isUserNameSuccess && isPasswordSuccess) {
                    String inputUserName = userName.getText().toString();
                    String inputPassword = password.getText().toString();
                    String inputRestaurant = nameRestaurant.getText().toString();
                    boolean isValidUser = false;
                    for (Users user : userList) {
                        System.out.println("user list test name " + user.getUser_name());
                        System.out.println("user list test pass " + user.getPassword());
                        System.out.println("user list test restau " + user.getShop_name());
                        if (user.getUser_name().equals(inputUserName) && user.getPassword().equals(inputPassword) && user.getShop_name().equals(inputRestaurant)) {
                            isValidUser = true;
                            userId = Integer.parseInt(user.getUser_id());
                            shopName = user.getShop_name();
                            infoUserName = user.getUser_name();
                            role = user.getRole();
                            shop_id = user.getShop_id();
                            break;
                        }
                    }

                    if (isValidUser) {
                        // thêm hiệu ứng loading
                        progressBar.setVisibility(View.VISIBLE);

                        // gửi userId khi đăng nhập thành công
                        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("userId", userId);
                        editor.putString("shopName", shopName);
                        editor.putString("shop_id", shop_id);
                        System.out.println("test id_shop login : "+ shop_id);

                        editor.putString("infoUserName", infoUserName);
                        editor.putString("role", role);
                        editor.apply();

                        switch (role) {
                            case "admin":
                                Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(adminIntent);
                                finish();
                                break;
                            case "thungan":
                            case "order":
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                break;
                            default:
                                // Xử lý logic cho trường hợp không xác định
                                break;
                        }


                    } else {
                        Toast.makeText(LoginActivity.this, "Tên người dùng hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        // lấy ra tên shop vừa dc truyền khi login thành công
//        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
//        int userId = sharedPreferences.getInt("userId", 0);
//        if (userId == 0) {
//            finishAffinity();
//        } else {
//            super.onBackPressed();
//        }
//    }
}
