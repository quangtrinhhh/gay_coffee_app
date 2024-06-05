package com.example.kiotviet_fake.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.TableSelectByUserIdService;
import com.example.kiotviet_fake.database.select.TableSelectService;
import com.example.kiotviet_fake.models.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSuDung extends Fragment {
    int isTableUserId;
    String isShopId;

    private Handler handler;
    private Runnable runnable;
    private TableAdapter tableAdapter;
    private RecyclerView recyclerView;

    public FragmentSuDung() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // lấy ra userId vừa dc truyền khi login thành công
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        isTableUserId = sharedPreferences.getInt("userId", 0);
        isShopId = sharedPreferences.getString("shop_id", "");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_su_dung, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        super.onResume();
        initView();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                initView(); // Gọi lại hàm initview()
                handler.postDelayed(this, 2000); // Lập lịch chạy lại sau 2 giây
            }
        };
        handler.postDelayed(runnable, 2000); // Lập lịch chạy hàm đầu tiên sau 2 giây
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        super.onPause();
        // Loại bỏ callback của handler khi Fragment bị tạm dừng
        handler.removeCallbacks(runnable);
    }

    public void initView() {

        ArrayList<Table> arrayList = new ArrayList<>();

        //select data from api
        TableSelectByUserIdService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(TableSelectByUserIdService.class);
        Call<String> call = apiService.getTable(isShopId, "");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = Integer.parseInt(jsonObject.getString("id"));
                            String tableName = jsonObject.getString("table_name");
                            int status = Integer.parseInt(jsonObject.getString("status"));
                            float table_price = Float.parseFloat(jsonObject.getString("table_price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(table_price);

                            String userIdString = jsonObject.getString("user_id");
                            int userId = 0; // Giá trị mặc định nếu không thể chuyển đổi
                            if (userIdString != null && !userIdString.equals("null") && !userIdString.isEmpty()) {
                                userId = Integer.parseInt(userIdString);
                            }
                            if (status != 0) {
                                arrayList.add(new Table(id, tableName, status, userId, formattedPrice));
                            }
                        }
                        // Initialize adapter
                        if (tableAdapter == null) {
                            tableAdapter = new TableAdapter(arrayList, requireContext());
                            recyclerView.setAdapter(tableAdapter);
                        } else {
                            tableAdapter.updateData(arrayList);
                        }
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