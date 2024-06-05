package com.example.kiotviet_fake.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.HangHoaAdapter;
import com.example.kiotviet_fake.adapters.PhongBanAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.BillItemsSelectByUserIdService;
import com.example.kiotviet_fake.database.select.BillsSelectByUserIdService;
import com.example.kiotviet_fake.models.Bill_hang_hoa;
import com.example.kiotviet_fake.models.Bill_phong_ban;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPhongBan extends Fragment {
    TextView txtTitleNull, txtQuantityTong, txtTotalPriceTong, txtQuantityProductNgoiTaiBan, txtTotalPriceNgoiTaiBan, txtQuantityProductMangVe, txtTotalPriceMangVe;
LinearLayout ln_isNull;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phong_ban_end_of_day_report, container, false);

        txtTitleNull = (TextView) view.findViewById(R.id.tv_titleNull);
        txtQuantityTong = view.findViewById(R.id.tv_quantityTong);
        txtTotalPriceTong = view.findViewById(R.id.tv_totalPriceTong);
        txtQuantityProductNgoiTaiBan = view.findViewById(R.id.tv_quantityProductNgoiTaiBan);
        txtTotalPriceNgoiTaiBan = view.findViewById(R.id.tv_totalPriceNgoiTaiBan);
        txtQuantityProductMangVe = view.findViewById(R.id.tv_quantityProductMangVe);
        txtTotalPriceMangVe = view.findViewById(R.id.tv_totalPriceMangVe);
        ln_isNull = view.findViewById(R.id.ln_isNull);

        return view;
    }

    public void initView(String selectedDate, String isShopId) {
        final int[] total_priceTong = {0};
        final int[] total_quantityTong = {0};
        final int[] total_quantityMangVe = {0};
        final float[] total_priceMangVe = {0};
        //select data from api
        BillsSelectByUserIdService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(BillsSelectByUserIdService.class);
        Call<String> call = apiService.getBills(isShopId, selectedDate);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<Bill_phong_ban> arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String quantity = jsonObject.getString("quantity");
                            String name = jsonObject.getString("name");
                            String date = jsonObject.getString("dateTime_end");
                            float totalPrice = Integer.parseInt(jsonObject.getString("total_price"));

                            NumberFormat formatterNumberFormat = NumberFormat.getInstance(Locale.getDefault());
                            String formatPrice = formatterNumberFormat.format(totalPrice);

                            total_priceTong[0] += totalPrice;
                            total_quantityTong[0] += Integer.parseInt(quantity);
                            if (name.toLowerCase().contains("mang")) {
                                total_priceMangVe[0] = totalPrice;
                                txtQuantityProductMangVe.setText(quantity + " đơn");
                                txtTotalPriceMangVe.setText(formatPrice);
                                total_quantityMangVe[0] = Integer.parseInt(quantity);
                            }
                            arrayList.add(new Bill_phong_ban(name, quantity, formatPrice));

                        }
                        if (arrayList.isEmpty()) {
                            txtTitleNull.setVisibility(View.VISIBLE);
                            ln_isNull.setVisibility(View.GONE);
                        } else {
                            txtTitleNull.setVisibility(View.GONE);
                            ln_isNull.setVisibility(View.VISIBLE);

                            NumberFormat formatterNumberFormat = NumberFormat.getInstance(Locale.getDefault());
                            String formatPrice = formatterNumberFormat.format(total_priceTong[0]);

                            txtQuantityProductNgoiTaiBan.setText(total_quantityTong[0] - total_quantityMangVe[0] + " đơn");
                            txtTotalPriceNgoiTaiBan.setText(formatterNumberFormat.format(total_priceTong[0] - total_priceMangVe[0]));
                            txtTotalPriceTong.setText(formatPrice);
                            txtQuantityTong.setText(total_quantityTong[0] + " đơn");
                            arrayList.add(0, new Bill_phong_ban("Tổng", String.valueOf(total_quantityTong[0] - total_quantityMangVe[0]), formatterNumberFormat.format(total_priceTong[0] - total_priceMangVe[0])));
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
                        PhongBanAdapter phongBanAdapter = new PhongBanAdapter(arrayList, requireContext()); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(phongBanAdapter);
                        phongBanAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView

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
