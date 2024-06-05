package com.example.kiotviet_fake.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.BillsSelectService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTongHop extends Fragment {

    TextView txtTongDoanhThu, txtDoanhThu, txtDoanhThuThuan, txtDoanhThuThuan_1, txtSoHoaDon, txtDoanhThuTBD;
    int totalRevenueToday = 0;
    int countBills = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tong_hop_end_of_day_report, container, false);


        txtTongDoanhThu = (TextView) view.findViewById(R.id.txtTongDoanhThu);
        txtDoanhThu = (TextView) view.findViewById(R.id.txtDoanhThu);
        txtDoanhThuThuan = (TextView) view.findViewById(R.id.txtDoanhThuThuan);
        txtDoanhThuThuan_1 = (TextView) view.findViewById(R.id.txtDoanhThuThuan_1);
        txtSoHoaDon = (TextView) view.findViewById(R.id.txtSoHoaDon);
        txtDoanhThuTBD = (TextView) view.findViewById(R.id.txtDoanhThuTBD);

        return view;

    }

    public void initView(String selectedDate,String isShopId) {
        //reset về 0
        totalRevenueToday = 0;
        countBills = 0;

        //select data from api
        BillsSelectService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(BillsSelectService.class);
        Call<String> call = apiService.getBills(isShopId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String dateTime = jsonObject.getString("dateTime");
                            String dateTime_end = jsonObject.getString("dateTimeEnd");
                            String code = jsonObject.getString("code");
                            int table_id = jsonObject.getInt("tableId");
                            int user_id = jsonObject.getInt("userId");
                            int total_price = Integer.parseInt(jsonObject.getString("total_price"));

                            String[] parts = dateTime_end.split(" ");
                            if (parts[0].equals(selectedDate)) {
                                totalRevenueToday += total_price;
                                countBills++;
                            }
                        }
                        updateUI();
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

    public void updateUI() {
        NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
        String formattedPrice = formatter.format(totalRevenueToday);

        String trungBinhDon = "";
        if (countBills <= 0) {
            trungBinhDon = "0";
        } else {
            trungBinhDon = formatter.format(totalRevenueToday / countBills);
        }

        txtTongDoanhThu.setText(formattedPrice);
        txtDoanhThu.setText(formattedPrice);
        txtDoanhThuThuan.setText(formattedPrice);
        txtDoanhThuThuan_1.setText(formattedPrice);
        txtSoHoaDon.setText(String.valueOf(countBills));
        txtDoanhThuTBD.setText(trungBinhDon);
    }


}
