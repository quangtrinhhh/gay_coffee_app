package com.example.kiotviet_fake.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kiotviet_fake.fragments.FragmentAdminHangHoa;
import com.example.kiotviet_fake.fragments.FragmentAdminHoaDon;
import com.example.kiotviet_fake.fragments.FragmentAdminNhieuHon;
import com.example.kiotviet_fake.fragments.FragmentAdminThongBao;
import com.example.kiotviet_fake.fragments.FragmentAdminTongQuan;

public class ViewPagerAdminAdapter extends FragmentStateAdapter {

    public ViewPagerAdminAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FragmentAdminTongQuan();
        } else if (position == 1) {
            return new FragmentAdminHoaDon();
        } else if (position == 2) {
            return new FragmentAdminHangHoa();
        } else if (position == 3) {
            return new FragmentAdminThongBao();
        } else if (position == 4) {
            return new FragmentAdminNhieuHon();
        } else {
            return new FragmentAdminTongQuan(); // Trường hợp mặc định, không nên đạt được
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
