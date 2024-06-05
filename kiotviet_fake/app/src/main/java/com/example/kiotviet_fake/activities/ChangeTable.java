package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.updateOrderTableById.UpdateOrderTableByIdAPI;
import com.example.kiotviet_fake.database.updateOrderTableById.UpdateOrderTableByIdService;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusApiClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusService;
import com.example.kiotviet_fake.models.Order;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeTable extends AppCompatActivity {
    ImageView btnCancel;
    LinearLayout btnChonBan, lnlSelectTable;
    CheckBox cbNgoiTaiBan, cbMangVe;
    TextView btnLuu, tvTableName;

    ProgressBar progressBar;

    String nameTable;
    int idTable;
    int orderId;
    int idTableupdate;
    int idTableMangVe;
    String nameTableMangVe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_table);

        SharedPreferences sharedPreferences = getSharedPreferences("tableMangVe", Context.MODE_PRIVATE);
        idTableMangVe = sharedPreferences.getInt("tableId", 0);
        nameTableMangVe = sharedPreferences.getString("tableName", "");

        addControl();
        updateUI();
        btnClick();
    }

    private void updateUI() {
        Intent intent = getIntent();
        nameTable = intent.getStringExtra("nameTable");
        idTable = intent.getIntExtra("idTable", 0);
        idTableupdate = intent.getIntExtra("idTableupdate", 0);
        orderId = intent.getIntExtra("orderId", 0);
        Log.d("TAG", "btnClick: " + idTableupdate + " " + orderId + " " + idTable + " " + nameTable);

        SharedPreferences sharedPreferences = getSharedPreferences("mangVe", Context.MODE_PRIVATE);
        int statusMangVe = sharedPreferences.getInt("statusMangVe", 0);
        Log.d("TAG", "updateUI: " + statusMangVe);

        if (nameTable.toLowerCase().contains("mang")) {
            cbMangVe.setChecked(true);
            cbNgoiTaiBan.setChecked(false);
            lnlSelectTable.setVisibility(View.GONE);
        } else {
            cbNgoiTaiBan.setChecked(true);
            lnlSelectTable.setVisibility(View.VISIBLE);
            tvTableName.setText(nameTable);
        }

        if (statusMangVe == 1 && !nameTable.toLowerCase().contains("mang")) {
            cbMangVe.setVisibility(View.GONE);
            // Vô hiệu hóa CheckBox
            cbNgoiTaiBan.setEnabled(false);
        }

    }


    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChonBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeTable.this, ChangeTableItems.class);
                intent.putExtra("nameTable", nameTable);
                intent.putExtra("idTable", idTable);
                intent.putExtra("orderId", orderId);
                intent.putExtra("checkFlat", "doiBan");

                startActivity(intent);
                finish();
            }
        });

        cbNgoiTaiBan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbNgoiTaiBan.isChecked()) {
                    cbMangVe.setChecked(false);
                    lnlSelectTable.setVisibility(View.VISIBLE);
                } else {
                    cbMangVe.setChecked(true);
                    lnlSelectTable.setVisibility(View.GONE);
                }
            }
        });

        cbMangVe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbMangVe.isChecked()) {
                    cbNgoiTaiBan.setChecked(false);
                    lnlSelectTable.setVisibility(View.GONE);
                    idTableupdate = idTableMangVe;
                    nameTable = nameTableMangVe;
                } else {
                    cbNgoiTaiBan.setChecked(true);
                    lnlSelectTable.setVisibility(View.VISIBLE);
                }
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idTableupdate == 0 || idTableupdate == idTable) {
                    finish();
                } else {
                    // thêm hiệu ứng loading
                    progressBar.setVisibility(View.VISIBLE);
                    updateOrderTableById("11177575", "60-dayfreetrial");
                }
            }
        });
    }

    private void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        btnChonBan = (LinearLayout) findViewById(R.id.btn_chonBan);
        lnlSelectTable = (LinearLayout) findViewById(R.id.lnl_selectTable);
        cbNgoiTaiBan = (CheckBox) findViewById(R.id.cb_NgoiTaiBan);
        cbMangVe = (CheckBox) findViewById(R.id.cb_mangVe);
        btnLuu = (TextView) findViewById(R.id.btn_luu);
        tvTableName = (TextView) findViewById(R.id.tv_tableName);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void updateOrderTableById(String username, String password) {
        UpdateOrderTableByIdService service = UpdateOrderTableByIdAPI.createService(username, password);
        Call<String> call = service.updateOrderTableById(orderId, idTableupdate);
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

    private void isUpdateStatusTable(String username, String password) {

        int id = idTable;
        double status = 0;
        float table_price = 0;

        TableUpdateStatusService service = TableUpdateStatusApiClient.createService(username, password);
        Call<String> call = service.updateData(id, status, table_price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    navigateToTableDetailActivty();
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

    private void navigateToTableDetailActivty() {
        Intent intent = new Intent(ChangeTable.this, TableDetailActivity.class);
        intent.putExtra("nameTable", nameTable);
        intent.putExtra("idTable", idTableupdate);
        intent.putExtra("finish_activity", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Không thực hiện hành động nào khi nút quay trở lại được nhấn
//        super.onBackPressed();
    }
}