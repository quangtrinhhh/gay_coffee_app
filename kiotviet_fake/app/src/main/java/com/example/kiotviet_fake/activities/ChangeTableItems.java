package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ChangeTableItemAdapter;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderAPI;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderService;
import com.example.kiotviet_fake.database.deleteOrder.OrderDeleteApiClient;
import com.example.kiotviet_fake.database.deleteOrder.OrderDeleteService;
import com.example.kiotviet_fake.database.insertOrderItems.OrderInsertItemsApiClient;
import com.example.kiotviet_fake.database.insertOrderItems.OrderInsertItemsService;
import com.example.kiotviet_fake.database.insertOrders.OrderInsertApiClient;
import com.example.kiotviet_fake.database.insertOrders.OrderInsertService;
import com.example.kiotviet_fake.database.select.Orders_OrderItem_Product_SelectService;
import com.example.kiotviet_fake.database.select.TableSelectByUserIdService;
import com.example.kiotviet_fake.database.select.TableSelectService;
import com.example.kiotviet_fake.database.updateItemOfProduct.UpdateItemOfOrderAPI;
import com.example.kiotviet_fake.database.updateItemOfProduct.UpdateItemOfOrderService;
import com.example.kiotviet_fake.database.updateOrderTableById.UpdateOrderTableByIdAPI;
import com.example.kiotviet_fake.database.updateOrderTableById.UpdateOrderTableByIdService;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusApiClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.models.TachDon;
import com.example.kiotviet_fake.session.SessionManager;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeTableItems extends AppCompatActivity implements AdapterListener {
    ImageView btnCancel, btnSearch, btnCloseSearch;
    LinearLayout inputSearch;
    EditText searchEditText;
    TextView txtNameTable;
    int isTableUserId;
    String nameTable,isShopId;
    int idTable;
    int orderId;
    int idOrderByDelete;
    String checkFlat;
    int newOrderId = 0;
    int tableTotalPrice;
    ProgressBar progressBar;
    ArrayList<Boolean> isDelteOrders = new ArrayList<>();
    int idTableupdate;
    int itemSize;
    float totalPriceAll = 0;

    ArrayList<Table> arrayList = new ArrayList<>();
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_table_items);

        // lấy ra userId vừa dc truyền khi login thành công
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isTableUserId = sharedPreferences.getInt("userId", 0);
        isShopId = sharedPreferences.getString("shop_id", "");

        Intent intent = getIntent();
        nameTable = intent.getStringExtra("nameTable");
        idTable = intent.getIntExtra("idTable", 0);
        orderId = intent.getIntExtra("orderId", 0);
        checkFlat = intent.getStringExtra("checkFlat");
        itemSize = intent.getIntExtra("itemSize", 0);
        totalPriceAll = intent.getFloatExtra("totalPriceAll", 0);
        idOrderByDelete = intent.getIntExtra("idOrderByDelete", 0);

        addControl();
        btnClick();

        switch (checkFlat) {
            case "doiBan":
                initView("Mang về");
                break;
            case "tachDon":
                initView("");
                break;
            default:
                System.out.println("Case ko tồn tại");

        }
    }

    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (checkFlat) {
                    // quy tắt đặt tên biến trong java là "camelCase"
                    case "doiBan":
                        Intent intent = new Intent(ChangeTableItems.this, ChangeTable.class);
                        intent.putExtra("nameTable", nameTable);
                        intent.putExtra("idTable", idTable);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                        finish();
                        break;
                    case "tachDon":
                        finish();
                        break;
                    default:
                        System.out.println("Case ko tồn tại");
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.requestFocus();

                // Hiển thị bàn phím
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);

                inputSearch.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) txtNameTable.getLayoutParams();
                layoutParams.weight = 0;
                txtNameTable.setLayoutParams(layoutParams);
            }
        });
        btnCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");

                // Ẩn bàn phím
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

                inputSearch.setVisibility(View.GONE);
                btnSearch.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) txtNameTable.getLayoutParams();
                layoutParams.weight = 1;
                txtNameTable.setLayoutParams(layoutParams);
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().toLowerCase().trim();
                ArrayList<Table> filterTables = new ArrayList<>();
                searchText = removeSpecialCharacters(searchText);

                for (Table table : arrayList) {
                    System.out.println("search" + removeSpecialCharacters(table.getTable_name()) + searchText);
                    // Lọc dữ liệu theo tên
                    if (removeSpecialCharacters(table.getTable_name()).toLowerCase().contains(searchText)) {
                        filterTables.add(table);
                    }
                }

                // Tạo adapter mới với dữ liệu đã lọc và cập nhật ListView
                ChangeTableItemAdapter ChangeTableItemAdapter = new ChangeTableItemAdapter(filterTables, ChangeTableItems.this, idTable, orderId, checkFlat, ChangeTableItems.this);
                recyclerView.setAdapter(ChangeTableItemAdapter);
            }
        });
    }

    public static String removeSpecialCharacters(String input) {
        // Chuẩn hóa chuỗi đầu vào và loại bỏ dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Chuyển đổi chữ Đ thành d
        normalized = normalized.replace("Đ", "d");

        normalized = normalized.replace(" ", ""); // Loại bỏ khoang trang

        return normalized;
    }

    private void addControl() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        inputSearch = (LinearLayout) findViewById(R.id.inputSearch);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);
        btnCloseSearch = (ImageView) findViewById(R.id.btnCloseSearch);
        searchEditText = (EditText) findViewById(R.id.et_search);
        txtNameTable = (TextView) findViewById(R.id.txtNameTable);
    }

    public void initView(String isNameTable) {
        recyclerView = findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);

        //select data from api
        TableSelectByUserIdService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(TableSelectByUserIdService.class);
        Call<String> call = apiService.getTable(isShopId, isNameTable);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = Integer.parseInt(jsonObject.getString("id"));
                            String tableName = jsonObject.getString("table_name");
                            int status = Integer.parseInt(jsonObject.getString("status"));
                            float table_price = Float.parseFloat(jsonObject.getString("table_price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(table_price);


                            String userIdString = jsonObject.getString("user_id");
                            int userId = 0; // Giá trị mặc định nếu không thể chuyển đổi
                            if (userIdString != null && !userIdString.equals("null") && !userIdString.isEmpty()) {
                                userId = Integer.parseInt(userIdString);
                            }
                            if (id == idTable) {
                                arrayList.add(0, new Table(id, tableName, status, userId, formattedPrice));
                            }
                            if (status == 0 && table_price == 0) {
                                arrayList.add(new Table(id, tableName, status, userId, formattedPrice));
                            }

                        }
                        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
                        ChangeTableItemAdapter ChangeTableItemAdapter = new ChangeTableItemAdapter(arrayList, ChangeTableItems.this, idTable, orderId, checkFlat, ChangeTableItems.this); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(ChangeTableItemAdapter);
                        ChangeTableItemAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        // Không thực hiện hành động nào khi nút quay trở lại được nhấn
//        super.onBackPressed();
    }

    @Override
    public void onItemDeleted() {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public void update_totalQuantity_totalPrice(int quantity, float priceTotal) {

    }

    @Override
    public void notification_insertOrder(int idTable, String nameTable) {
        this.nameTable = nameTable;
        openNotificationDialog(idTable, nameTable);
    }

    @Override
    public void notification_arrIdDeleteSize(int arrIdDelete) {

    }

    public void openNotificationDialog(int idTable, String nameTable) {
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
        Button btnHuy = (Button) dialog.findViewById(R.id.btn_huy);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btn_xacNhan);

        txtContent.setText("Xác nhận tách đơn tới - " + nameTable);

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
                    insertOrder("11177575", "60-dayfreetrial", idTable);
                    dialog.dismiss();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    public void insertOrder(String username, String password, int idTable) throws ParseException {
        Date currentDate = new Date();

        // Định dạng thời gian hiện tại thành chuỗi theo định dạng "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(currentDate);
        String code = generateRandomCode();
        int userId = isTableUserId;
        idTableupdate = idTable;

        OrderInsertService service = OrderInsertApiClient.createService(username, password);
        Call<String> call = service.insertOrder(formattedDateTime, code, idTable, userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // kiểm tra Retrofit đã hoàn thành
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        newOrderId = jsonObject.getInt("orderId");
                        insertOrder_items("11177575", "60-dayfreetrial");

                    } catch (JSONException e) {
                        e.printStackTrace();
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

    public void insertOrder_items(String username, String password) throws ParseException {

        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<TachDon> tachDons = sessionManager.getTachDon();

        int numberOfOrders = tachDons.size(); // Số lượng đơn hàng cần thêm vào

        // Biến đếm số lượng Retrofit đã hoàn thành
        AtomicInteger retrofitCallCounter = new AtomicInteger(0);

        for (TachDon tachDon : tachDons) {
            int quantity = tachDon.getNewQuantity();
            String priceString = String.valueOf(tachDon.getPriceProduct());
            priceString = priceString.replace(".0", ""); // Loại bỏ dấu chấm
            int price = Integer.parseInt(priceString) * quantity;
            int order_id = newOrderId;
            int product_id = tachDon.getProductId();

            // tính tổng giá của bàn
            tableTotalPrice += price;


            OrderInsertItemsService service = OrderInsertItemsApiClient.createService(username, password);
            Call<String> call = service.insertOrderItem(quantity, price, order_id, product_id);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        // Tăng biến đếm sau mỗi Retrofit thành công
                        int counter = retrofitCallCounter.incrementAndGet();

                        // Kiểm tra xem tất cả các cuộc gọi Retrofit đã hoàn thành chưa
                        if (counter == numberOfOrders) {
                            // Nếu tất cả các cuộc gọi Retrofit đã hoàn thành, chuyển màn hình mới
                            try {
                                UpdateId_table_Or_UpdateOrder_items("11177575", "60-dayfreetrial");
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
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

    public void UpdateId_table_Or_UpdateOrder_items(String username, String password) throws ParseException {
        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<TachDon> tachDons = sessionManager.getTachDon();

        int newItemCount = 0;
        int newItemsize = 0;
        for (int i = 0; i < tachDons.size(); i++) {
            TachDon tachDon = tachDons.get(i);
            newItemCount++;

            DeleteItemOfOrderService service = DeleteItemOfOrderAPI.createService(username, password);
            if (tachDon.getQuantity() == tachDon.getNewQuantity()) {
                newItemsize++;
                Call<String> call = service.deleteItemOfOrder(tachDon.getIdOrderItem());
                handleResponse(call, true, newItemCount, newItemsize, tachDons.size(), username, password);
            } else {
                // Cập nhật số lượng và giá của mục
                UpdateItemOfOrderService updateService = UpdateItemOfOrderAPI.createService(username, password);
                Call<String> call = updateService.updateItemOfOrder(tachDon.getIdOrderItem(), String.valueOf(tachDon.getPriceProduct() * (tachDon.getQuantity() - tachDon.getNewQuantity())), String.valueOf(tachDon.getQuantity() - tachDon.getNewQuantity()));
                handleResponse(call, false, newItemCount, newItemsize, tachDons.size(), username, password);
            }
        }
    }

    private void handleResponse(Call<String> call, boolean isDelete, int newItemCount, int newItemsize, int totalItems, String username, String password) {
        isDelteOrders.add(isDelete);
        final boolean[] checked = new boolean[1];
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + isDelete);
                    if (newItemCount == totalItems) {
                        for (Boolean isDelteOrder : isDelteOrders) {
                            if (isDelteOrder) {
                                checked[0] = true;
                            } else {
                                checked[0] = false;
                                break;
                            }
                        }
                        Log.d("TAG", "onResponse1: " + checked[0]);

                        if (checked[0] && itemSize == newItemsize) {
                            try {
                                deleteOrder(username, password);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            Log.d("TAG", "onResponse: " + "đã chọn all");
                        } else {
                            updateTable(username, password);
                        }
                    }
                } else {
                    Log.e("TAG", "API call failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "API call failed: " + t.getMessage());
            }
        });
    }

    public void deleteOrder(String username, String password) throws ParseException {

        Log.d("TAG", "deleteOrder: " + idOrderByDelete);
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
        Log.d("TAG", "isUpdateStatusTable: " + "hhhh");
        int id = idTable;
        double status = 0;
        float table_price = 0;

        TableUpdateStatusService service = TableUpdateStatusApiClient.createService(username, password);
        Call<String> call = service.updateData(id, status, table_price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    navigateToTableDetailActivity();
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

    private void updateTable(String username, String password) {
        String priceString = String.valueOf(totalPriceAll).replace(".0", ""); // Loại bỏ dấu chấm

        int id = idTable;
        double status = 1;
        float table_price = Integer.parseInt(priceString) - tableTotalPrice;

        TableUpdateStatusService service = TableUpdateStatusApiClient.createService(username, password);
        Call<String> call = service.updateData(id, status, table_price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    navigateToTableDetailActivity();
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


    private void navigateToTableDetailActivity() {
        Intent intent = new Intent(ChangeTableItems.this, TableDetailActivity.class);
        intent.putExtra("nameTable", nameTable);
        intent.putExtra("idTable", idTableupdate);
        intent.putExtra("finish_activity", true);
        startActivity(intent);
        finish();

        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.removeTachDonAll();
        sessionManager.removeBillAll();
    }


    public static String generateRandomCode() {
        Random rand = new Random();

        // Tạo ba số ngẫu nhiên từ 100 đến 999
        int num1 = rand.nextInt(900) + 100;
        int num2 = rand.nextInt(900) + 100;

        // Kết hợp các số và dấu "-" để tạo mã
        String code = num1 + "-" + num2;

        return code;
    }
}