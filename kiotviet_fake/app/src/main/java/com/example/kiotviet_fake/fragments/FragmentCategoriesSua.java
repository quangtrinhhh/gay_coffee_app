package com.example.kiotviet_fake.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.database.select.ProductSelectService;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCategoriesSua extends Fragment {
    private Random random = new Random();
    public FragmentCategoriesSua() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories_sua, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void initView() {
        SessionProducts sessionProducts = SessionProducts.getInstance();
        ArrayList<Product> listProducts = sessionProducts.getProductAll();

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences categorieSharedPreferences = requireContext().getSharedPreferences("categoriesFilter", Context.MODE_PRIVATE);
        String nameCategories = categorieSharedPreferences.getString("nameCategories_7", "");

        ArrayList<Product> products = new ArrayList<>();
        for (Product listProduct : listProducts) {
            if (listProduct.getNameCategories().equals(nameCategories)) {
                Product product = new Product(listProduct.getId(), listProduct.getIdProductItem(), listProduct.getName(), listProduct.getPrice(), listProduct.getQuantity(), listProduct.getQuantityOrder(), listProduct.getIdTable(), listProduct.getNameTable(), listProduct.getIdProduct(), listProduct.getNameCategories(), listProduct.getProduct_code(),listProduct.getIdCategories());
                products.add(product);
            }
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration); // Thêm dường viền vào RecyclerView

        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
        ProductAdapter productAdapter = new ProductAdapter(products, requireContext(), null); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
        recyclerView.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView
    }
}
