package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.TachDonAdapter;
import com.example.kiotviet_fake.adapters.XemTamTinhAdapter;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class XemTamTinhActivity extends AppCompatActivity {
    XemTamTinhAdapter xemTamTinhAdapter;
    TextView txtCode, txtNameTable, txtDateTime, txtTotalPrice, txtSale, txtPay, txtQuantityProduct;
    String nameTable, dateTime, code;
    ImageView btnCancel;
    int quantityTotal, idTable;
    float priceTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_tam_tinh);

        addControl();
        updateUI();
        btnClick();
        initView();
    }

    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateUI() {
        Intent intent = getIntent();
        nameTable = intent.getStringExtra("nameTable");
        dateTime = intent.getStringExtra("dateTime");
        code = intent.getStringExtra("code");
        quantityTotal = intent.getIntExtra("quantityTotal", 0);
        idTable = intent.getIntExtra("idTable", 0);
        priceTotal = intent.getFloatExtra("totalPrice", 0);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(priceTotal);
        formatDateTime(dateTime);

        txtNameTable.setText(nameTable);
        txtCode.setText("Xem tạm tính " + code);
        txtTotalPrice.setText(formattedNumber);
        txtPay.setText(formattedNumber);
        txtQuantityProduct.setText(quantityTotal + "");
    }

    public void formatDateTime(String dateTimeString) {
        // Định dạng đầu vào của chuỗi ngày giờ (ví dụ: "yyyy-MM-dd HH:mm:ss")
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);

        // Định dạng đầu ra mong muốn với hệ thống 24 giờ ("dd MM yyyy HH:mm")
        String outputPattern = "dd MM yyyy HH:mm";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        try {
            // Chuyển đổi chuỗi đầu vào thành đối tượng Date
            Date date = inputFormat.parse(dateTimeString);

            // Định dạng lại đối tượng Date theo định dạng đầu ra mong muốn
            String formattedDateTime = outputFormat.format(date);

            // Gán giá trị đã định dạng vào TextView
            txtDateTime.setText(formattedDateTime);
        } catch (ParseException e) {
            // Xử lý ngoại lệ nếu chuỗi đầu vào không hợp lệ
            System.err.println("Failed to parse date time string: " + dateTimeString);
            e.printStackTrace();
        }
    }


    private void addControl() {
        txtCode = (TextView) findViewById(R.id.txtCode);
        txtNameTable = (TextView) findViewById(R.id.txtNameTable);
        txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        txtSale = (TextView) findViewById(R.id.txtSale);
        txtPay = (TextView) findViewById(R.id.txtPay);
        txtQuantityProduct = (TextView) findViewById(R.id.txtQuantityProduct);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
    }

    public void initView() {
        //thêm dữ liệu vào sessionManager
        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<Bill> bills = sessionManager.getBills();
        ArrayList<Product> arrayList = new ArrayList<>();
        for (Bill bill : bills) {
            arrayList.add(new Product(bill.getIdOrderItem(), "", bill.getNameProduct(), String.valueOf(bill.getPriceProduct()), 200, bill.getQuantity(), bill.getTableId(), bill.getNameTable(), bill.getProductId(),"","",0));
        }
        RecyclerView recyclerView = findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration); // Thêm dường viền vào RecyclerView

        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
        xemTamTinhAdapter = new XemTamTinhAdapter(arrayList, getApplicationContext()); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
        recyclerView.setAdapter(xemTamTinhAdapter);
        xemTamTinhAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView

    }
}