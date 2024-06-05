package com.example.kiotviet_fake.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.TableDetailActivity;
import com.example.kiotviet_fake.adapters.HangHoaAdapter;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.BillItemsSelectByUserIdService;
import com.example.kiotviet_fake.database.select.BillsSelectService;
import com.example.kiotviet_fake.database.select.Orders_OrderItem_Product_SelectService;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Bill_hang_hoa;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHangHoa extends Fragment {

    TextView txtTitleNull;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hang_hoa_end_of_day_report, container, false);

        txtTitleNull = (TextView) view.findViewById(R.id.tv_titleNull);

        return view;
    }

    public void initView(String selectedDate, String isShopId) {
        final int[] total_price = {0};
        //select data from api
        BillItemsSelectByUserIdService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(BillItemsSelectByUserIdService.class);
        Call<String> call = apiService.getBillItems(isShopId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<Bill_hang_hoa> arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int quantity = Integer.parseInt(jsonObject.getString("quantity"));
                            String name = jsonObject.getString("name");
                            String date = jsonObject.getString("dateTime_end");
                            float totalPrice = Integer.parseInt(jsonObject.getString("total_price"));

                            NumberFormat formatterNumberFormat = NumberFormat.getInstance(Locale.getDefault());
                            String formatPrice = formatterNumberFormat.format(totalPrice);

                            if (date.equals(selectedDate)) {
                                total_price[0] += totalPrice;
                                arrayList.add(new Bill_hang_hoa(name, quantity, formatPrice));
                            }

                        }
                        if (arrayList.isEmpty()) {
                            txtTitleNull.setVisibility(View.VISIBLE);
                        } else {
                            txtTitleNull.setVisibility(View.GONE);
                            NumberFormat formatterNumberFormat = NumberFormat.getInstance(Locale.getDefault());
                            String formatPrice = formatterNumberFormat.format(total_price[0]);
                            arrayList.add(0,new Bill_hang_hoa("Tổng", 0, formatPrice));
                        }

                        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
                        recyclerView.setHasFixedSize(true);
                        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
                        recyclerView.setLayoutManager(layoutManager);

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
                        dividerItemDecoration.setDrawable(drawable);
                        recyclerView.addItemDecoration(dividerItemDecoration); // Thêm dường viền vào RecyclerView

                        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
                        HangHoaAdapter hangHoaAdapter = new HangHoaAdapter(arrayList, requireContext()); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(hangHoaAdapter);
                        hangHoaAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView

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

}
