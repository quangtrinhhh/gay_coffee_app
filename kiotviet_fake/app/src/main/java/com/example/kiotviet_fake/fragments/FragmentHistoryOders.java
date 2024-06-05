package com.example.kiotviet_fake.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.HistoryAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.HistoryService;
import com.example.kiotviet_fake.models.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHistoryOders extends Fragment {
    RecyclerView listHistory;
    HistoryAdapter historyAdapter;
    ArrayList<History> historyList;

    TextView txtTitleNull;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_oders_end_of_day_report, container, false);

        listHistory = view.findViewById(R.id.listHistory);
        historyList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyList, getContext());
        listHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        listHistory.setAdapter(historyAdapter);
        txtTitleNull = (TextView) view.findViewById(R.id.tv_titleNull);

        return view;
    }

    public void fetchData(String selectedDate, String isShopId) {
        HistoryService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(HistoryService.class);
        Call<String> call = apiService.getHistory(isShopId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        historyList.clear(); // Xóa dữ liệu cũ trước khi cập nhật dữ liệu mới
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Log.d("TAG", "onResponse: "+jsonObject.getInt("id"));
                            int id = jsonObject.getInt("id");
                            String dateTime = jsonObject.getString("dateTime");
                            String dateTime_end = jsonObject.getString("dateTime_end");
                            String code = jsonObject.getString("code");
                            int table_id = jsonObject.getInt("table_id");
                            int user_id = jsonObject.getInt("user_id");
                            double total_price = jsonObject.getDouble("total_price");
                            String nameTable = jsonObject.getString("table_name");

                            String[] date = dateTime_end.split(" ");
                            if (date[0].equals(selectedDate)) {
                                History history = new History(id, dateTime, dateTime_end, code, table_id, user_id, total_price, nameTable);
                                historyList.add(history);
                            }
                        }
                        if (historyList.isEmpty()) {
                            txtTitleNull.setVisibility(View.VISIBLE);
                        } else {
                            txtTitleNull.setVisibility(View.GONE);
                        }
                        // Cập nhật dữ liệu mới cho adapter
                        historyAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("TAG", "onResponse: erros "+e.getMessage());
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
