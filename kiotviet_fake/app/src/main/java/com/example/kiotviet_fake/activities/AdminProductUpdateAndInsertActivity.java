package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.insertProduct.ProductInsert;
import com.example.kiotviet_fake.database.insertProduct.ProductInsertAPIClient;
import com.example.kiotviet_fake.database.updateCategory.UpdateCategoryByIdAPI;
import com.example.kiotviet_fake.database.updateCategory.UpdateCategoryByIdService;
import com.example.kiotviet_fake.database.updateProduct.UpdateProductAPI;
import com.example.kiotviet_fake.database.updateProduct.UpdateProductService;
import com.example.kiotviet_fake.models.Category;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProductUpdateAndInsertActivity extends AppCompatActivity {
    EditText txtNameProduct, txtMaHang, txtLoaiHang, txtGiaBan;
    TextView txtTitle, btnLuu;
    ImageView btnCancel, setImage;

    int id, categories_id;
    String name, categories_name, product_code, price, checkFlat;

    // tranh quay lại activity củ
    private BroadcastReceiver closeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
            Log.d("TAG", "onReceive: mong la dc ");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_update_and_insert);

        addControl();
        updateUI();
        btnClick();

        // tranh quay lại activity củ
        // Đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(closeReceiver, new IntentFilter("CLOSE_ADMIN_ACTIVITY"));
    }

    @Override
    protected void onDestroy() {
        // Hủy đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(closeReceiver);
        super.onDestroy();
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
                        updateProduct("11177575", "60-dayfreetrial");
                        break;
                    case "add":
                        insertProduct("11177575", "60-dayfreetrial");
                        break;
                    default:
                        Toast.makeText(AdminProductUpdateAndInsertActivity.this, "not key", Toast.LENGTH_LONG).show();
                }
            }
        });
        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminProductUpdateAndInsertActivity.this, "Chức năng đang được cập nhật", Toast.LENGTH_LONG).show();
            }
        });

        txtLoaiHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txtNameProduct.getText().toString();
                price = txtGiaBan.getText().toString();
                Intent intent = new Intent(AdminProductUpdateAndInsertActivity.this, ChangerCategoriesActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("product_code", product_code);
                intent.putExtra("name", name);
                intent.putExtra("categories_name", categories_name);
                intent.putExtra("categories_id", categories_id);
                intent.putExtra("price", price);
                intent.putExtra("checkFlat", checkFlat);
                startActivity(intent);
            }
        });
    }

    private void updateUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        product_code = intent.getStringExtra("product_code");
        name = intent.getStringExtra("name");
        categories_name = intent.getStringExtra("categories_name");
        categories_id = intent.getIntExtra("categories_id", 0);
        price = intent.getStringExtra("price");
        checkFlat = intent.getStringExtra("checkFlat");

        switch (checkFlat) {
            case "update":
                txtTitle.setText("Sửa hàng hoá");
                break;
            case "add":
                txtTitle.setText("Thêm hàng hoá");
                break;
            default:
                Toast.makeText(AdminProductUpdateAndInsertActivity.this, "not key", Toast.LENGTH_LONG).show();
        }
        txtMaHang.setText(product_code);
        txtNameProduct.setText(name);
        txtLoaiHang.setText(categories_name);
        txtGiaBan.setText(price);
    }

    private void updateProduct(String username, String password) {
        String txtPrice = txtGiaBan.getText().toString();
        txtPrice = txtPrice.replace(".", "");

        UpdateProductService service = UpdateProductAPI.createService(username, password);
        Call<String> call = service.updateProduct(id,txtNameProduct.getText().toString(),txtPrice,200,categories_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(AdminProductUpdateAndInsertActivity.this, AdminProductDetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("product_code", product_code);
                    intent.putExtra("name", txtNameProduct.getText().toString());
                    intent.putExtra("categories_name", categories_name);
                    intent.putExtra("categories_id", categories_id);
                    intent.putExtra("price", txtGiaBan.getText().toString());
                    closeAdminActivity();
                    finish();
                    startActivity(intent);
                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void insertProduct(String username, String password) {
        String txtPrice = txtGiaBan.getText().toString();
        txtPrice = txtPrice.replace(".", "");

        ProductInsert service = ProductInsertAPIClient.createService(username, password);
        Call<String> call = service.insertProduct(txtNameProduct.getText().toString(),txtPrice,200,categories_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    runInitViewFragmentHangHoa();
                    finish();
                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void closeAdminActivity() {
        Intent intent = new Intent("CLOSE_ADMIN_PRODUCT_DETAIL_ACTIVITY");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private void runInitViewFragmentHangHoa() {
        Intent intent = new Intent("RUN_INIT_VIEW");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void addControl() {
        txtNameProduct = (EditText) findViewById(R.id.et_tenHang);
        txtMaHang = (EditText) findViewById(R.id.et_maHang);
        txtLoaiHang = (EditText) findViewById(R.id.et_loaiHang);
        txtGiaBan = (EditText) findViewById(R.id.et_giaBan);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        setImage = (ImageView) findViewById(R.id.setImage);
        btnLuu = (TextView) findViewById(R.id.btn_luu);
    }
}