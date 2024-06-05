package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ChangeCategoriesAdapter;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.insertBills.BillsInsertApiClient;
import com.example.kiotviet_fake.database.insertBills.BillsInsertService;
import com.example.kiotviet_fake.database.insertCategory.CategoryInsertApiClient;
import com.example.kiotviet_fake.database.insertCategory.CategoryInsertService;
import com.example.kiotviet_fake.database.select.SelectCategoriesService;
import com.example.kiotviet_fake.interface_main.AdapterChangerCategories;
import com.example.kiotviet_fake.models.Category;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.session.SessionCategories;
import com.example.kiotviet_fake.session.SessionProducts;
import com.example.kiotviet_fake.workers.ApiWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangerCategoriesActivity extends AppCompatActivity implements AdapterChangerCategories {
    RecyclerView recyclerView;
    private ChangeCategoriesAdapter changeCategoriesAdapter;

    ImageView btnCancel, btnAdd;
    ProgressBar progressBar;

    String isShopId,categories_name,checkFlat,product_code,name,price;
    int categories_length = 0,id,categories_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changer_categories);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        isShopId = sharedPreferences.getString("shop_id", "");

        // khai báo recyclerView
        recyclerView = findViewById(R.id.recycler_view); // Đảm bảo RecyclerView đã được tìm thấy trong layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(ChangerCategoriesActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        addControl();
        updateUI();
        initView();
        btnClick();
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
    }

    private void initView() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String isShopId = sharedPreferences.getString("shop_id", "");

        SelectCategoriesService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(SelectCategoriesService.class);
        Call<String> call = apiService.getCategories(isShopId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<Category> categories = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("name");
                            Category category = new Category(id, name);
                            categories.add(category);
                        }
                        categories_length = categories.size();
                        changeCategoriesAdapter = new ChangeCategoriesAdapter(categories, ChangerCategoriesActivity.this,ChangerCategoriesActivity.this,categories_name,checkFlat);
                        recyclerView.setAdapter(changeCategoriesAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("OrderProductAdapter", "API response unsuccessful: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("OrderProductAdapter", "Error fetching categories", t);
            }
        });
    }

    private void addControl() {
        btnAdd = (ImageView) findViewById(R.id.btnAdd);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void btnClick() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categories_length < 10) {
                    onDialogUpdateNameCategory();
                } else {
                    Toast.makeText(ChangerCategoriesActivity.this, "Vui lòng không tạo quá 10 loại hàng", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onDialogUpdateNameCategory() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_and_add_category);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText etNameCategory = (EditText) dialog.findViewById(R.id.et_nameCategory);
        Button btnHuy = (Button) dialog.findViewById(R.id.btn_huy);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btn_xacNhan);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.tv_title);

        // Hiển thị bàn phím sau khi dialog đã được hiển thị
        etNameCategory.postDelayed(new Runnable() {
            @Override
            public void run() {
                etNameCategory.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(etNameCategory, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }, 200); // Delay để đảm bảo UI đã sẵn sàng

        txtTitle.setText("Thêm");
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
                if (!etNameCategory.getText().toString().isEmpty()) {
                    insertCategory("11177575", "60-dayfreetrial", String.valueOf(etNameCategory.getText()).trim().toUpperCase(), isShopId);
                    dialog.dismiss();
                } else {
                    Toast.makeText(ChangerCategoriesActivity.this, "Vui lòng nhập tên loại hàng", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void insertCategory(String username, String password, String nameCategory, String isShopId) {
        CategoryInsertService service = CategoryInsertApiClient.createService(username, password);
        Call<String> call = service.insertCategory(nameCategory, isShopId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    initView();
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

    @Override
    public void updateSizeCategories(int size) {
        categories_length = size;
    }

    private void closeAdminActivity() {
        Intent intent = new Intent("CLOSE_ADMIN_ACTIVITY");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void finishActivity(String nameCategory, int idCategory) {
        Log.d("TAG", "finishActivity: "+nameCategory);
        Intent intent = new Intent(ChangerCategoriesActivity.this, AdminProductUpdateAndInsertActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("product_code", product_code);
        intent.putExtra("name", name);
        intent.putExtra("categories_name", nameCategory);
        intent.putExtra("categories_id", idCategory);
        intent.putExtra("price", price);
        intent.putExtra("checkFlat", checkFlat);
        // tranh quay lại activity củ
        closeAdminActivity();
        startActivity(intent);
        finish();
    }
}