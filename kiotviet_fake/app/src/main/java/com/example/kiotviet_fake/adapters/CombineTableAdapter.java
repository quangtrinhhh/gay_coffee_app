package com.example.kiotviet_fake.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.SingleGraftActivity;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.models.TableGroup;

import java.util.ArrayList;

public class CombineTableAdapter extends RecyclerView.Adapter<CombineTableAdapter.viewHolder> {
    ArrayList<TableGroup> tables;
    Context context;
    int idTable;
    int idTableupdate;

    public CombineTableAdapter(ArrayList<TableGroup> tables, Context context, int idTable, int idTableupdate) {
        this.tables = tables;
        this.context = context;
        this.idTable = idTable;
        this.idTableupdate = idTableupdate;
    }

    @NonNull
    @Override
    public CombineTableAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_changer_table, parent, false);
        return new CombineTableAdapter.viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CombineTableAdapter.viewHolder holder, int position) {
        TableGroup table = tables.get(position);
        Log.d("TAG", "onBindViewHolder: " + table.getTable_id() +" " + idTable);
        holder.tvNameTable.setText(table.getTable_name());
        if (table.getTable_id() != idTable) {
            holder.tvNameTable.setTextColor(Color.parseColor("#000000"));
            holder.tvIconCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        }else{
            holder.tvNameTable.setTextColor(Color.parseColor("#0365ca"));
            holder.tvIconCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#0365ca")));
        }
        holder.lnlEItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idTableupdate = table.getTable_id();
                Intent intent = new Intent(context, SingleGraftActivity.class);
                intent.putExtra("price", table.getTable_price());
                System.out.println("test adapter id" +  table.getTable_price() +" / "+ table.getTable_id());
                intent.putExtra("id_table", table.getTable_id());
                intent.putExtra("nameTable", table.getTable_name());
                intent.putExtra("quantity_product", table.getProduct_quantity());
                intent.putExtra("order_id_new", table.getOrder_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

                // Kết thúc Activity hiện tại
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvNameTable;
        LinearLayout lnlEItem;
        ImageView tvIconCheck;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameTable = (TextView) itemView.findViewById(R.id.tv_nameTable);
            lnlEItem = (LinearLayout) itemView.findViewById(R.id.lnl_item);
            tvIconCheck = (ImageView) itemView.findViewById(R.id.iv_iconCheck);
        }
    }
}
