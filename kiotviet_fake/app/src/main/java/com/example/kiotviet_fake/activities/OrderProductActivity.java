package com.example.kiotviet_fake.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.insertOrders.OrderInsertApiClient;
import com.example.kiotviet_fake.database.insertOrderItems.OrderInsertItemsService;
import com.example.kiotviet_fake.database.insertOrders.OrderInsertService;
import com.example.kiotviet_fake.database.insertOrderItems.OrderInsertItemsApiClient;
import com.example.kiotviet_fake.database.select.OrdersSelectService;
import com.example.kiotviet_fake.fragments.FragmentCategoriesOrder;
import com.example.kiotviet_fake.fragments.FragmentCategoriesTatCa;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.session.SessionManager;
import com.google.android.material.tabs.TabLayout;

import android.view.ViewTreeObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderProductActivity extends AppCompatActivity {
    private ViewPager pager;
    private TabLayout tabLayout;

    TextView txtNameTable, btnThemVaoDon, bntChonLai;
    ImageView btnCancel, btnSearch, btnCloseSearch;
    LinearLayout inputSearch, container_order_3;
    EditText searchEditText;

    int idTable;
    String nameTable;
    int newOrderId;

    int tableTotalPrice;
    int isTableUserId;
    ProgressBar progressBar;

    private BroadcastReceiver updateReceiver;

    FragmentCategoriesOrder fragmentCategoriesOrder;
    FragmentCategoriesTatCa fragmentCategoriesTatCa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        // lấy ra userId vừa dc truyền khi login thành công
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isTableUserId = sharedPreferences.getInt("userId", 0);

        // Đăng ký BroadcastReceiver
        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onProductClick();
            }
        };
        IntentFilter filter = new IntentFilter("ACTION_UPDATE");
        registerReceiver(updateReceiver, filter);

        addControl();
        onFragment(savedInstanceState);
        updateUI();
        btnClick();
        onProductClick();
    }

    private void onFragment(Bundle savedInstanceState) {
        progressBar.setVisibility(View.VISIBLE);
        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Kiểm tra xem nội dung đã vẽ xong chưa
                if (rootView.getWidth() > 0 && rootView.getHeight() > 0) {
                    // Gọi mã của bạn ở đây
                    if (savedInstanceState == null) {
                        fragmentCategoriesOrder = new FragmentCategoriesOrder();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_product, fragmentCategoriesOrder).commit();
                    }

                    // Loại bỏ bất kỳ lắng nghe nào sau khi đã chạy mã
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    // Thêm listener để kiểm tra khi fragment đã được thêm vào stack
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof FragmentCategoriesOrder) {
            progressBar.setVisibility(View.GONE);
            Log.d("TAG", "Fragment is loaded");
        }
    }


        private void updateUI () {
            Intent intent = getIntent();
            nameTable = intent.getStringExtra("nameTable");
            idTable = intent.getIntExtra("idTable", 0);
            tableTotalPrice = intent.getIntExtra("totalPriceTable", 0);
            Log.d("TAG", "updateUI11111: " + tableTotalPrice);
            newOrderId = intent.getIntExtra("idOrder", 0);
            Log.e("TAG", "updateUI: " + newOrderId);

            txtNameTable.setText(nameTable);

        }

        private void addControl () {
            pager = (ViewPager) findViewById(R.id.view_pager);
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            btnCancel = (ImageView) findViewById(R.id.btnCancel);
            txtNameTable = (TextView) findViewById(R.id.txtNameTable);
            btnThemVaoDon = (TextView) findViewById(R.id.btnThemVaoDon);
            bntChonLai = (TextView) findViewById(R.id.bntChonLai);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            btnSearch = (ImageView) findViewById(R.id.btnSearch);
            inputSearch = (LinearLayout) findViewById(R.id.inputSearch);
            btnCloseSearch = (ImageView) findViewById(R.id.btnCloseSearch);
            searchEditText = findViewById(R.id.et_search);
            container_order_3 = findViewById(R.id.container_order_3);
        }

        public void btnClick () {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // xoá session order
                    SessionManager sessionManager = SessionManager.getInstance();
                    sessionManager.removeOrderAll();

                    finish();
                }
            });

            btnThemVaoDon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // thêm hiệu ứng loading
                    progressBar.setVisibility(View.VISIBLE);
                    selectOrders();

                }
            });
            bntChonLai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //reset lại trang
                    Intent intent = getIntent();
                    startActivity(intent);
                    finish();


                    SessionManager sessionManager = SessionManager.getInstance();
                    sessionManager.removeOrderAll();
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

//                tabLayout.setVisibility(View.GONE);
//                pager.setCurrentItem(0, true);

                    // Hiển thị bàn phím
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);

                    //ẩn fragmentHome
                    if (fragmentCategoriesOrder != null) {
                        getSupportFragmentManager().beginTransaction().hide(fragmentCategoriesOrder).commit();
                    }

                    fragmentCategoriesTatCa = new FragmentCategoriesTatCa();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_product, fragmentCategoriesTatCa)
                            .commit();
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

                    // Ẩn bàn phím
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

                    //ẩn fragmentTatCa
                    if (fragmentCategoriesTatCa != null) {
                        getSupportFragmentManager().beginTransaction().hide(fragmentCategoriesTatCa).commit();
                    }

                    // Hiển thị lại fragment home
                    if (fragmentCategoriesOrder == null) {
                        fragmentCategoriesOrder = new FragmentCategoriesOrder(); // Tạo mới fragment home
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_product, fragmentCategoriesOrder, "fragmentCategoriesOrder")
                                .commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .show(fragmentCategoriesOrder)
                                .commit();
                    }

