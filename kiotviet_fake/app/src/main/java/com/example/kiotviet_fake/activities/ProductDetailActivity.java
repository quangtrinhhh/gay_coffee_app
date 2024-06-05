package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.updateItemOfProduct.UpdateItemOfOrderAPI;
import com.example.kiotviet_fake.database.updateItemOfProduct.UpdateItemOfOrderService;
import com.example.kiotviet_fake.session.SessionManager;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    TextView nameProduct, txtPrice, idPr, total, edtGiaBan;
    EditText edtGiamGia, quantity;
    ImageView goBack, tang, giam;
    Button btnLuu;
    int productId, productQuantity;
    String productPrice, productName, quantityStr,product_code;
    int totalPrice;
    String nameTable;
    int idTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        addControl();
        LoadData();
        UpdateUI();
        BtnOclick();
    }

    private void UpdateUI() {
        nameProduct.setText(productName);
        txtPrice.setText(productPrice);
        idPr.setText(product_code);
        edtGiaBan.setText(productPrice);
        quantity.setText(String.valueOf(productQuantity));
        updateTotalPrice(); // Cập nhật tổng giá ban đầu

        // Sự kiện khi số lượng thay đổi
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalPrice(); // Cập nhật tổng giá khi thay đổi số lượng
            }
        });

        // Sự kiện khi phần trăm giảm giá thay đổi
        edtGiamGia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalPrice(); // Cập nhật tổng giá khi thay đổi phần trăm giảm giá
            }
        });

        // Sự kiện khi bấm nút Tăng
        tang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityStr = quantity.getText().toString();
                int currentQuantity = Integer.parseInt(quantityStr);
                currentQuantity++;
                quantity.setText(String.valueOf(currentQuantity));
                updateTotalPrice(); // Cập nhật tổng giá khi tăng số lượng
            }
        });

        // Sự kiện khi bấm nút Giảm
        giam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityStr = quantity.getText().toString();
                int currentQuantity = Integer.parseInt(quantityStr);
                if (currentQuantity > 1) {
                    currentQuantity--;
                    quantity.setText(String.valueOf(currentQuantity));
                    updateTotalPrice(); // Cập nhật tổng giá khi giảm số lượng
                }
            }
        });
    }

    private void addControl() {
        nameProduct = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        idPr = findViewById(R.id.idProduct);
        edtGiamGia = findViewById(R.id.edtGiamGia);
        edtGiaBan = findViewById(R.id.EdtPrice);
        goBack = findViewById(R.id.btnCancel);
        total = findViewById(R.id.total);
        quantity = findViewById(R.id.quantity);
        tang = findViewById(R.id.tang);
        giam = findViewById(R.id.giam);
        btnLuu = findViewById(R.id.btnLuuSanPham);
    }

    private void BtnOclick() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = SessionManager.getInstance();
                sessionManager.removeBillAll();

                Intent intent = new Intent(ProductDetailActivity.this, TableDetailActivity.class);
                intent.putExtra("idTable", idTable);
                intent.putExtra("nameTable", nameTable);
                startActivity(intent);
                finish();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị từ các EditText
                String totalStr = total.getText().toString();
                totalStr = totalStr.replace(".", "");
                String quantityStr = quantity.getText().toString();


                System.out.println("test value " + totalStr);
                System.out.println("test" + quantityStr);
                // Kiểm tra giá trị hợp lệ
                if (!totalStr.isEmpty() && !quantityStr.isEmpty()) {
                    // Chuyển đổi giá trị sang kiểu dữ liệu float và int

                    // Gọi service Retrofit để cập nhật thông tin sản phẩm
                    UpdateItemOfOrderService service = UpdateItemOfOrderAPI.createService("11177575", "60-dayfreetrial");
                    Call<String> call = service.updateItemOfOrder(productId, totalStr, quantityStr);

                    // Thực hiện request và xử lý response
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                // Xử lý response thành công
                                Toast.makeText(ProductDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                Log.d("update", "Thành công: " + productId);

                                SessionManager sessionManager = SessionManager.getInstance();
                                sessionManager.removeBillAll();

                                Intent intent = new Intent(ProductDetailActivity.this, TableDetailActivity.class);
                                intent.putExtra("idTable", idTable);
                                intent.putExtra("nameTable", nameTable);
                                startActivity(intent);
                                finish();


                            } else {
                                // Xử lý response không thành công
                                Toast.makeText(ProductDetailActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();

                                Log.e("update", "Thất bại: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            // Xử lý lỗi
                            Toast.makeText(ProductDetailActivity.this, "Cập nhật thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("update", "Lỗi: " + t.getMessage());
                        }
                    });
                } else {
                    // Thông báo lỗi nếu giá trị trống
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng nhập giá trị", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoadData() {
        Intent intent = getIntent();
        productId = intent.getIntExtra("product_id", 0);
        productName = intent.getStringExtra("product_name");
        productPrice = intent.getStringExtra("product_price");
        productQuantity = intent.getIntExtra("product_quantity", 0);
        nameTable = intent.getStringExtra("nameTable");
        idTable = intent.getIntExtra("idTable", 0);
        product_code = intent.getStringExtra("product_code");

        totalPrice = Integer.parseInt(productPrice.replace(".", ""));
    }

    // Phương thức cập nhật tổng giá dựa trên số lượng, giá sản phẩm và phần trăm giảm giá
    private void updateTotalPrice() {
        quantityStr = quantity.getText().toString();
        int quantityValue = Integer.parseInt(quantityStr);
        float totalBeforeDiscount = totalPrice * quantityValue;
        String discountStr = edtGiamGia.getText().toString();
        if (!discountStr.isEmpty()) {
            float discountPercent = Float.parseFloat(discountStr);
            float discountAmount = totalBeforeDiscount * (discountPercent / 100);
            float totalAfterDiscount = totalBeforeDiscount - discountAmount;
            total.setText(formatPrice(totalAfterDiscount));
        } else {
            total.setText(formatPrice(totalBeforeDiscount));
        }
    }

    // Phương thức định dạng giá tiền với dấu phẩy
    private String formatPrice(float price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(price);
    }

    @Override
    public void onBackPressed() {
        // Không thực hiện hành động nào khi nút quay trở lại được nhấn
//        super.onBackPressed();
    }
}
