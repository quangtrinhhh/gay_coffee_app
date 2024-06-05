package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.BillsSelectService;
import com.example.kiotviet_fake.database.select.Orders_OrderItem_Product_SelectService;
import com.example.kiotviet_fake.fragments.FragmentHangHoa;
import com.example.kiotviet_fake.fragments.FragmentHistoryOders;
import com.example.kiotviet_fake.fragments.FragmentPhongBan;
import com.example.kiotviet_fake.fragments.FragmentTongHop;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EndOfDayReportActivity extends AppCompatActivity {
    ImageView btnCancel;
    TextView txtDate, txtNameCategories;
    LinearLayout btnSetDate, btnSetBillCategories;
    String selectedDate,isShopId;
    int isUserId;

    FragmentPhongBan fragmentPhongBan;
    FragmentHangHoa fragmentHangHoa;
    FragmentTongHop fragmentTongHop;
    FragmentHistoryOders fragmentHistoryOders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_day_report);

        // lấy id tài khoan đang login
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isUserId = sharedPreferences.getInt("userId", 0);
        isShopId = sharedPreferences.getString("shop_id", "");

        Log.d("TAG", "onCreate: " + isShopId);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(currentDate);

        // Khởi tạo các fragment
        fragmentPhongBan = new FragmentPhongBan();
        fragmentHangHoa = new FragmentHangHoa();
        fragmentTongHop = new FragmentTongHop();
        fragmentHistoryOders = new FragmentHistoryOders();


        // Hiển thị fragment mặc định
        showFragment(fragmentTongHop);
        fragmentTongHop.initView(selectedDate, isShopId);

        addControl();

        //set date (UI) ngày hiện tại
        txtDate.setText(selectedDate);

        btnClick();

    }

    // Phương thức để hiển thị fragment
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment); // Loại bỏ fragment hiện tại
        transaction.replace(R.id.fragmentContainer, fragment, "CURRENT_FRAGMENT"); // Sử dụng tag "CURRENT_FRAGMENT" để xác định Fragment
        transaction.commit();
    }

    private void addControl() {
        txtNameCategories = (TextView) findViewById(R.id.tv_nameCategories);
        txtDate = (TextView) findViewById(R.id.txtDate);
        btnSetDate = (LinearLayout) findViewById(R.id.btnSetDate);
        btnSetBillCategories = (LinearLayout) findViewById(R.id.btn_setBillCategoies);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);

    }

    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetDate();
            }
        });

        btnSetBillCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSetBillCategories.setBackgroundResource(R.drawable.bg_title_detail_btn);
                openSetBillCategories();
            }
        });
    }

    public void openSetBillCategories() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menu_end_of_day_report);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Set gravity to bottom
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
        }

        dialog.show();
        TextView txtTongHop = (TextView) dialog.findViewById(R.id.tv_tongHop);
        TextView txtHangHoa = (TextView) dialog.findViewById(R.id.tv_hangHoa);
        TextView txtPhongBan = (TextView) dialog.findViewById(R.id.tv_phongBan);
        TextView txtDanSachHoaDon = (TextView) dialog.findViewById(R.id.tv_danhSachHoaDon);
        ImageView btnCancel = (ImageView) dialog.findViewById(R.id.iv_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtTongHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTongHop.initView(selectedDate, isShopId);
                txtNameCategories.setText("Tổng hợp");
                showFragment(fragmentTongHop);
                dialog.dismiss();
            }
        });
        txtHangHoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentHangHoa.initView(selectedDate, isShopId);
                txtNameCategories.setText("Hàng hoá");
                showFragment(fragmentHangHoa);
                dialog.dismiss();
            }
        });
        txtPhongBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentPhongBan.initView(selectedDate, isShopId);
                txtNameCategories.setText("Phòng bàn");
                showFragment(fragmentPhongBan);
                dialog.dismiss();
            }
        });
        txtDanSachHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentHistoryOders.fetchData(selectedDate, isShopId);
                txtNameCategories.setText("Danh sách hoá đơn");
                showFragment(fragmentHistoryOders);
                dialog.dismiss();
            }
        });
    }

    public void openSetDate() {
        Calendar calendar = Calendar.getInstance();

        // Lấy ngày, tháng và năm hiện tại
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EndOfDayReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Hiển thị ngày đã chọn
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                calendar.set(year, month, dayOfMonth);
                selectedDate = dateFormat.format(calendar.getTime());

                //set date
                txtDate.setText(selectedDate);

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentByTag("CURRENT_FRAGMENT"); // Sử dụng tag để xác định Fragment

                if (currentFragment != null) {
                    if (currentFragment instanceof FragmentTongHop) {
                        fragmentTongHop.initView(selectedDate, isShopId);
                    } else if (currentFragment instanceof FragmentHangHoa) {
                        fragmentHangHoa.initView(selectedDate, isShopId);
                    } else if (currentFragment instanceof FragmentPhongBan) {
                        fragmentPhongBan.initView(selectedDate, isShopId);
                    } else if (currentFragment instanceof FragmentHistoryOders) {
                        fragmentHistoryOders.fetchData(selectedDate, isShopId);
                    } else {

                    }
                }
            }
        }, currentYear, currentMonth, currentDay);

        // Đặt giới hạn cho DatePickerDialog, không cho phép chọn ngày trước ngày hiện tại
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Mở hộp thoại chọn ngày
        datePickerDialog.show();
    }
}