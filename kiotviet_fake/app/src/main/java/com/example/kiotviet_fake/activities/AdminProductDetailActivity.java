package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.deleteProduct.ProductDeleteApiClient;
import com.example.kiotviet_fake.database.deleteProduct.ProductDeleteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProductDetailActivity extends AppCompatActivity {
    TextView txtNameProduct, txtMaHang, txtLoaiHang, txtGiaBan;
    ImageView btnCancel, btnUpdate, btnDelete;
    ProgressBar progressBar;

    int id, categories_id;
    String name, categories_name, price, product_code;

    // tranh quay lại activity củ
    private BroadcastReceiver closeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);


        addControl();
        updateUI();
        btnClick();

        // tranh quay lại activity củ
        // Đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(closeReceiver, new IntentFilter("CLOSE_ADMIN_PRODUCT_DETAIL_ACTIVITY"));
    }

    @Override
    protected void onDestroy() {
        // Hủy đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(closeReceiver);
        super.onDestroy();
    }

    private void updateUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        product_code = intent.getStringExtra("product_code");
        name = intent.getStringExtra("name");
        categories_name = intent.getStringExtra("categories_name");
        categories_id = intent.getIntExtra("categories_id", 0);
        price = intent.getStringExtra("price");

        Log.d("TAG", "updateUI: " + name);
        txtMaHang.setText(product_code);
        txtNameProduct.setText(name);
        txtLoaiHang.setText(categories_name);
        txtGiaBan.setText(price);
    }

    private void addControl() {
        txtNameProduct = (TextView) findViewById(R.id.tv_nameProduct);
        txtMaHang = (TextView) findViewById(R.id.tv_maHang);
        txtLoaiHang = (TextView) findViewById(R.id.tv_loaiHang);
        txtGiaBan = (TextView) findViewById(R.id.tv_giaBan);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        btnUpdate = (ImageView) findViewById(R.id.btnUpdate);
        btnDelete = (ImageView) findViewById(R.id.btnDelete);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runInitViewFragmentHangHoa();
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProductDetailActivity.this, AdminProductUpdateAndInsertActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("product_code", product_code);
                intent.putExtra("name", name);
                intent.putExtra("categories_name", categories_name);
                intent.putExtra("categories_id", categories_id);
                intent.putExtra("price", price);
                intent.putExtra("checkFlat", "update");
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTheConfirmationDialog();
            }
        });
    }


    public void openTheConfirmationDialog() {
        Dialog dialog = new Dialog(AdminProductDetailActivity.this);
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

        txtContent.setText("Xác nhận xoá sản phẩm");
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
                progressBar.setVisibility(View.VISIBLE);
                deleteProduct("11177575", "60-dayfreetrial");
                dialog.dismiss();
            }
        });


    }

    public void deleteProduct(String username, String password) {
        ProductDeleteService service = ProductDeleteApiClient.createService(username, password);
        Call<String> call = service.deleteProduct(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    runInitViewFragmentHangHoa();
                    finish();
                } else {
                    if (response.code() == 400) {
                        // Xử lý lỗi 400
                        openNotificationDialog();
                    } else {
                        // Xử lý các mã lỗi khác nếu cần
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    public void openNotificationDialog() {
        Dialog dialog = new Dialog(AdminProductDetailActivity.this);
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

        txtContent.setText("Không thể xóa sản phẩm khi đang liên kết với bàn chưa thanh toán. Vui lòng thanh toán trước và thử lại.");
        txtTitle.setVisibility(View.GONE);
        btnHuy.setAlpha(0);

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
                dialog.dismiss();
            }
        });


    }

    private void runInitViewFragmentHangHoa() {
        Intent intent = new Intent("RUN_INIT_VIEW");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}