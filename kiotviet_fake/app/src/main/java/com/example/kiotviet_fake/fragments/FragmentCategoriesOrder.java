package com.example.kiotviet_fake.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.OrderProductAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentCategoriesOrder extends Fragment {
    private ViewPager2 pager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addControl();
    }

    private void addControl() {
        pager = getView().findViewById(R.id.view_pager);
        tabLayout = getView().findViewById(R.id.tab_layout);

        FragmentManager manager = getChildFragmentManager();
        OrderProductAdapter adapter = new OrderProductAdapter(manager, getLifecycle(), getContext());
        pager.setAdapter(adapter);

        // Đặt số trang được giữ
        pager.setOffscreenPageLimit(3); // Số trang được giữ: 5


        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(adapter.getCategoryTitles().get(position));
            }
        }).attach();

        // Bắt sự kiện khi tab được chọn
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Chuyển ngay lập tức sang trang tương ứng khi tab được chọn
                pager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không cần xử lý
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không cần xử lý
            }
        });

    }
}
