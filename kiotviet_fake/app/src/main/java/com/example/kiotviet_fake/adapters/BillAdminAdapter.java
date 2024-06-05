package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.DetailBillActivity;
import com.example.kiotviet_fake.models.Bill_Admin;
import com.example.kiotviet_fake.models.DetailBill;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BillAdminAdapter extends RecyclerView.Adapter<BillAdminAdapter.ViewHolder>{
    private ArrayList<Bill_Admin> listBill;
    private Context context;

    public BillAdminAdapter(ArrayList<Bill_Admin> listBill, Context context) {
        this.listBill = listBill;
        this.context = context;
    }

    @NonNull
    @Override
    public BillAdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdminAdapter.ViewHolder holder, int position) {
        Bill_Admin billAdmin = listBill.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTotal = decimalFormat.format(billAdmin.getTotal_price());
        holder.textViewCode.setText(billAdmin.getCode_admin());
        holder.textViewName.setText(String.valueOf(billAdmin.getName_user()));
        holder.textViewTotalPrice.setText(formattedTotal);
        holder.textViewDayTime.setText(billAdmin.getDateTimeEnd());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sử dụng context từ Adapter
                Intent intent = new Intent(context, DetailBillActivity.class);
                // Truyền dữ liệu nếu cần thiết
                intent.putExtra("bill_id", billAdmin.id_bill);
                System.out.println("bill_id  : "+ billAdmin.id_bill);
                // Thực hiện chuyển Activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listBill.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCode;
        TextView textViewTotalPrice;
        TextView textViewName;
        TextView textViewDayTime;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewCode = itemView.findViewById(R.id.code_bill);
            textViewTotalPrice = itemView.findViewById(R.id.total_bill);
            textViewName = itemView.findViewById(R.id.name_bill);
            textViewDayTime = itemView.findViewById(R.id.day_time);
        }

    }
}
