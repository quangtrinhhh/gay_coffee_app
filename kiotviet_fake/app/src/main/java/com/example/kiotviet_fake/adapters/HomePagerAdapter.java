package com.example.kiotviet_fake.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kiotviet_fake.fragments.FragmentTatCa;
import com.example.kiotviet_fake.fragments.FragmentConTrong;
import com.example.kiotviet_fake.fragments.FragmentSuDung;

public class HomePagerAdapter extends FragmentStateAdapter {

    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentTatCa();
            case 1:
                return new FragmentSuDung();
            case 2:
                return new FragmentConTrong();
            default:
                return new FragmentTatCa();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
