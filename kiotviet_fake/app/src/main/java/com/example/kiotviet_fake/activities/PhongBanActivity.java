package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.Adapter_Admin_Tables;
import com.example.kiotviet_fake.database.InserTable.TableInsertApiClient;
import com.example.kiotviet_fake.database.InserTable.TableInsertService;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.insertBills.BillsInsertApiClient;
import com.example.kiotviet_fake.database.insertBills.BillsInsertService;
import com.example.kiotviet_fake.database.select.AdminTableSelectService;
import com.example.kiotviet_fake.database.select.BillItemsSelectByUserIdService;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table_Admin;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhongBanActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Table_Admin> tableAdmins_ArrayList = new ArrayList<>(); // Khởi tạo ArrayList ở cấp độ lớp
    Adapter_Admin_Tables adapterAdminTables;
    String id_shop;
    int id_user;
    ImageView btnCancel,imageView ;
    TextView count_tables;
    // Khai báo biến toàn cục để lưu vị trí ban đầu khi bắt đầu kéo ImageView
    private int mLastAction;
    private int mInitialX;
    private int mInitialY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong_ban);

        btnCancel = findViewById(R.id.btnCancel);
        recyclerView = findViewById(R.id.recycler_view_table);
        imageView = findViewById(R.id.floating_image_view);
        count_tables = findViewById(R.id.count_tables);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        id_shop = sharedPreferences.getString("shop_id","");
        id_user = sharedPreferences.getInt("userId",0);
        System.out.println("test id_user : "+ id_user);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        LoadDataTables();
        ButtonOnclick();


    }

    private void ButtonOnclick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo và hiển thị Dialog hoặc AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(PhongBanActivity.this);
                builder.setTitle("Thêm bàn mới");

                // Thêm các trường nhập liệu vào Dialog
                final EditText tableNameInput = new EditText(PhongBanActivity.this);
                tableNameInput.setHint("Tên bàn");

                builder.setView(tableNameInput);

                builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lấy dữ liệu từ trường nhập liệu
                        String tableName = tableNameInput.getText().toString().trim();

                        // Thêm dữ liệu vào cơ sở dữ liệu
                        if (!tableName.isEmpty()) {
                            // Thực hiện thêm bàn vào cơ sở dữ liệu ở đây
                            addTableToDatabase(tableName,id_shop,id_user);
                        } else {
                            Toast.makeText(PhongBanActivity.this, "Vui lòng nhập tên bàn", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Hiển thị Dialog
                builder.create().show();
            }
        });
    }

    private void addTableToDatabase(String tableName, String idShop, int idUser) {
       TableInsertService service = TableInsertApiClient.createService("11177575", "60-dayfreetrial");

        Call<String> call = service.insertTable(tableName.toString().toUpperCase(),idUser,idShop);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PhongBanActivity.this, "Thêm thành công ", Toast.LENGTH_SHORT).show();
                    LoadDataTables();

                } else {
                    Toast.makeText(PhongBanActivity.this, "Thêm thất bại ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void LoadDataTables() {
        AdminTableSelectService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(AdminTableSelectService.class);
        Call<String> call = apiService.getTable_Admin(id_shop);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        tableAdmins_ArrayList.clear();
                        int countTable = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String jsonIdShop = jsonObject.getString("id_shop");
                            String id_table = jsonObject.getString("id_table");
                            String table_name = jsonObject.getString("table_name");
                            String table_price = jsonObject.getString("table_price");
                            int status = jsonObject.getInt("status");

                            countTable++;
                           System.out.println(String.format("phongban %s-%s-%s-%s-%d", jsonIdShop, id_table, table_name, table_price, status));
                            Table_Admin tableAdmin = new Table_Admin(jsonIdShop,id_table,table_name,table_price,status);

                            tableAdmins_ArrayList.add(tableAdmin);

                        }

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
                        dividerItemDecoration.setDrawable(drawable);
                        recyclerView.addItemDecoration(dividerItemDecoration);


                        adapterAdminTables = new Adapter_Admin_Tables(tableAdmins_ArrayList,PhongBanActivity.this);
                        recyclerView.setAdapter(adapterAdminTables);
                        System.out.println( "phongban B " +adapterAdminTables.getItemCount());
                        count_tables.setText(String.valueOf(countTable) + " Bàn");
                        adapterAdminTables.notifyDataSetChanged(); // Thông báo cho Adapter dữ liệu đã thay đổi
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}