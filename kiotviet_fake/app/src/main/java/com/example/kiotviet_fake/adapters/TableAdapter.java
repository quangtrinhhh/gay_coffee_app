package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.OrderProductActivity;
import com.example.kiotviet_fake.activities.TableDetailActivity;
import com.example.kiotviet_fake.models.Table;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    private ArrayList<Table> tables;
    private Context context;

    public TableAdapter(ArrayList<Table> tables, Context context) {
        this.tables = tables;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_table, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Table table = tables.get(position);
        holder.txtSoBan.setText(table.getTable_name());
        holder.txtGia.setText(String.valueOf(table.getTable_price()));
        if (table.getTable_price().equals("0")) {
            holder.txtGia.setText("");
        }

        if (table.getStatus() != 0) {
            holder.item.setBackgroundResource(R.drawable.bg_item_1);
        } else {
            holder.item.setBackgroundResource(R.drawable.bg_item);
        }

        if (table.getTable_name().toLowerCase().contains("mang")) {
            holder.imgCart.setVisibility(View.VISIBLE);
        } else {
            holder.imgCart.setVisibility(View.GONE);
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (table.getStatus() != 0) {
                    intent = new Intent(context, TableDetailActivity.class);
                } else {
                    intent = new Intent(context, OrderProductActivity.class);
                }
                intent.putExtra("nameTable", table.getTable_name());
                intent.putExtra("idTable", table.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public void updateData(ArrayList<Table> newData) {
        tables.clear();
        tables.addAll(newData);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSoBan, txtThoiGian, txtGia;
        LinearLayout item;
        ImageView imgCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            txtSoBan = itemView.findViewById(R.id.txtSoBan);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            txtGia = itemView.findViewById(R.id.txtGia);
            imgCart = itemView.findViewById(R.id.imgCart);
        }
    }
}
