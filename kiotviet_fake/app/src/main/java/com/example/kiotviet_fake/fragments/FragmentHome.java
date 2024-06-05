package com.example.kiotviet_fake.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.HomePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentHome extends Fragment {
    private ViewPager2 pager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_frament, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControl(view);
    }

    private void addControl(View view) {
        pager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);

        HomePagerAdapter adapter = new HomePagerAdapter(requireActivity());
        pager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Tất cả");
                        break;
                    case 1:
                        tab.setText("Sử dụng");
                        break;
                    case 2:
                        tab.setText("Còn trống");
                        break;
                }
            }
        }).attach();

    }
}
