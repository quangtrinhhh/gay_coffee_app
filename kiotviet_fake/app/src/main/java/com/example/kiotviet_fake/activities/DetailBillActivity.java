package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.DetailBillAdapter;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.Detail_item;
import com.example.kiotviet_fake.models.DetailBill;
import com.example.kiotviet_fake.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBillActivity extends AppCompatActivity {
    RecyclerView listItemBill;
    DetailBillAdapter detailBillAdapter;
    ArrayList<DetailBill> listItemProduct;
    TextView tong, title;
    ImageView gobackk;
    int id_bill;
    double total;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);
        LoadData();
        fetchData();
        BtnOnClick();

    }

    private void BtnOnClick() {
        gobackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchData() {

        Detail_item apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(Detail_item.class);
        Call<String> call = apiService.getDetail_item(id_bill);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<DetailBill> arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int bill_item_id = jsonObject.getInt("bill_item_id");
                            int quantity = jsonObject.getInt("quantity");
                            double total_price = jsonObject.getDouble("total_price");
                            int product_id = jsonObject.getInt("product_id");
                            int bill_id = jsonObject.getInt("bill_id");
                            String product_name = jsonObject.getString("product_name");
                            arrayList.add(new DetailBill(bill_item_id, quantity, total_price, product_id, bill_id, product_name));

                        }
                        RecyclerView recyclerView = findViewById(R.id.ListItemBill); // Sử dụng getView() để lấy view được inflate từ layout
                        recyclerView.setHasFixedSize(true);
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
                        recyclerView.setLayoutManager(layoutManager);

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
                        dividerItemDecoration.setDrawable(drawable);
                        recyclerView.addItemDecoration(dividerItemDecoration); // Thêm dường viền vào RecyclerView

                        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
                        DetailBillAdapter detailBillAdapter = new DetailBillAdapter(arrayList, getApplicationContext()); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(detailBillAdapter);
                        detailBillAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView
                        // Cập nhật dữ liệu mới cho adapter
                        detailBillAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công (ví dụ: mã lỗi không phải 200)
                    Toast.makeText(DetailBillActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }


    private void LoadData() {
        Intent intent = getIntent();
        id_bill = intent.getIntExtra("bill_id", 0);
        code = intent.getStringExtra("code");
        total = intent.getDoubleExtra("total", 0);


        tong = findViewById(R.id.tongTien);
        gobackk = findViewById(R.id.goback);
        title = findViewById(R.id.tv_code);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTotal = decimalFormat.format(total);

        tong.setText(formattedTotal);
        title.setText(code);

//        listItemProduct = new ArrayList<DetailBill>();
//        detailBillAdapter = new DetailBillAdapter(listItemProduct, this);
//        listItemBill = findViewById(R.id.ListItemBill);
//        listItemBill.setAdapter(detailBillAdapter);

    }

    @Override
    public void onBackPressed() {
        // Không thực hiện hành động nào khi nút quay trở lại được nhấn
//        super.onBackPressed();
    }
}