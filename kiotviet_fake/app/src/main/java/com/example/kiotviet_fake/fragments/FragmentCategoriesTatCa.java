package com.example.kiotviet_fake.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.ProductSelectService;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.session.SessionProducts;

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

public class FragmentCategoriesTatCa extends Fragment {
    private ProductAdapter productAdapter;
    private ArrayList<Product> arrayProducts = new ArrayList<>();
    RecyclerView recyclerView;
    private View view;

    public FragmentCategoriesTatCa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categories_tat_ca, container, false);
        Log.d("TAG", "categoriesTatca: " + "da chay");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void initView() {
        SessionProducts sessionProducts = SessionProducts.getInstance();
        ArrayList<Product> listProducts = sessionProducts.getProductAll();

        recyclerView = view.findViewById(R.id.recycler_view); // Đảm bảo RecyclerView đã được tìm thấy trong layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Product> products = new ArrayList<>();
        for (Product listProduct : listProducts) {
            Product product = new Product(listProduct.getId(), listProduct.getIdProductItem(), listProduct.getName(), listProduct.getPrice(), listProduct.getQuantity(), listProduct.getQuantityOrder(), listProduct.getIdTable(), listProduct.getNameTable(), listProduct.getIdProduct(), listProduct.getNameCategories(), listProduct.getProduct_code(),listProduct.getIdCategories());
            products.add(product);
            arrayProducts.add(product);
        }

        // Tạo adapter nếu chưa có và cập nhật dữ liệu mới
        if (productAdapter != null) {
            productAdapter.updateData(products);
            productAdapter.notifyDataSetChanged();
        } else {
            productAdapter = new ProductAdapter(products, requireContext(), null);
            recyclerView.setAdapter(productAdapter);
        }

        // Thêm đường phân chia giữa các mục trong RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    // Phương thức thực hiện tìm kiếm sản phẩm
    public void performSearch(String keyword) {
        keyword = removeAccents(keyword.toLowerCase());
        ArrayList<Product> searchResult = new ArrayList<>();
        if (keyword.isEmpty()) {
            // Hiển thị tất cả sản phẩm khi keyword rỗng
            searchResult.addAll(arrayProducts);
        } else {
            // Tiến hành tìm kiếm khi keyword không rỗng
            for (Product product : arrayProducts) {
                String nameTable = removeAccents(product.getName().toLowerCase());
                if (nameTable.contains(keyword)) {
                    Log.d("TAG", "performSearch: " + nameTable);
                    searchResult.add(product);
                }
            }
        }
        // Cập nhật dữ liệu cho adapter
        if (productAdapter != null) {
            productAdapter.updateData(searchResult);
        } else {
            // Khởi tạo adapter và đặt cho recyclerView
            productAdapter = new ProductAdapter(searchResult, requireContext(), null);
            recyclerView.setAdapter(productAdapter);
        }
    }

    private String removeAccents(String input) {
        // Chuẩn hóa chuỗi đầu vào và loại bỏ dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        normalized = normalized.replaceAll("Đ", "d").replaceAll("đ", "d"); // Chuyển đổi chữ Đ thành d
        return normalized.replaceAll("\\s", ""); //return và  Xoá khoảng trắng
    }


}
