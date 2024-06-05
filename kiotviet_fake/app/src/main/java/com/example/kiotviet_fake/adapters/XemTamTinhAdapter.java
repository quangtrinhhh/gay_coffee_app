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

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemTamTinhAdapter extends RecyclerView.Adapter<XemTamTinhAdapter.viewHolder> {
    ArrayList<Product> products;
    Context context;

    public XemTamTinhAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_xem_tam_tinh, parent, false);

        return new viewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Product product = products.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        // Định dạng tổng giá
        String formattedPrice = decimalFormat.format(Float.parseFloat(product.getPrice()));
        String formattedTotalPrice = decimalFormat.format(Float.parseFloat(product.getPrice()) * product.getQuantityOrder());


        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(formattedPrice);
        holder.txtQuantity.setText("x " + product.getQuantityOrder());

        holder.txtTotalPrice.setText(formattedTotalPrice);
    }


    public int getItemCount() {
        return products.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity, txtTotalPrice;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.tv_name);
            txtPrice = (TextView) itemView.findViewById(R.id.tv_price);
            txtQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            txtTotalPrice = (TextView) itemView.findViewById(R.id.tv_totalPrice);
        }
    }


}