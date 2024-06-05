package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.models.DetailBill;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailBillAdapter extends RecyclerView.Adapter<DetailBillAdapter.ViewHolder>{
    private ArrayList<DetailBill> listBill;
    private Context context;

    public DetailBillAdapter(ArrayList<DetailBill> listBill, Context context) {
        this.listBill = listBill;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailBillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DetailBillAdapter.ViewHolder holder, int position) {
        DetailBill detailBill = listBill.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTotal = decimalFormat.format(detailBill.getTotal_price());
        holder.textViewProductName.setText(detailBill.getName());
        holder.textViewQuantity.setText(String.valueOf(detailBill.getQuantity()));
        holder.textViewTotalPrice.setText(formattedTotal);

    }

    @Override
    public int getItemCount() {
        return listBill.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName;
        TextView textViewQuantity;
        TextView textViewTotalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.name_product);
            textViewQuantity = itemView.findViewById(R.id.quantity_product);
            textViewTotalPrice = itemView.findViewById(R.id.price_price);

        }
    }
}
