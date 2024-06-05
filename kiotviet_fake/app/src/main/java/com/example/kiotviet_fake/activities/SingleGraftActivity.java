package com.example.kiotviet_fake.activities;

import static android.telephony.PhoneNumberUtils.formatNumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.updateOrderTables.GroupTableApi;
import com.example.kiotviet_fake.database.updateOrderTables.GroupTableService;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusApiClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusService;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleGraftActivity extends AppCompatActivity {
    ImageView btnCancel;
    TextView quantity_item, TotalPrice, name_Table, txtXong;
    String nameTable, priceTotal;
    LinearLayout chonBan;
    int quantityItem, idTable_new, idTable_old, order_id_new, order_id_old;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_graft);

        Intent intent = getIntent();
        quantityItem = intent.getIntExtra("quantity_product", 0);
        nameTable = intent.getStringExtra("nameTable");
        idTable_new = intent.getIntExtra("id_table", 0);
        priceTotal = intent.getStringExtra("price");
        order_id_new = intent.getIntExtra("order_id_new", 0);


        SharedPreferences sharedPreferences = getSharedPreferences("order", Context.MODE_PRIVATE);
        idTable_old = sharedPreferences.getInt("idTable_old", 0);
        order_id_old = sharedPreferences.getInt("order_id_old", 0);

        System.out.println("test id table new : " + idTable_new);
        System.out.println("test id table old : " + idTable_old);
        System.out.println("test id order_id_new : " + order_id_new);
        System.out.println("test id order_id_old : " + order_id_old);
        addControl();
        RenderView();
        BtnOnClick();
    }

    private void RenderView() {
        quantity_item.setText(String.valueOf(quantityItem));

        TotalPrice.setText(formatNumber(priceTotal));
        name_Table.setText(nameTable);
    }

    public static String formatNumber(String numberStr) {
        try {
            if (numberStr != null) {
                // Kiểm tra xem chuỗi không phải là null trước khi gọi phương thức trim()
                String trimmedNumberStr = numberStr.trim();

                if (!trimmedNumberStr.isEmpty()) {
                    // Chuyển đổi chuỗi số đã được trim thành số
                    double number = Double.parseDouble(trimmedNumberStr);

                    // Tạo một đối tượng DecimalFormat
                    DecimalFormat formatter = new DecimalFormat("#,###");

                    // Định dạng số và trả về kết quả
                    return formatter.format(number);
                } else {
                    return "0";
                }
            } else {
                return "0";
            }
        } catch (NumberFormatException e) {
            // Xử lý nếu chuỗi không phải là một số hợp lệ
            return "0";
        }
    }

    private void BtnOnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chonBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleGraftActivity.this, TableListActivity.class);
                intent.putExtra("id_table", idTable_old);
                startActivity(intent);
                finish();

            }
        });
        txtXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idTable_new != 0 && idTable_new != idTable_old) {
                    // thêm hiệu ứng loading
                    progressBar.setVisibility(View.VISIBLE);
                    GroupTablesOrder();
                } else {
                    finish();
                }
            }
        });
    }

    private void GroupTablesOrder() {
        GroupTableService service = GroupTableApi.createService("11177575", "60-dayfreetrial");
        Call<String> call = service.GroupTableService(order_id_new, order_id_old);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    isUpdateStatusTable("11177575", "60-dayfreetrial");
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

    private void navigateToTableDetailActivity() {
        Intent intent = new Intent(SingleGraftActivity.this, TableDetailActivity.class);
        intent.putExtra("nameTable", nameTable);
        intent.putExtra("idTable", idTable_new);
        Toast.makeText(SingleGraftActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    private void isUpdateStatusTable(String username, String password) {

        int id = idTable_old;
        double status = 0;
        float table_price = 0;

        TableUpdateStatusService service = TableUpdateStatusApiClient.createService(username, password);
        Call<String> call = service.updateData(id, status, table_price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    navigateToTableDetailActivity();
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

    private void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        quantity_item = (TextView) findViewById(R.id.quantity_item);
        TotalPrice = (TextView) findViewById(R.id.TotalPrice);
        chonBan = (LinearLayout) findViewById(R.id.btn_chon_ban);
        name_Table = (TextView) findViewById(R.id.nameTable);
        txtXong = (TextView) findViewById(R.id.txtXong);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }
}