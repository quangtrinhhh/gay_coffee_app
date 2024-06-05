package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kiotviet_fake.fragments.FragmentCategoriesCafe;
import com.example.kiotviet_fake.fragments.FragmentCategoriesDaXay;
import com.example.kiotviet_fake.fragments.FragmentCategoriesMonThem;
import com.example.kiotviet_fake.fragments.FragmentCategoriesNuocDongChai;
import com.example.kiotviet_fake.fragments.FragmentCategoriesNuocEp;
import com.example.kiotviet_fake.fragments.FragmentCategoriesSinhTo;
import com.example.kiotviet_fake.fragments.FragmentCategoriesSodaY;
import com.example.kiotviet_fake.fragments.FragmentCategoriesSua;
import com.example.kiotviet_fake.fragments.FragmentCategoriesSuaChua;
import com.example.kiotviet_fake.fragments.FragmentCategoriesTatCa;
import com.example.kiotviet_fake.fragments.FragmentCategoriesThuocLa;
import com.example.kiotviet_fake.models.Category;
import com.example.kiotviet_fake.session.SessionCategories;

import java.util.ArrayList;
import java.util.List;

public class OrderProductAdapter extends FragmentStateAdapter {

    private Context context;
    private List<String> categoryTitles;
    private SessionCategories sessionCategories;

    public OrderProductAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Context context) {
        super(fragmentManager, lifecycle);
        this.context = context;
        this.categoryTitles = new ArrayList<>();
        loadCategoryTitles();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentCategoriesTatCa();
            case 1:
                return new FragmentCategoriesCafe();
            case 2:
                return new FragmentCategoriesDaXay();
            case 3:
                return new FragmentCategoriesNuocEp();
            case 4:
                return new FragmentCategoriesNuocDongChai();
            case 5:
                return new FragmentCategoriesSinhTo();
            case 6:
                return new FragmentCategoriesSodaY();
            case 7:
                return new FragmentCategoriesSuaChua();
            case 8:
                return new FragmentCategoriesSua();
            case 9:
                return new FragmentCategoriesThuocLa();
            case 10:
                return new FragmentCategoriesMonThem();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return categoryTitles.size();
    }

    public List<String> getCategoryTitles() {
        return categoryTitles;
    }

    private void loadCategoryTitles() {
        sessionCategories = new SessionCategories(context);
        List<Category> categories = sessionCategories.getCategories(); // Thay vì List<String>
        for (Category category : categories) { // Thay vì String category : categories
            categoryTitles.add(category.getName()); // Thêm tên của danh mục
        }
        notifyDataSetChanged();
    }
}
