package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.models.Bill_hang_hoa;
import com.example.kiotviet_fake.models.Bill_phong_ban;

import java.util.ArrayList;

public class PhongBanAdapter extends RecyclerView.Adapter<PhongBanAdapter.viewHolder> {
    ArrayList<Bill_phong_ban> billPhongBans;
    Context context;

    public PhongBanAdapter(ArrayList<Bill_phong_ban> billPhongBans, Context context) {
        this.billPhongBans = billPhongBans;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_hang_hoa_ban_ra, parent, false);
        return new viewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Bill_phong_ban billPhongBan = billPhongBans.get(position);
        holder.txtNameProduct.setText(billPhongBan.getName());
        holder.txtQuantityProduct.setText(billPhongBan.getQuantity() + " đơn");
        holder.txtTotalPrice.setText(billPhongBan.getPrice());

        if(billPhongBan.getName().toLowerCase().equals("tổng")){
            holder.txtNameProduct.setTypeface(null, Typeface.BOLD);
        }
        if(billPhongBan.getName().toLowerCase().contains("mang")){
            holder.item.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return billPhongBans.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtNameProduct, txtQuantityProduct, txtTotalPrice;
        LinearLayout item;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.tv_nameProduct);
            txtQuantityProduct = itemView.findViewById(R.id.tv_quantityProduct);
            txtTotalPrice = itemView.findViewById(R.id.tv_totalPrice);
            item = itemView.findViewById(R.id.item);

        }
    }


}