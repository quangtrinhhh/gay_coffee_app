package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.ProductDetailActivity;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderAPI;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Bill_hang_hoa;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HangHoaAdapter extends RecyclerView.Adapter<HangHoaAdapter.viewHolder> {
    ArrayList<Bill_hang_hoa> bill_hang_hoas;
    Context context;

    public HangHoaAdapter(ArrayList<Bill_hang_hoa> bill_hang_hoas, Context context) {
        this.bill_hang_hoas = bill_hang_hoas;
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
        Bill_hang_hoa billHangHoa = bill_hang_hoas.get(position);
        holder.txtNameProduct.setText(billHangHoa.getName());
        holder.txtQuantityProduct.setText(String.valueOf(billHangHoa.getQuantity()));
        holder.txtTotalPrice.setText(billHangHoa.getPrice());

        if(billHangHoa.getName().toLowerCase().equals("tổng")){
            holder.txtNameProduct.setTypeface(null, Typeface.BOLD);
            holder.txtQuantityProduct.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return bill_hang_hoas.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtNameProduct, txtQuantityProduct, txtTotalPrice;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.tv_nameProduct);
            txtQuantityProduct = itemView.findViewById(R.id.tv_quantityProduct);
            txtTotalPrice = itemView.findViewById(R.id.tv_totalPrice);

        }
    }


}