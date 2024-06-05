package com.example.kiotviet_fake.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kiotviet_fake.models.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionCategories {

    private static final String PREF_NAME = "session_categories";
    private static final String KEY_CATEGORIES = "categories";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public SessionCategories(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // Thêm một đối tượng Category vào danh sách
    public void addCategory(Category category) {
        List<Category> categories = getCategories();
        categories.add(category);
        saveCategories(categories);
    }

    // Xóa tất cả các danh mục
    public void clearCategories() {
        editor.remove(KEY_CATEGORIES).apply();
    }

    // Lấy danh sách tất cả các danh mục
    public List<Category> getCategories() {
        String categoriesJson = sharedPreferences.getString(KEY_CATEGORIES, null);
        if (categoriesJson != null) {
            Type type = new TypeToken<List<Category>>() {}.getType();
            return gson.fromJson(categoriesJson, type);
        } else {
            return new ArrayList<>();
        }
    }

    // Lưu danh sách danh mục
    private void saveCategories(List<Category> categories) {
        String categoriesJson = gson.toJson(categories);
        editor.putString(KEY_CATEGORIES, categoriesJson).apply();
    }
}
