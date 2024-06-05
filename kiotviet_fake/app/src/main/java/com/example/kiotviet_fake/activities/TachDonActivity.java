package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.TachDonAdapter;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderAPI;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderService;
import com.example.kiotviet_fake.database.deleteOrder.OrderDeleteApiClient;
import com.example.kiotviet_fake.database.deleteOrder.OrderDeleteService;
import com.example.kiotviet_fake.database.deleteOrderItems.OrderDeleteItemsApiClient;
import com.example.kiotviet_fake.database.deleteOrderItems.OrderDeleteItemsService;
import com.example.kiotviet_fake.database.insertBillItems.BillsInsertItemsApiClient;
import com.example.kiotviet_fake.database.insertBillItems.BillsInsertItemsService;
import com.example.kiotviet_fake.database.insertBills.BillsInsertApiClient;
import com.example.kiotviet_fake.database.insertBills.BillsInsertService;
import com.example.kiotviet_fake.database.updateItemOfProduct.UpdateItemOfOrderAPI;
import com.example.kiotviet_fake.database.updateItemOfProduct.UpdateItemOfOrderService;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusApiClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.TachDon;
import com.example.kiotviet_fake.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TachDonActivity extends AppCompatActivity implements AdapterListener {
    ImageView btnCancel;
    Button btnThanhToan, btnTachDon;
    TextView txtNameTable, txtQuantityProduct, txtTotalPrice;

    int idTable;
    String nameTable;
    float totalPrice = 0;
    float totalPriceAll = 0;
    TachDonAdapter tachDonAdapter;
    int newBillId = 0;
    String dateTime,role;
    int isTableUserId;
    int itemSize;
    int idOrderByDelete;
    ProgressBar progressBar;

    ArrayList<Boolean> isDelteOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tach_don);

        // lấy ra userId và role vừa dc truyền khi login thành công
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isTableUserId = sharedPreferences.getInt("userId", 0);
        role = sharedPreferences.getString("role", "");


        addControl();
        updateUI();
        btnClick();
        initView();
    }

    private void updateUI() {
        Intent intent = getIntent();
        nameTable = intent.getStringExtra("nameTable");
        dateTime = intent.getStringExtra("dateTime");
        idTable = intent.getIntExtra("idTable", 0);
        itemSize = intent.getIntExtra("itemSize", 0);
        idOrderByDelete = intent.getIntExtra("idOrderByDelete", 0);
        totalPriceAll = intent.getFloatExtra("totalPrice", 0);


        txtNameTable.setText(nameTable);

        switch (role) {
            case "order":
                btnThanhToan.setVisibility(View.GONE);
                break;
            default:
        }

    }

    private void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        txtNameTable = (TextView) findViewById(R.id.txtNameTable);
        txtQuantityProduct = (TextView) findViewById(R.id.tv_quantityProduct);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        btnThanhToan = (Button) findViewById(R.id.btnThanhToan);
        btnTachDon = (Button) findViewById(R.id.btnTachDon);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void btnClick() {
        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<TachDon> tachDons = sessionManager.getTachDon();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = SessionManager.getInstance();
//                sessionManager.removeBillAll();
                sessionManager.removeTachDonAll();
                finish();
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tachDons.size() == 0) {
                    Toast.makeText(TachDonActivity.this, "Chọn sản phẩm", Toast.LENGTH_LONG).show();
                } else {
                    // thêm hiệu ứng loading
                    progressBar.setVisibility(View.VISIBLE);
                    insertBill("11177575", "60-dayfreetrial");
                }
            }
        });
        btnTachDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tachDons.size() == 0) {
                    Toast.makeText(TachDonActivity.this, "Chọn sản phẩm", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(TachDonActivity.this, ChangeTableItems.class);
                    intent.putExtra("checkFlat", "tachDon");
                    intent.putExtra("idTable", idTable);
                    intent.putExtra("itemSize", itemSize);
                    intent.putExtra("totalPriceAll", totalPriceAll);
                    intent.putExtra("idOrderByDelete", idOrderByDelete);
                    startActivity(intent);
                }
            }
        });
    }

    public void initView() {
        //thêm dữ liệu vào sessionManager
        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<Bill> bills = sessionManager.getBills();
        ArrayList<Product> arrayList = new ArrayList<>();
        for (Bill bill : bills) {
            arrayList.add(new Product(bill.getIdOrderItem(), "", bill.getNameProduct(), String.valueOf(bill.getPriceProduct()), 200, bill.getQuantity(), bill.getTableId(), bill.getNameTable(), bill.getProductId(),"","",0));
        }
        RecyclerView recyclerView = findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration); // Thêm dường viền vào RecyclerView

        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
        tachDonAdapter = new TachDonAdapter(arrayList, getApplicationContext(), TachDonActivity.this); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
        recyclerView.setAdapter(tachDonAdapter);
        tachDonAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView

    }

    private void insertBill(String username, String password) {
        //lấy thời gian hiện tại
        Date currentDate = new Date();
        SimpleDateFormat formatterSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatterSimpleDateFormat.format(currentDate);

        String code = generateRandomCode();

        String txtTotal = txtTotalPrice.getText().toString();
        txtTotal = txtTotal.replace(".", "");

        BillsInsertService service = BillsInsertApiClient.createService(username, password);
        Call<String> call = service.insertBills(dateTime, formattedDateTime, code, idTable, isTableUserId, Float.parseFloat(txtTotal));
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
        ArrayList<TachDon> tachDons = sessionManager.getTachDon();

        int totalCalls = tachDons.size();
        final int[] completedCalls = {0};
        for (TachDon tachDon : tachDons) {
            BillsInsertItemsService service = BillsInsertItemsApiClient.createService(username, password);
            Call<String> call = service.insertBillItems(tachDon.getNewQuantity(), tachDon.getTotalPrice(), tachDon.getProductId(),tachDon.getNameProduct(), newBillId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        try {
                            completedCalls[0]++;
                            if (completedCalls[0] == totalCalls) {
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

    public void deleteOrder_items(String username, String password) throws ParseException {
        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<TachDon> tachDons = sessionManager.getTachDon();

        int newItemCount = 0;
        int newItemSize = 0;
        for (int i = 0; i < tachDons.size(); i++) {
            TachDon tachDon = tachDons.get(i);
            newItemCount++;
            DeleteItemOfOrderService service = DeleteItemOfOrderAPI.createService(username, password);

            if (tachDon.getQuantity() == tachDon.getNewQuantity()) {
                newItemSize++;
                Call<String> call = service.deleteItemOfOrder(tachDon.getIdOrderItem());
                handleResponse(call, true, newItemCount, newItemSize, tachDons.size(), username, password);
            } else {
                // Cập nhật số lượng và giá của mục
                UpdateItemOfOrderService updateService = UpdateItemOfOrderAPI.createService(username, password);
                Call<String> call = updateService.updateItemOfOrder(
                        tachDon.getIdOrderItem(),
                        String.valueOf(tachDon.getPriceProduct() * (tachDon.getQuantity() - tachDon.getNewQuantity())),
                        String.valueOf(tachDon.getQuantity() - tachDon.getNewQuantity())
                );
                handleResponse(call, false, newItemCount, newItemSize, tachDons.size(), username, password);
            }
        }
    }

    private void handleResponse(Call<String> call, boolean isDelete, int newItemCount, int newItemSize, int totalItems, String username, String password) {
        isDelteOrders.add(isDelete);
        final boolean[] checked = new boolean[1];
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (newItemCount == totalItems) {
                        for (Boolean isDelteOrder : isDelteOrders) {
                            if (isDelteOrder) {
                                checked[0] = true;
                            } else {
                                checked[0] = false;
                                break;
                            }
                        }
                        if (checked[0] && itemSize == newItemSize) {
                            try {
                                deleteOrder(username, password);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            navigateToTableDetailActivity();
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

    private void navigateToTableMainActivity() {
        Intent intent = new Intent(TachDonActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.removeTachDonAll();
        sessionManager.removeBillAll();
    }

    private void navigateToTableDetailActivity() {
        Intent intent = new Intent(TachDonActivity.this, TableDetailActivity.class);
        intent.putExtra("idTable", idTable);
        intent.putExtra("nameTable", nameTable);
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


    @Override
    public void onItemDeleted() {

    }

    @Override
    public void finishActivity() {

    }


    @Override
    public void update_totalQuantity_totalPrice(int quantity, float priceTotal) {
        totalPrice += priceTotal;
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(totalPrice);
        txtQuantityProduct.setText(quantity + "");
        txtTotalPrice.setText(formattedNumber);
    }

    @Override
    public void notification_insertOrder(int idTable, String nameTable) {

    }

    @Override
    public void notification_arrIdDeleteSize(int arrIdDelete) {

    }


}