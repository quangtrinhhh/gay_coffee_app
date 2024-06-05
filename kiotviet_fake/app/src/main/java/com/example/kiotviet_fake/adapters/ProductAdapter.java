package com.example.kiotviet_fake.adapters;

import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.LoginActivity;
import com.example.kiotviet_fake.activities.MainActivity;
import com.example.kiotviet_fake.activities.ProductDetailActivity;
import com.example.kiotviet_fake.activities.TableDetailActivity;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderAPI;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.session.SessionManager;

import java.text.ParseException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewHolder> {
    ArrayList<Product> products;
    Context context;
    private AdapterListener adapterListener;

    public ProductAdapter(ArrayList<Product> products, Context context, AdapterListener adapterListener) {
        this.products = products;
        this.context = context;
        this.adapterListener = adapterListener;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new viewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Product product = products.get(position);
        SessionManager sessionManager = SessionManager.getInstance();
        int getOrderQuantityByProductId = sessionManager.getOrderQuantityByIdProductItem(product.getIdProductItem());
        final int[] count = {getOrderQuantityByProductId > 0 ? getOrderQuantityByProductId : 1};
        final boolean[] isToggle = {false};
        int quantity = product.getQuantityOrder();

        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(String.valueOf(product.getPrice()));
        holder.txtQuantity.setText(String.valueOf(product.getQuantityOrder()));
        Log.d("TAG", "onMenuItemClick11: " + product.getQuantityOrder());

        holder.txtQuantity.setVisibility(product.getIdTable() > 0 ? View.VISIBLE : View.GONE);
        holder.imgSelect.setVisibility(product.getIdTable() > 0 ? View.VISIBLE : View.GONE);

        holder.txtCount.setText(String.valueOf(getOrderQuantityByProductId > 0 ? getOrderQuantityByProductId : 1));
        holder.countQuanity.setVisibility(getOrderQuantityByProductId > 0 ? View.VISIBLE : View.GONE);
        holder.imgCheck.setVisibility(getOrderQuantityByProductId > 0 ? View.VISIBLE : View.GONE);
        isToggle[0] = getOrderQuantityByProductId > 0 ? true : false;

        // xử lý item order product
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!product.getIdProductItem().equals("") && !isToggle[0]) {
                    holder.countQuanity.setVisibility(View.VISIBLE);
                    holder.imgCheck.setVisibility(View.VISIBLE);
                    product.setQuantityOrder(1); // gán số lượng 1 khi chọn vào sản phẩm

                    // thêm đơn hàng mới
                    Order order1 = new Order(product.getIdProductItem(), product.getQuantityOrder(), product.getPrice(), 1, product.getId());
                    sessionManager.addOrder(order1);

                    // Khi sự kiện xảy ra, phát broadcast
                    Intent intent = new Intent("ACTION_UPDATE");
                    context.sendBroadcast(intent);
                } else {
                    holder.countQuanity.setVisibility(View.GONE);
                    holder.imgCheck.setVisibility(View.GONE);
                    product.setQuantityOrder(0);  // gán số lượng 0 khi ko chọn vào sản phẩm

                    //xóa đơn hàng
                    SessionManager sessionManager = SessionManager.getInstance();
                    sessionManager.removeOrderByProductId(product.getIdProductItem());

                    // Khi sự kiện xảy ra, phát broadcast
                    Intent intent = new Intent("ACTION_UPDATE");
                    context.sendBroadcast(intent);
                    count[0] = 1;
                    holder.txtCount.setText(count[0] + "");
                }
                isToggle[0] = !isToggle[0];

            }

        });

        holder.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]++;
                holder.txtCount.setText(String.valueOf(count[0]));
                product.setQuantityOrder(count[0]);
                sessionManager.updateQuantityProduct(product.getIdProductItem(), count[0]);
            }
        });

        holder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[0] > 1) {
                    count[0]--;
                    holder.txtCount.setText(String.valueOf(count[0]));
                    product.setQuantityOrder(count[0]);
                    sessionManager.updateQuantityProduct(product.getIdProductItem(), count[0]);
                }
            }
        });


        // xử lý item detail product
        holder.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setForceShowIcon(true);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_more) {
                            // Xử lý khi người dùng chọn sửa
                            Intent intent = new Intent(context, ProductDetailActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("product_id", product.getId());
                            intent.putExtra("product_name", product.getName());
                            intent.putExtra("product_price", product.getPrice());
                            intent.putExtra("product_quantity", quantity);
                            intent.putExtra("nameTable", product.getNameTable());
                            intent.putExtra("product_code",product.getProduct_code());
                            intent.putExtra("idTable", product.getIdTable());

                            context.startActivity(intent);
                            adapterListener.finishActivity();// Sử dụng context để khởi chạy Intent

                        }
                        if (item.getItemId() == R.id.action_delete) {
                            // Xử lý khi người dùng chọn sửa
                            Log.e("TAG", "onMenuItemClick: " + product.getId());
                            DeleteOrderProductItem("11177575", "60-dayfreetrial");
                            return true;
                        }
                        return false;
                    }

                    private void DeleteOrderProductItem(String username, String password) {

                        DeleteItemOfOrderService service = DeleteItemOfOrderAPI.createService(username, password);
                        Call<String> call = service.deleteItemOfOrder(product.id);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()) {
                                    // Xử lý phản hồi thành công từ API nếu cần
                                    Log.d("DeleteItemOfOrder", "Xóa sản phẩm thành công id  : " + product.getId());
                                    // Thông báo cho Activity biết rằng có item được xoá
                                    adapterListener.onItemDeleted();

                                    //cập nhật UI
                                    products.remove(product);
                                    notifyDataSetChanged();

                                } else {
                                    // Xử lý phản hồi lỗi từ API nếu cần
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                    }
                });
                popupMenu.show();

            }
        });
    }


    public int getItemCount() {
        return products.size();
    }
    public void updateData(ArrayList<Product> newData) {
        products.clear();
        products.addAll(newData);
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity, btnTang, btnGiam, txtCount;
        ImageView imgSelect, imgCheck;
        LinearLayout countQuanity;
        RelativeLayout item;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtQuantity = (TextView) itemView.findViewById(R.id.txtQuantity);
            imgSelect = (ImageView) itemView.findViewById(R.id.imgSelect);
            imgCheck = (ImageView) itemView.findViewById(R.id.imgCheck);
            item = (RelativeLayout) itemView.findViewById(R.id.item);
            countQuanity = (LinearLayout) itemView.findViewById(R.id.countQuanity);
            btnTang = (TextView) itemView.findViewById(R.id.btnTang);
            btnGiam = (TextView) itemView.findViewById(R.id.btnGiam);
            txtCount = (TextView) itemView.findViewById(R.id.txtCount);

        }
    }


}