package com.example.kiotviet_fake.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.AdminNhanVienDetailActivity;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Category;
import com.example.kiotviet_fake.models.NhanVien;

import java.util.ArrayList;

public class NhanVienAdminAdapter extends RecyclerView.Adapter<NhanVienAdminAdapter.ViewHolder> {
    ArrayList<NhanVien> nhanViens;
    Context context;

    public NhanVienAdminAdapter(ArrayList<NhanVien> nhanViens, Context context) {
        this.nhanViens = nhanViens;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_nhan_vien, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhanVien nhanVien = nhanViens.get(position);
        holder.txtName.setText(nhanVien.getName());
        holder.txtRole.setText(nhanVien.getRole());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminNhanVienDetailActivity.class);
                intent.putExtra("id",nhanVien.getId());
                intent.putExtra("userName",nhanVien.getName());
                intent.putExtra("password",nhanVien.getPassword());
                intent.putExtra("role",nhanVien.getRole());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return nhanViens.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtRole;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_name);
            txtRole = itemView.findViewById(R.id.tv_role);
            item = itemView.findViewById(R.id.item);
        }
    }
}
