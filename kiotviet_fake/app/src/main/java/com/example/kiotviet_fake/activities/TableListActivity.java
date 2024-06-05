package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ChangeTableItemAdapter;
import com.example.kiotviet_fake.adapters.CombineTableAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.SelectTablesService;
import com.example.kiotviet_fake.database.select.TableSelectByUserIdService;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.models.TableGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableListActivity extends AppCompatActivity {
    ImageView btnCancel, btnSearch, btnCloseSearch;
    TextView txtNameTable;
    LinearLayout inputSearch;
    EditText searchEditText;
    int isTableUserId;
    String nameTable;
    int idTable;
    int orderId;
    String id_shop;

    ArrayList<TableGroup> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isTableUserId = sharedPreferences.getInt("userId", 0);
        id_shop = sharedPreferences.getString("shop_id","");

        Intent intent = getIntent();
        nameTable = intent.getStringExtra("nameTable");
        idTable = intent.getIntExtra("id_table", 0);
        orderId = intent.getIntExtra("orderId", 0);

        contronle();
        BtnOnClick();
        SelectTable();
    }

    private void SelectTable() {
        recyclerView = findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);

        //select data from api
        SelectTablesService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(SelectTablesService.class);
        Call<String> call = apiService.getTable(id_shop);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String user_id = jsonObject.getString("user_id");
                            int id = Integer.parseInt(jsonObject.getString("table_id"));
                            String tableName = jsonObject.getString("table_name");
                            int status = Integer.parseInt(jsonObject.getString("status"));
                            String table_price = jsonObject.getString("table_price");
                            int product_quantity = jsonObject.getInt("product_quantity");
                            String order_id = jsonObject.getString("order_id");
                            System.out.println("tesst" + order_id);

                            if (status == 1 && id != idTable) {
                                System.out.println("test id table a : " + id);
                                arrayList.add(new TableGroup(user_id, id, tableName, status, table_price, product_quantity, Integer.parseInt(order_id)));
                            }

                        }
                        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
                        CombineTableAdapter combineTableAdapter = new CombineTableAdapter(arrayList, TableListActivity.this, idTable, orderId); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(combineTableAdapter);
                        Log.d("TAG", "onResponse: " + idTable);
                        combineTableAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("TAG", "Failed to fetch data: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    private void BtnOnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableListActivity.this, SingleGraftActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.requestFocus();

                // Hiển thị bàn phím
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);

                inputSearch.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                txtNameTable.setVisibility(View.GONE);

            }
        });
        btnCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");

                // Ẩn bàn phím
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

                inputSearch.setVisibility(View.GONE);
                btnSearch.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                txtNameTable.setVisibility(View.VISIBLE);

            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().toLowerCase().trim();
                ArrayList<TableGroup> filterTables = new ArrayList<>();
                searchText = removeSpecialCharacters(searchText);

                for (TableGroup tableGroup : arrayList) {
                    System.out.println("search" + removeSpecialCharacters(tableGroup.getTable_name()).toLowerCase() + searchText);
                    // Lọc dữ liệu theo tên
                    if (removeSpecialCharacters(tableGroup.getTable_name()).toLowerCase().contains(searchText)) {
                        filterTables.add(tableGroup);
                    }
                }

                // Tạo adapter mới với dữ liệu đã lọc và cập nhật ListView
                CombineTableAdapter combineTableAdapter = new CombineTableAdapter(filterTables, TableListActivity.this, idTable, orderId);
                recyclerView.setAdapter(combineTableAdapter);
            }
        });

    }

    public static String removeSpecialCharacters(String input) {
        // Chuẩn hóa chuỗi đầu vào và loại bỏ dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Chuyển đổi chữ Đ thành d
        normalized = normalized.replace("Đ", "d");

        normalized = normalized.replace(" ", ""); // Loại bỏ khoang trang

        return normalized;
    }

    private void contronle() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);
        inputSearch = (LinearLayout) findViewById(R.id.inputSearch);
        searchEditText = (EditText) findViewById(R.id.et_search);
        txtNameTable = (TextView) findViewById(R.id.txtNameTable);
        btnCloseSearch = (ImageView) findViewById(R.id.btnCloseSearch);

    }
}