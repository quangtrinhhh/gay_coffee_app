package com.example.kiotviet_fake.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.ProductSelectService;
import com.example.kiotviet_fake.database.select.SelectCategoriesService;
import com.example.kiotviet_fake.models.Category;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionCategories;
import com.example.kiotviet_fake.session.SessionProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiWorker extends Worker {
    private SessionCategories sessionCategories;
    private SessionProducts sessionProducts;

    public ApiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        callCategoriesApi();
        callProductsApi();
        Log.d("TAG", "doWork: da chay");
        // Trả về trạng thái success tạm thời, API call sẽ chạy trong nền
        return Result.success();
    }

    private void callProductsApi() {
        sessionProducts = SessionProducts.getInstance();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String isShopId = sharedPreferences.getString("shop_id", "");

        //select data from api
        ProductSelectService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(ProductSelectService.class);
        Call<String> call = apiService.getProducts(isShopId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        sessionProducts.removeProductAll();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("product_name");
                            float price = Float.parseFloat(jsonObject.getString("price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(price);

                            int quantity = jsonObject.getInt("quantity");
                            String categoriesName = jsonObject.getString("categories_name");
                            String product_code = jsonObject.getString("product_code");
                            int categories_id = jsonObject.getInt("categories_id");

                            // sửa đổi thêm điều kiện userid và idcategories = user_id split(_) userId[1]
                            String idProductItem = id + "Tất Cả";
                            Product product = new Product(id, idProductItem, name, formattedPrice, quantity, 1, 0, null, 0, categoriesName, product_code,categories_id);
                            sessionProducts.addProduct(product);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý lỗi khi không nhận được phản hồi từ API
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi khi gọi API không thành công
            }
        });
    }

    private void callCategoriesApi() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String isShopId = sharedPreferences.getString("shop_id", "");
        Log.d("TAG", "loadCategoryTitles: shop_id = " + isShopId);

        sessionCategories = new SessionCategories(getApplicationContext());

        SelectCategoriesService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(SelectCategoriesService.class);
        Call<String> call = apiService.getCategories(isShopId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        // gửi userId khi đăng nhập thành công
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("categoriesFilter", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();

                        sessionCategories.clearCategories();
                        sessionCategories.addCategory(new Category(-1, "Tất cả"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("name");
                            editor.putString("nameCategories_" + i + "", name);
                            sessionCategories.addCategory(new Category(id,name));
                        }
                        editor.apply();
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
}
