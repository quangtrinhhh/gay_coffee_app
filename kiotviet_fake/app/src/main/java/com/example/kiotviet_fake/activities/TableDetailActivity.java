package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.database.deleteOrder.OrderDeleteApiClient;
import com.example.kiotviet_fake.database.deleteOrder.OrderDeleteService;
import com.example.kiotviet_fake.database.deleteOrderItems.OrderDeleteItemsApiClient;
import com.example.kiotviet_fake.database.deleteOrderItems.OrderDeleteItemsService;
import com.example.kiotviet_fake.database.insertBillItems.BillsInsertItemsApiClient;
import com.example.kiotviet_fake.database.insertBillItems.BillsInsertItemsService;
import com.example.kiotviet_fake.database.insertBills.BillsInsertApiClient;
import com.example.kiotviet_fake.database.insertBills.BillsInsertService;
import com.example.kiotviet_fake.database.select.Orders_OrderItem_Product_SelectService;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusApiClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableDetailActivity extends AppCompatActivity implements AdapterListener {
    ImageView btnCancel, btnThem, btnNotification;
    TextView txtNameTable, txtCode, txtQuantity, txtTotalPrice;
    Button btnThanhToan, btnTamTinh, btnThongBao;
    LinearLayout btnDoiBan;

    RecyclerView recyclerView;

    int idTable;
    int newOrderId;
    String nameTable;
    int quantityTotal = 0;

    float priceTotal = 0;

    int idOrderByDelete;

    int newBillId;

    String dateTime;

    ProgressBar progressBar;
    int itemSize = 0;
    String code, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_detail);

        // lấy ra role
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");

        recyclerView = findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration); // Thêm dường viền vào RecyclerView

        addControl();
        updateUI();
        initView();
        btnClick();
    }


    public void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        txtNameTable = (TextView) findViewById(R.id.txtNameTable);
        txtCode = (TextView) findViewById(R.id.txtCode);
        txtQuantity = (TextView) findViewById(R.id.txtQuantity);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        btnThanhToan = (Button) findViewById(R.id.btnThanhToan);
        btnTamTinh = (Button) findViewById(R.id.btnTamTinh);
        btnThongBao = (Button) findViewById(R.id.btnThongBao);
        btnThem = (ImageView) findViewById(R.id.btnThem);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnDoiBan = (LinearLayout) findViewById(R.id.btn_doiBan);
        btnNotification = (ImageView) findViewById(R.id.btnNotification);
    }

    private void updateUI() {
        Intent intent = getIntent();
        nameTable = intent.getStringExtra("nameTable");
        idTable = intent.getIntExtra("idTable", 0);

        txtNameTable.setText(nameTable);

        switch (role) {
            case "order":
                btnThanhToan.setVisibility(View.GONE);
                break;
            default:
        }
    }

    public void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = SessionManager.getInstance();
                sessionManager.removeBillAll();

                Intent intent = new Intent(TableDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // thêm hiệu ứng loading
                progressBar.setVisibility(View.VISIBLE);
                insertBill("11177575", "60-dayfreetrial");
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToTableOrderProductActivity();
            }
        });

        btnDoiBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableDetailActivity.this, ChangeTable.class);
                intent.putExtra("nameTable", nameTable);
                intent.putExtra("idTable", idTable);
                intent.putExtra("orderId", idOrderByDelete);
                startActivity(intent);
            }
        });
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(TableDetailActivity.this, v);

                // Inflate the menu resource
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.setForceShowIcon(true);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.thong_tin_DH) {
                            Intent intent = new Intent(TableDetailActivity.this, InformationOderActivity.class);
                            intent.putExtra("dateTime", dateTime);
                            intent.putExtra("nameTable", nameTable);
                            startActivity(intent);
                        }
                        if (item.getItemId() == R.id.Gop_ban) {
                            Intent intent = new Intent(TableDetailActivity.this, SingleGraftActivity.class);
                            SharedPreferences sharedPreferences = getSharedPreferences("order", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("idTable_old", idTable);
                            editor.putInt("order_id_old", idOrderByDelete);

                            editor.apply();
                            startActivity(intent);
                        }
                        if (item.getItemId() == R.id.tach_don) {
                            Intent intent = new Intent(TableDetailActivity.this, TachDonActivity.class);
                            intent.putExtra("nameTable", nameTable);
                            intent.putExtra("idTable", idTable);
                            intent.putExtra("totalPrice", priceTotal);
                            intent.putExtra("dateTime", dateTime);
                            intent.putExtra("itemSize", itemSize);
                            intent.putExtra("idOrderByDelete", idOrderByDelete);

                            startActivity(intent);
                        }
                        if (item.getItemId() == R.id.huy_don) {
                            openNotificationDialog();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        btnTamTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableDetailActivity.this, XemTamTinhActivity.class);
                intent.putExtra("nameTable", nameTable);
                intent.putExtra("idTable", idTable);
                intent.putExtra("totalPrice", priceTotal);
                intent.putExtra("dateTime", dateTime);
                intent.putExtra("code", code);
                intent.putExtra("quantityTotal", quantityTotal);
                startActivity(intent);

                Log.d("TAG", "onResponse11111: " + priceTotal);
            }
        });
        btnThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TableDetailActivity.this, "Chức năng đang được cập nhật", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initView() {
        quantityTotal = 0;
        priceTotal = 0;
        itemSize = 0;
        //select data from api
        Orders_OrderItem_Product_SelectService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(Orders_OrderItem_Product_SelectService.class);
        Call<String> call = apiService.getOrders(idTable);

        //thêm dữ liệu vào sessionManager
        SessionManager sessionManager = SessionManager.getInstance();

        //reset bill
        sessionManager.removeBillAll();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<Product> arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = Integer.parseInt(jsonObject.getString("id"));
                            int order_id = Integer.parseInt(jsonObject.getString("orderId"));
                            code = jsonObject.getString("code");
                            dateTime = jsonObject.getString("dateTime");
                            int table_id = Integer.parseInt(jsonObject.getString("table_id"));
                            int user_id = Integer.parseInt(jsonObject.getString("user_id"));
                            int product_id = Integer.parseInt(jsonObject.getString("product_id"));
                            String product_name = jsonObject.getString("name");
                            float price = Integer.parseInt(jsonObject.getString("price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(price);

                            float totalPrice = Integer.parseInt(jsonObject.getString("totalPrice"));
                            int quantity = Integer.parseInt(jsonObject.getString("quantity"));
                            String product_code = jsonObject.getString("product_code");

                            arrayList.add(new Product(id, "", product_name, formattedPrice, 200, quantity, idTable, nameTable, product_id, "", product_code, 0));
                            quantityTotal += quantity;
                            priceTotal += totalPrice;
                            itemSize++;
                            txtCode.setText(code);
                            idOrderByDelete = order_id;
                            newOrderId = order_id;

                            // thêm vào kho lưu trữ bill
                            Bill bill = new Bill(dateTime, "demo", code, table_id, user_id, quantity, price * quantity, product_id, product_name, nameTable, price, id);
                            sessionManager.addBill(bill);

                            if (nameTable.toLowerCase().contains("mang")) {
                                selectInfoTableMangVe(idTable, nameTable); // gửi id và ten bàn
                            }

                            NumberFormat formatterNumberFormat = NumberFormat.getInstance(Locale.getDefault());
                            String formatPrice = formatterNumberFormat.format(priceTotal);


                            txtQuantity.setText("Tổng tiền " + quantityTotal);

                            txtTotalPrice.setText(formatPrice);

                        }
                        // kiểm tra xem còn sản phẩm nào trong bàn không nếu ko thì reset lại bàn và chuyển về trang home
                        if (jsonArray.length() == 0) {
                            deleteOrder("11177575", "60-dayfreetrial");
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }

                        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
                        ProductAdapter productAdapter = new ProductAdapter(arrayList, getApplicationContext(), TableDetailActivity.this); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView

                        updateTable("11177575", "60-dayfreetrial"); // cập nhật giá và trạng thái của bàn

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e("TAG", "Failed to fetch data: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    public void deleteOrder_items(String username, String password) throws ParseException {

        OrderDeleteItemsService service = OrderDeleteItemsApiClient.createService(username, password);
        Call<String> call = service.deleteOrderItem(idOrderByDelete);
        Log.e("TAG", "deleteOrder_items: " + idOrderByDelete);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // kiểm tra Retrofit đã hoàn thành
                if (response.isSuccessful()) {
                    try {
                        deleteOrder("11177575", "60-dayfreetrial");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    public void deleteOrder(String username, String password) throws ParseException {

        OrderDeleteService service = OrderDeleteApiClient.createService(username, password);
        Call<String> call = service.deleteOrder(idOrderByDelete);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // kiểm tra Retrofit đã hoàn thành
                if (response.isSuccessful()) {
                    isUpdateStatusTable("11177575", "60-dayfreetrial");
                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void isUpdateStatusTable(String username, String password) {

        int id = idTable;
        double status = 0;
        float table_price = 0;

        TableUpdateStatusService service = TableUpdateStatusApiClient.createService(username, password);
        Call<String> call = service.updateData(id, status, table_price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    navigateToTableMainActivity();
                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void insertBill(String username, String password) {
        //lấy thời gian hiện tại
        Date currentDate = new Date();
        SimpleDateFormat formatterSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatterSimpleDateFormat.format(currentDate);

        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<Bill> bills = sessionManager.getBills();
        Bill firstBill = bills.get(0);

        String txtTotal = txtTotalPrice.getText().toString();
        txtTotal = txtTotal.replace(".", "");

        BillsInsertService service = BillsInsertApiClient.createService(username, password);
        Call<String> call = service.insertBills(firstBill.getDateTime(), formattedDateTime, firstBill.getCode(), firstBill.getTableId(), firstBill.getUserId(), Float.parseFloat(txtTotal));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().toString());
                        newBillId = jsonObject.getInt("billId");
                        insertBillItems("11177575", "60-dayfreetrial");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void insertBillItems(String username, String password) {
        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<Bill> bills = sessionManager.getBills();

        int totalCalls = bills.size();
        final int[] completedCalls = {0};
        for (Bill bill : bills) {
            BillsInsertItemsService service = BillsInsertItemsApiClient.createService(username, password);
            Call<String> call = service.insertBillItems(bill.getQuantity(), bill.getTotalPrice(), bill.getProductId(), bill.getNameProduct(), newBillId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        try {
                            completedCalls[0]++;
                            if (completedCalls[0] == totalCalls) {
                                sessionManager.removeBillAll();
                                deleteOrder_items("11177575", "60-dayfreetrial");
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // Xử lý phản hồi không thành công
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Xử lý lỗi
                }
            });
        }
    }

    private void navigateToTableMainActivity() {
        Intent intent = new Intent(TableDetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.removeBillAll();

    }

    private void navigateToTableOrderProductActivity() {
        Intent intent = new Intent(TableDetailActivity.this, OrderProductActivity.class);
        intent.putExtra("idTable", idTable);
        intent.putExtra("nameTable", nameTable);
        intent.putExtra("idOrder", newOrderId);

        String formattedNumber = String.format("%.0f", priceTotal);
//        Log.d("TAG", "navigateToTableOrderProductActivity: "+formattedNumber);
        intent.putExtra("totalPriceTable", Integer.parseInt(formattedNumber));

        startActivity(intent);
    }

    // nhận thông báo từ productAdapter khi item đã được xoá
    @Override
    public void onItemDeleted() {
        quantityTotal = 0;
        priceTotal = 0;

        // thêm hiệu ứng loading
        progressBar.setVisibility(View.VISIBLE);

        initView();  // chạy lại initView
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void update_totalQuantity_totalPrice(int quantity, float priceTotal) {

    }

    @Override
    public void notification_insertOrder(int idTable, String nameTable) {

    }

    @Override
    public void notification_arrIdDeleteSize(int arrIdDelete) {

    }

    private void updateTable(String username, String password) {

        int id = idTable;
        double status = 1;
        float table_price = priceTotal;

        TableUpdateStatusService service = TableUpdateStatusApiClient.createService(username, password);
        Call<String> call = service.updateData(id, status, table_price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    public void selectInfoTableMangVe(int id, String nameTable) {
        SharedPreferences sharedPreferences = getSharedPreferences("tableMangVe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("tableId", id);
        editor.putString("tableName", nameTable);
        editor.apply();
    }

    public void openNotificationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtContent = (TextView) dialog.findViewById(R.id.tv_content);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.tv_title);
        Button btnHuy = (Button) dialog.findViewById(R.id.btn_huy);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btn_xacNhan);

        txtTitle.setText("Huỷ đơn");
        txtContent.setText("Xác nhận huỷ đơn hàng");

        dialog.show();

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // thêm hiệu ứng loading
                    progressBar.setVisibility(View.VISIBLE);
                    deleteOrder_items("11177575", "60-dayfreetrial");
                    dialog.dismiss();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        // Không thực hiện hành động nào khi nút quay trở lại được nhấn
//        super.onBackPressed();
    }


}