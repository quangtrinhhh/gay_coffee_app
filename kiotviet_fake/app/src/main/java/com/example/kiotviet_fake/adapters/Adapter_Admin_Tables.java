package com.example.kiotviet_fake.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.PhongBanActivity;
import com.example.kiotviet_fake.database.UpdateNameTable.UpdateNameTableAPI;
import com.example.kiotviet_fake.database.UpdateNameTable.UpdateNameTableService;
import com.example.kiotviet_fake.database.deleteTable.DeleteTableAPI;
import com.example.kiotviet_fake.database.deleteTable.DeleteTableService;
import com.example.kiotviet_fake.database.updateOrderTableById.UpdateOrderTableByIdAPI;
import com.example.kiotviet_fake.database.updateOrderTableById.UpdateOrderTableByIdService;
import com.example.kiotviet_fake.models.Table_Admin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Admin_Tables extends RecyclerView.Adapter<Adapter_Admin_Tables.ViewHolder>{
    private ArrayList<Table_Admin> tables;
    private Context context;


    public Adapter_Admin_Tables(ArrayList<Table_Admin> tables, Context context) {
        this.tables = tables;
        this.context = context;

    }

    @NonNull
    @Override
    public Adapter_Admin_Tables.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Admin_Tables.ViewHolder holder, int position) {
        Table_Admin tableAdmin = tables.get(position);
        holder.name_table.setText(tableAdmin.getTable_name());

        String priceText = tableAdmin.getTable_price();
        double price = 0; // Giá trị mặc định
        if (priceText != null && !priceText.isEmpty()) {
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Xử lý trường hợp không thể chuyển đổi thành số
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTotal = decimalFormat.format(price);
        holder.text_price.setText(priceText != null && !priceText.isEmpty() ? formattedTotal : "0");
        holder.text_statucs.setText(tableAdmin.getStatus() == 0 ?  "Bàn trống" : "Đang dùng");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị AlertDialog cho phần tử được nhấn
                showAlertDialog(tableAdmin);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_table,text_price,text_statucs;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_table = itemView.findViewById(R.id.name_table);
            text_price = itemView.findViewById(R.id.text_price);
            text_statucs = itemView.findViewById(R.id.text_statucs);

        }
    }
    private void showAlertDialog(Table_Admin tableAdmin) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Sửa hoặc xóa bàn");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_table, null);
        dialogBuilder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        Button buttonDelete = dialogView.findViewById(R.id.buttonDelete);
        Button buttonEdit = dialogView.findViewById(R.id.buttonEdit);

        // Set hint to the EditText
        editTextName.setText(tableAdmin.getTable_name());

        AlertDialog alertDialog = dialogBuilder.create();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete action
                // For example:
                // deleteTable(tableAdmin);
                if(tableAdmin.getStatus() == 0){
                    DeleteTable(tableAdmin.getId_table());
                    alertDialog.dismiss();
                }else {
                    Toast.makeText(context, "Vui lòng thanh toán trước khi xóa", Toast.LENGTH_SHORT).show();
                    MessengerDialog();
                }

            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(newName)) {
                    // Handle edit action
                    // For example:
                    // editTableName(tableAdmin, newName);
                    UpdattNameTable(tableAdmin.getId_table(),newName);
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }

    private void MessengerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Vui lòng thánh toán bàn trước khi xóa")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Đóng dialog
                    }
                })
                .show();
    }

    private void DeleteTable(String idTable) {

        DeleteTableService updateNameTableService = DeleteTableAPI.createService("11177575", "60-dayfreetrial");
        Call<String> call = updateNameTableService.deleteTable( idTable);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    if (context instanceof PhongBanActivity) {
                        ((PhongBanActivity) context).LoadDataTables();
                    }
                } else {
                    // Xử lý phản hồi không thành công
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void UpdattNameTable(String idTable, String newName) {
        System.out.println("test update table " + idTable+ " " + newName);
        UpdateNameTableService updateNameTableService = UpdateNameTableAPI.createService("11177575", "60-dayfreetrial");
        Call<String> call = updateNameTableService.updateNameTable(newName.toString().toUpperCase(), idTable);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    if (context instanceof PhongBanActivity) {
                        ((PhongBanActivity) context).LoadDataTables();
                    }
                } else {
                    // Xử lý phản hồi không thành công
                    Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
