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
import com.example.kiotviet_fake.models.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTatCa extends Fragment {
    private int isTableUserId;
    private String isShopId;
    private View view;
    private TableAdapter tableAdapter;
    private ArrayList<Table> originalTables = new ArrayList<>();
    RecyclerView recyclerView;

    String search = "";

    private Handler handler;
    private Runnable runnable;

    public FragmentTatCa() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        isTableUserId = sharedPreferences.getInt("userId", 0);
        isShopId = sharedPreferences.getString("shop_id", "");
        Log.d("TAG", "idshop: " + isShopId);

        view = inflater.inflate(R.layout.fragment_tat_ca, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false);
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

                handler.postDelayed(this, 1000); // Lập lịch chạy lại sau 2 giây
            }
        };
        handler.postDelayed(runnable, 1000); // Lập lịch chạy hàm đầu tiên sau 2 giây
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        super.onPause();
        // Loại bỏ callback của handler khi Fragment bị tạm dừng
        handler.removeCallbacks(runnable);
    }

    private void initView() {
        // Select data from API
        TableSelectByUserIdService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(TableSelectByUserIdService.class);
        Call<String> call = apiService.getTable(isShopId, "");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<Table> arrayList = new ArrayList<>();
                        originalTables.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = Integer.parseInt(jsonObject.getString("id"));
                            String tableName = jsonObject.getString("table_name");
                            int status = Integer.parseInt(jsonObject.getString("status"));
                            float table_price = Float.parseFloat(jsonObject.getString("table_price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(table_price);

                            String userIdString = jsonObject.getString("user_id");
                            int userId = 0;
                            if (userIdString != null && !userIdString.equals("null") && !userIdString.isEmpty()) {
                                userId = Integer.parseInt(userIdString);
                            }

                            if (tableName.toLowerCase().contains("mang")) {
                                int statusMangVe = status;
                                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("mangVe", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("statusMangVe", statusMangVe);
                                editor.apply();
                            }

                            Table table = new Table(id, tableName, status, userId, formattedPrice);
                            arrayList.add(table);
                            originalTables.add(table);
                        }

                        // Initialize adapter
                        if (tableAdapter == null) {
                            tableAdapter = new TableAdapter(arrayList, requireContext());
                            recyclerView.setAdapter(tableAdapter);
                            Log.d("TAG", "onResponse: add");
                        } else {
                            if(search.equals("")){
                                tableAdapter.updateData(arrayList);
                                Log.d("TAG", "onResponse: update");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void performSearch(String keyword) {
        Log.d("TAG", "performSearch: " + keyword);
        search = keyword;
        keyword = removeAccents(keyword.toLowerCase());
        ArrayList<Table> searchResult = new ArrayList<>();
        if (keyword.isEmpty()) {
            searchResult.addAll(originalTables);
            Log.d("TAG", "performSearch: " + "hi");
        } else {
            for (Table table : originalTables) {
                String nameTable = removeAccents(table.getTable_name().toLowerCase());
                if (nameTable.contains(keyword)) {
                    searchResult.add(table);
                }
            }
        }
        if (tableAdapter != null) {
            tableAdapter.updateData(searchResult);
        } else {
            // Khởi tạo adapter và đặt cho recyclerView
            tableAdapter = new TableAdapter(searchResult, requireContext());
            recyclerView.setAdapter(tableAdapter);
        }
    }

    private String removeAccents(String input) {
        // Loại bỏ dấu diacritical
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAccents = pattern.matcher(nfdNormalizedString).replaceAll("");

        // Loại bỏ khoảng trắng ở đầu và cuối chuỗi
        String trimmedString = noAccents.trim();

        // Loại bỏ tất cả khoảng trắng trong chuỗi
        return trimmedString.replaceAll("\\s+", "");
    }
}
