package com.example.kiotviet_fake.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.AdminProductUpdateAndInsertActivity;
import com.example.kiotviet_fake.activities.ChangerCategoriesActivity;
import com.example.kiotviet_fake.database.deleteCategory.CategoryDeleteApiClient;
import com.example.kiotviet_fake.database.deleteCategory.CategoryDeleteService;
import com.example.kiotviet_fake.database.insertCategory.CategoryInsertApiClient;
import com.example.kiotviet_fake.database.insertCategory.CategoryInsertService;
import com.example.kiotviet_fake.database.updateCategory.UpdateCategoryByIdAPI;
import com.example.kiotviet_fake.database.updateCategory.UpdateCategoryByIdService;
import com.example.kiotviet_fake.interface_main.AdapterChangerCategories;
import com.example.kiotviet_fake.models.Category;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeCategoriesAdapter extends RecyclerView.Adapter<ChangeCategoriesAdapter.viewHolder> {
    ArrayList<Category> categories;
    Context context;
    AdapterChangerCategories adapterChangerCategories;
    String input_content, categories_name, checkFlat;

    public ChangeCategoriesAdapter(ArrayList<Category> categories, Context context, AdapterChangerCategories adapterChangerCategories, String categories_name, String checkFlat) {
        this.categories = categories;
        this.context = context;
        this.adapterChangerCategories = adapterChangerCategories;
        this.categories_name = categories_name;
        this.checkFlat = checkFlat;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_categories, parent, false);
        return new viewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Category category = categories.get(position);
        if (categories_name != null) {
            if (categories_name.equals(category.getName())) {
                holder.txtNameCategory.setTextColor(Color.parseColor("#0365ca"));
            }
        }
        holder.txtNameCategory.setText(category.getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (checkFlat) {
                    case "add":
                    case "update":
                        adapterChangerCategories.finishActivity(category.getName(), category.getId());
                        break;
                    case "manage":
                        onDialogSelect(position);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void onDialogSelect(int position) {
        Category category = categories.get(position);
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_category);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Set gravity to bottom
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
        }

        dialog.show();

        TextView btnXoa = dialog.findViewById(R.id.btn_xoa);
        TextView btnSua = dialog.findViewById(R.id.btn_sua);
        TextView btnHuy = dialog.findViewById(R.id.btn_huy);

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogUpdateNameCategory(category.getId(), category.getName(), category);
                dialog.dismiss();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotificationDialog(category.getId(), category.getName(), category);
                dialog.dismiss();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void openNotificationDialog(int idCategory, String nameCategory, Category category) {
        Dialog dialog = new Dialog(context);
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

        txtContent.setText("Xác nhận xoá - " + nameCategory);
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
                deleteCategory("11177575", "60-dayfreetrial", idCategory, category);
                dialog.dismiss();
            }
        });


    }

    public void onDialogUpdateNameCategory(int idCategory, String nameCategory, Category category) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_and_add_category);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText etNameCategory = (EditText) dialog.findViewById(R.id.et_nameCategory);
        Button btnHuy = (Button) dialog.findViewById(R.id.btn_huy);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btn_xacNhan);

        etNameCategory.setText(nameCategory);
        input_content = nameCategory;

        dialog.show();

        etNameCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim().toUpperCase();
                input_content = content;
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input_content.isEmpty()) {
                    updateCategory("11177575", "60-dayfreetrial", idCategory, category);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Vui lòng nhập tên loại hàng", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void deleteCategory(String username, String password, int id, Category category) {
        CategoryDeleteService service = CategoryDeleteApiClient.createService(username, password);
        Call<String> call = service.deleteCategory(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    categories.remove(category);
                    adapterChangerCategories.updateSizeCategories(categories.size());
                    notifyDataSetChanged();
                } else {
                    if (response.code() == 400) {
                        // Xử lý lỗi 400
                        openNotificationDialog();
                    } else {
                        // Xử lý các mã lỗi khác nếu cần
                    } // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    public void openNotificationDialog() {
        Dialog dialog = new Dialog(context);
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

        txtContent.setText("Không thể xóa loại hàng này vì đang liên kết với sản phẩm. Vui lòng thay đổi liên kết sản phẩm trước và thử lại.");
        txtTitle.setVisibility(View.GONE);
        btnHuy.setAlpha(0);

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
                dialog.dismiss();
            }
        });


    }

    private void updateCategory(String username, String password, int id, Category category) {
        UpdateCategoryByIdService service = UpdateCategoryByIdAPI.createService(username, password);
        Call<String> call = service.updateCategoryById(id, input_content);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    category.setName(input_content);
                    notifyDataSetChanged();
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

    public int getItemCount() {
        return categories.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtNameCategory;
        LinearLayout item;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCategory = itemView.findViewById(R.id.tv_nameCategory);
            item = itemView.findViewById(R.id.item);
        }
    }


}