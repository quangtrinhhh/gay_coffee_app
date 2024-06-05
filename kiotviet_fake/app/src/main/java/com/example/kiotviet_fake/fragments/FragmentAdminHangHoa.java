package com.example.kiotviet_fake.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.AdminProductUpdateAndInsertActivity;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.adapters.ProductAdminAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.ProductSelectService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminHangHoa extends Fragment implements AdapterListener {
    private ProductAdminAdapter productAdminAdapter;
    private ArrayList<Product> arrayProducts = new ArrayList<>();
    private View view;

    RecyclerView recyclerView;
    TextView txtCountProduct, txtTitle;
    ImageView btnClose, btnSearch, btnClear, btnThem;

    String isShopId;

    public FragmentAdminHangHoa() {
    }

    // chạy lại initview()
    private BroadcastReceiver closeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initView();
            Log.d("TAG", "onReceive: fg hang hoa ");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_fragment_admin_hang_hoa, container, false);
        txtCountProduct = view.findViewById(R.id.txtCountProduct);
        txtTitle = view.findViewById(R.id.txtTitle);
        btnClose = view.findViewById(R.id.btnClose);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnClear = view.findViewById(R.id.btnClear);
        btnThem = view.findViewById(R.id.btnThem);

        return view;
    }

    @Override
    public void onDestroy() {
        // Hủy đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(closeReceiver);
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        isShopId = sharedPreferences.getString("shop_id", "");

        recyclerView = view.findViewById(R.id.recycler_view); // Đảm bảo RecyclerView đã được tìm thấy trong layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // Thêm đường phân chia giữa các mục trong RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(closeReceiver, new IntentFilter("RUN_INIT_VIEW"));

        initView();
        btnClick();
    }

    private void btnClick() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClear.setVisibility(View.GONE);
                btnClose.setVisibility(View.GONE);
                btnSearch.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.VISIBLE);
                btnThem.setVisibility(View.VISIBLE);
                productAdminAdapter.closeSelection();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiển thị dialog xác nhận
                openNotificationDialog();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdminProductUpdateAndInsertActivity.class);
                intent.putExtra("checkFlat", "add");
                startActivity(intent);
            }
        });


    }

    public void openNotificationDialog() {
        Dialog dialog = new Dialog(getContext());
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

        txtContent.setText("Xác nhận xoá sản phẩm");
        txtTitle.setVisibility(View.GONE);

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
                productAdminAdapter.clearSelection("11177575", "60-dayfreetrial");
                dialog.dismiss();
            }
        });


    }

    public void initView() {
        //select data from api
        ProductSelectService apiService = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(ProductSelectService.class);
        Call<String> call = apiService.getProducts(isShopId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        ArrayList<Product> products = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("product_name");
                            float price = Float.parseFloat(jsonObject.getString("price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(price);

                            int quantity = jsonObject.getInt("quantity");
                            String categoriesName = jsonObject.getString("categories_name");
                            String product_code = jsonObject.getString("product_code");
                            int categories_id = jsonObject.getInt("categories_id");

                            // sửa đổi thêm điều kiện userid và idcategories = user_id split(_) userId[1]
                            String idProductItem = id + "Tất Cả";
                            Product product = new Product(id, idProductItem, name, formattedPrice, quantity, 1, 0, null, 0, categoriesName, product_code, categories_id);
                            products.add(product);
                            arrayProducts.add(product);
                            ;
                        }
                        txtCountProduct.setText(jsonArray.length() + " hàng hoá");

                        productAdminAdapter = new ProductAdminAdapter(products, requireContext(), FragmentAdminHangHoa.this);
                        recyclerView.setAdapter(productAdminAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý lỗi khi không nhận được phản hồi từ API
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi khi gọi API không thành công
            }
        });

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

    }

    @Override
    public void notification_arrIdDeleteSize(int arrIdDelete) {
        if (arrIdDelete > 0) {
            btnClear.setVisibility(View.VISIBLE);
            btnClose.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.GONE);
            txtTitle.setVisibility(View.GONE);
            btnThem.setVisibility(View.GONE);
        } else {
            initView();
            btnClear.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);
            btnSearch.setVisibility(View.VISIBLE);
            txtTitle.setVisibility(View.VISIBLE);
            btnThem.setVisibility(View.VISIBLE);
            productAdminAdapter.closeSelection();
        }
    }
}