//                tabLayout.setVisibility(View.VISIBLE);
                }
            });
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String keyword = s.toString().trim();
                    if (fragmentCategoriesTatCa != null) {
                        getSupportFragmentManager().beginTransaction().show(fragmentCategoriesTatCa).commit();
                        fragmentCategoriesTatCa.performSearch(keyword);
                    } else {
                        Log.e("ERRRR OrderProduct:", "FragmentCategoriesTatCa  is null");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

        public void selectOrders () {
            //select data from api
            OrdersSelectService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(OrdersSelectService.class);
            Call<String> call = apiService.getOrders();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        try {
                            boolean isInsert = false;
                            JSONArray jsonArray = new JSONArray(response.body());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
//                            String dateTime = jsonObject.getString("dateTime");
//                            String code = jsonObject.getString("code");
                                int table_id = jsonObject.getInt("table_id");
                                int user_id = jsonObject.getInt("user_id");
                                if (table_id == idTable && user_id == isTableUserId) {
                                    isInsert = true;
                                }
                            }
                            if (isInsert) {
                                insertOrder_items("11177575", "60-dayfreetrial");
                                Log.d("TAG", "onResponse: " + idTable + " " + isTableUserId);
                            } else {
                                insertOrder("11177575", "60-dayfreetrial");
                            }
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

        public void insertOrder (String username, String password) throws ParseException {
            Date currentDate = new Date();

            // Định dạng thời gian hiện tại thành chuỗi theo định dạng "yyyy-MM-dd HH:mm:ss"
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = formatter.format(currentDate);
            String code = generateRandomCode();
            int tableId = idTable;
            int userId = isTableUserId;

            OrderInsertService service = OrderInsertApiClient.createService(username, password);
            Call<String> call = service.insertOrder(formattedDateTime, code, tableId, userId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    // kiểm tra Retrofit đã hoàn thành
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            newOrderId = jsonObject.getInt("orderId");
                            Log.e("TAG", "thong: " + newOrderId);
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

        public void insertOrder_items (String username, String password) throws ParseException {

            SessionManager sessionManager = SessionManager.getInstance();
            ArrayList<Order> orders = sessionManager.getOrders();

            int numberOfOrders = orders.size(); // Số lượng đơn hàng cần thêm vào

            // Biến đếm số lượng Retrofit đã hoàn thành
            AtomicInteger retrofitCallCounter = new AtomicInteger(0);
// them vong lap kiem tra xem có sản phẩm đó trong bàn chk
            for (Order order : orders) {
                int quantity = order.getQuantity();
                String priceString = order.getPrice();
                priceString = priceString.replace(".", ""); // Loại bỏ dấu chấm
                int price = Integer.parseInt(priceString) * order.getQuantity();
                int order_id = newOrderId;
                Log.e("TAG", "insertOrder_items: " + priceString);
                int product_id = order.getProductId();

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
                                //xoá tất cả product đã chọn khi nhấn thêm vào đơn
                                sessionManager.removeOrderAll();
                                // Nếu tất cả các cuộc gọi Retrofit đã hoàn thành, chuyển màn hình mới
                                navigateToTableDetailActivity();
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

        private void navigateToTableDetailActivity () {
            SessionManager sessionManager = SessionManager.getInstance();
            sessionManager.removeBillAll();
            runInitViewTable();

            Intent intent = new Intent(OrderProductActivity.this, TableDetailActivity.class);
            intent.putExtra("idTable", idTable);
            intent.putExtra("nameTable", nameTable);
            intent.putExtra("idOrder", newOrderId);
            startActivity(intent);
            finish();

//        // ẩn hiệu ứng loading
//        progressBar.setVisibility(View.GONE);
        }


        public static String generateRandomCode () {
            Random rand = new Random();

            // Tạo ba số ngẫu nhiên từ 100 đến 999
            int num1 = rand.nextInt(900) + 100;
            int num2 = rand.nextInt(900) + 100;

            // Kết hợp các số và dấu "-" để tạo mã
            String code = num1 + "-" + num2;

            return code;
        }

    private void runInitViewTable() {
        Intent intent = new Intent("FINISH_ACTIVITY_TABLE_DETAIL");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

        @Override
        public void onBackPressed () {
            // Không thực hiện hành động nào khi nút quay trở lại được nhấn
//        super.onBackPressed();
        }

        public void onProductClick () {
            SessionManager sessionManager = SessionManager.getInstance();
            ArrayList<Order> orders = sessionManager.getOrders();
            if (orders.size() == 0) {
                container_order_3.setVisibility(View.GONE);
            } else {
                container_order_3.setVisibility(View.VISIBLE);
            }
        }


    }