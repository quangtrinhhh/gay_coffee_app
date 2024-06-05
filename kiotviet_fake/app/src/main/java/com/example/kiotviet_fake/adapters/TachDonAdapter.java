package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.TachDon;
import com.example.kiotviet_fake.session.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TachDonAdapter extends RecyclerView.Adapter<TachDonAdapter.viewHolder> {
    ArrayList<Product> products;
    Context context;
    private AdapterListener adapterListener;
    int totalQuantity = 0;

    public TachDonAdapter(ArrayList<Product> products, Context context, AdapterListener adapterListener) {
        this.products = products;
        this.context = context;
        this.adapterListener = adapterListener;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_tach_don, parent, false);

        return new viewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Product product = products.get(position);

        holder.txtName.setText(product.getName());
        holder.txtQuantity.setText("/ " + product.getQuantityOrder());
        holder.txtCount.setText("" + holder.COUNT[position]);

        String priceWithoutDots = product.getPrice().replace(".0", "");

        holder.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.COUNT[position] < product.getQuantityOrder()) {
                    holder.COUNT[position]++;
                    totalQuantity++;
                    holder.txtCount.setText("" + holder.COUNT[position]);
                    adapterListener.update_totalQuantity_totalPrice(totalQuantity, Float.parseFloat(product.getPrice()));

                    if (holder.COUNT[position] == 1) {
                        // thêm vào kho lưu trữ tach don
                        TachDon tachDon = new TachDon(product.getIdTable(), holder.COUNT[position], product.getQuantityOrder(), Float.parseFloat(priceWithoutDots) * holder.COUNT[position], product.getIdProduct(), Integer.parseInt(priceWithoutDots),product.getName(), product.getId());
                        holder.sessionManager.addTachDon(tachDon);
                    } else {
                        holder.sessionManager.updateQuantityProductTachDon(product.getId(), holder.COUNT[position]);
                        holder.sessionManager.updateTotalPriceProductTachDon(product.getId(), holder.COUNT[position]);
                    }
                }
            }
        });
        holder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.COUNT[position] > 0) {
                    holder.COUNT[position]--;
                    totalQuantity--;
                    holder.txtCount.setText("" + holder.COUNT[position]);
                    adapterListener.update_totalQuantity_totalPrice(totalQuantity, -1 * Float.parseFloat(product.getPrice()));
                    Log.d("TAG", "onClick: " + holder.COUNT[position]);

                    if (holder.COUNT[position] == 0) {
                        holder.sessionManager.removeTachDonByProductId(product.getId());
                    } else {
                        holder.sessionManager.updateQuantityProductTachDon(product.getId(), holder.COUNT[0]);
                        holder.sessionManager.updateTotalPriceProductTachDon(product.getId(), holder.COUNT[position]);
                    }
                }
            }
        });

    }


    public int getItemCount() {
        return products.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtCount, txtName, txtQuantity;
        ImageView btnTang, btnGiam;
        final int[] COUNT = new int[products.size()];
        SessionManager sessionManager = SessionManager.getInstance();

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtCount = itemView.findViewById(R.id.tv_count);
            txtName = itemView.findViewById(R.id.tv_name);
            txtQuantity = itemView.findViewById(R.id.tv_quantity);
            btnGiam = itemView.findViewById(R.id.iv_giam);
            btnTang = itemView.findViewById(R.id.iv_tang);


        }
    }


}