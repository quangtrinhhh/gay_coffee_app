package com.example.kiotviet_fake.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.example.kiotviet_fake.activities.ChangeTable;
import com.example.kiotviet_fake.activities.ProductDetailActivity;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderAPI;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.session.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeTableItemAdapter extends RecyclerView.Adapter<ChangeTableItemAdapter.viewHolder> {
    ArrayList<Table> tables;
    Context context;
    int idTable;
    int idTableupdate;
    int orderId;
    String checkFlat;
    private AdapterListener adapterListener;

    public ChangeTableItemAdapter(ArrayList<Table> tables, Context context, int idTable, int orderId, String checkFlat, AdapterListener adapterListener) {
        this.tables = tables;
        this.context = context;
        this.idTable = idTable;
        this.orderId = orderId;
        this.checkFlat = checkFlat;
        this.adapterListener = adapterListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_changer_table, parent, false);
        return new viewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Table table = tables.get(position);
        holder.tvNameTable.setText(table.getTable_name());
        if (table.getId() == idTable) {
            holder.tvNameTable.setTextColor(Color.parseColor("#0365ca"));
            holder.tvIconCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#0365ca")));
        } else {
            holder.tvNameTable.setTextColor(Color.parseColor("#000000"));
            holder.tvIconCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        }
        holder.lnlEItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (checkFlat) {
                    case "doiBan":
                        idTableupdate = table.getId();
                        Intent intent = new Intent(context, ChangeTable.class);
                        intent.putExtra("idTable", idTable);
                        intent.putExtra("idTableupdate", idTableupdate);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("nameTable", table.getTable_name());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                        // Kết thúc Activity hiện tại
                        ((Activity) context).finish();
                        break;
                    case "tachDon":
                        idTable = table.getId();
                        notifyDataSetChanged();// thông báo cho adapter cập nhật lại toàn bộ giao diện
                        adapterListener.notification_insertOrder(idTable,table.getTable_name());
                        break;
                    default:
                        System.out.println("error");
                }
            }
        });
    }

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