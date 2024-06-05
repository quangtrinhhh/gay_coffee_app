package com.example.kiotviet_fake.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.fragments.FragmentConTrong;
import com.example.kiotviet_fake.fragments.FragmentCungUngMon;
import com.example.kiotviet_fake.fragments.FragmentHetMon;
import com.example.kiotviet_fake.fragments.FragmentLoiInCheBien;
import com.example.kiotviet_fake.fragments.FragmentSuDung;
import com.example.kiotviet_fake.fragments.FragmentTatCa;
import com.example.kiotviet_fake.models.Table;

import java.util.ArrayList;

public class NotificationPagerAdapter extends FragmentStatePagerAdapter {
    public NotificationPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = new FragmentCungUngMon();
                break;
            case 1:
                frag = new FragmentHetMon();
                break;
            case 2:
                frag = new FragmentLoiInCheBien();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Công ứng món";
                break;
            case 1:
                title = "Hết món";
                break;
            case 2:
                title = "Lỗi in chế biến";
                break;
        }
        return title;
    }

}
