package com.example.kiotviet_fake.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.BillAdminAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.Bills_Admin;
import com.example.kiotviet_fake.database.select.HistoryService;
import com.example.kiotviet_fake.models.Bill_Admin;
import com.example.kiotviet_fake.models.History;
import com.example.kiotviet_fake.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminHoaDon extends Fragment {
    private ArrayList<Bill_Admin> Bill_AdminArayList;
    private BillAdminAdapter billAdminAdapter;
    private RecyclerView recyclerView_bill;
    private TextView totalAmountTextView,dayMonthYearr,dayMonthYearr2;
    LinearLayout xemTheoLich;
    String id_shop;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    private NumberPicker startDayPicker, startMonthPicker, startYearPicker;
    private NumberPicker endDayPicker, endMonthPicker, endYearPicker;
    public FragmentAdminHoaDon() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_admin_hoa_don, container, false);
        recyclerView_bill = view.findViewById(R.id.listView_bill);

        totalAmountTextView = view.findViewById(R.id.total_all_bills);

        xemTheoLich = view.findViewById(R.id.xemTheoLich);
        dayMonthYearr =view.findViewById(R.id.dayMonthYearr);
        dayMonthYearr2 = view.findViewById(R.id.dayMonthYearr2);




        Bill_AdminArayList = new ArrayList<>();
        billAdminAdapter = new BillAdminAdapter(Bill_AdminArayList, getContext());
        recyclerView_bill.setAdapter(billAdminAdapter);

        // Thiết lập LayoutManager cho RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_bill.setLayoutManager(layoutManager);


        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        id_shop = sharedPreferences.getString("shop_id","");
        System.out.println("test id_shop : "+ id_shop);

        xemTheoLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoadDataHoaDon(0,0,0,0,0,0); // Di chuyển hàm gọi LoadDataHoaDon() sau khi adapter được thiết lập
        addDividerToRecyclerView();

    }
    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn kiểu xem");

        String[] options = {"Xem theo ngày", "Xem từ ngày đến ngày", "Xem tất cả bill"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDayPickerDialog();
                        break;
                    case 1:
                       showDayToDayPickerDialog();
                        break;
                    case 2:
                        dayMonthYearr.setText("Xem tất cả");

                        LoadDataHoaDon(0,0,0,0,0,0);

                        break;
                }
            }

        });

        builder.show();
    }

    private void showDayToDayPickerDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.day_month_year, null);
        NumberPicker startDayPicker = dialogView.findViewById(R.id.dayPicker);
        NumberPicker startMonthPicker = dialogView.findViewById(R.id.monthPicker);
        NumberPicker startYearPicker = dialogView.findViewById(R.id.yearPicker);

        NumberPicker endDayPicker = dialogView.findViewById(R.id.endDayPicker);
        NumberPicker endMonthPicker = dialogView.findViewById(R.id.endMonthPicker);
        NumberPicker endYearPicker = dialogView.findViewById(R.id.endYearPicker);

        // Thiết lập giá trị cho NumberPickers
        startDayPicker.setMinValue(1);
        startDayPicker.setMaxValue(31);
        startDayPicker.setValue(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)); // Ngày hiện tại
        startDayPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị

        startMonthPicker.setMinValue(1);
        startMonthPicker.setMaxValue(12);
        startMonthPicker.setValue(Calendar.getInstance().get(Calendar.MONTH) + 1); // Tháng hiện tại
        startMonthPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị

        startYearPicker.setMinValue(1900); // Năm bắt đầu
        startYearPicker.setMaxValue(2100); // Năm kết thúc
        startYearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR)); // Năm hiện tại
        startYearPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị

        // Thiết lập giá trị cho NumberPickers
        endDayPicker.setMinValue(1);
        endDayPicker.setMaxValue(31);
        endDayPicker.setValue(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)); // Ngày hiện tại
        endDayPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị

        endMonthPicker.setMinValue(1);
        endMonthPicker.setMaxValue(12);
        endMonthPicker.setValue(Calendar.getInstance().get(Calendar.MONTH) + 1); // Tháng hiện tại
        endMonthPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị

        endYearPicker.setMinValue(1900); // Năm bắt đầu
        endYearPicker.setMaxValue(2100); // Năm kết thúc
        endYearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR)); // Năm hiện tại
        endYearPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        builder.setTitle("Chọn ngày,tháng và năm");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedStartDay = startDayPicker.getValue();
                int selectedStartMonth = startMonthPicker.getValue();
                int selectedStartYear = startYearPicker.getValue();

                int selectedEndDay = endDayPicker.getValue();
                int selectedEndMonth = endMonthPicker.getValue();
                int selectedEndYear = endYearPicker.getValue();

                if (isDateValid(selectedStartYear, selectedStartMonth, selectedStartDay, selectedEndYear, selectedEndMonth, selectedEndDay)) {
                    dayMonthYearr.setText(String.format("%02d/%02d/%d đến %02d/%02d/%d ", selectedStartDay, selectedStartMonth, selectedStartYear, selectedEndDay, selectedEndMonth, selectedEndYear));

                    // Gọi phương thức để truy vấn dữ liệu dựa trên ngày được chọn
                    LoadDataHoaDon(selectedStartYear, selectedStartMonth, selectedStartDay, selectedEndYear, selectedEndMonth, selectedEndDay);
                } else {
                    // Hiển thị thông báo cho người dùng nếu ngày bắt đầu lớn hơn ngày kết thúc
                    Toast.makeText(requireContext(), "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isDateValid(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        // Kiểm tra năm bắt đầu có nhỏ hơn hoặc bằng năm kết thúc
        if (startYear > endYear) {
            return false;
        } else if (startYear == endYear) {
            // Nếu năm bắt đầu bằng năm kết thúc, kiểm tra tháng bắt đầu
            if (startMonth > endMonth) {
                return false;
            } else if (startMonth == endMonth) {
                // Nếu tháng bắt đầu bằng tháng kết thúc, kiểm tra ngày bắt đầu
                return startDay <= endDay;
            }
        }
        return true;
    }

    private void showDayPickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Không cần thực hiện gì ở đây vì bạn chỉ muốn chọn tháng và năm
                        dayMonthYearr.setText(String.format("%02d/%02d/%d", dayOfMonth,month + 1, year));

                        LoadDataHoaDon(year,  month + 1, dayOfMonth,0,0,0);

                    }
                }, year, month, 1); // 1: ngày mặc định sẽ hiển thị, có thể là bất kỳ ngày nào vì nó sẽ bị ẩn đi

        // Ẩn bảng chọn ngày và hiển thị spinner cho tháng và năm
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        datePickerDialog.show();
    }
    private void addDividerToRecyclerView() {
        // Thêm dường viền vào RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_bill.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView_bill.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView_bill.addItemDecoration(dividerItemDecoration);
    }

    public void LoadDataHoaDon(int startYear, int startMonth, int startDayOfMonth,int endYear, int endMonth, int endDayOfMonth) {
        Bills_Admin billAdmin =  RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(Bills_Admin.class);


        Call<String> call = billAdmin.getBills_Admin(id_shop);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        Bill_AdminArayList.clear();
                        double totalAmount = 0;
                        int countBill = 0;
                        // Định dạng của chuỗi datetime
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat outputYearFormat = new SimpleDateFormat("yyyy");
                        SimpleDateFormat outputMonthFormat = new SimpleDateFormat("MM");
                        SimpleDateFormat outputDatFormat = new SimpleDateFormat("dd");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id_bill = jsonObject.getInt("id_bill");

                            String dateTime = jsonObject.getString("dateTime");
                            String dateTime_end = jsonObject.getString("dateTimeEnd");
                            String code = jsonObject.getString("code");
                            int table_id = jsonObject.getInt("tableId");
                            int user_id = jsonObject.getInt("userId");
                            double total_price = jsonObject.getDouble("total_price");
                            String name_user = jsonObject.getString("name_user");
                            String shop_id = jsonObject.getString("id_shop");

                            // Chuyển đổi chuỗi datetime thành đối tượng Date
                            Date datetime = inputFormat.parse(dateTime_end);
                            // Lấy năm và tháng từ đối tượng Date
                            String Year = outputYearFormat.format(datetime);
                            String Month = outputMonthFormat.format(datetime);
                            String Day = outputDatFormat.format(datetime);


                            int billYear = Integer.parseInt(Year);
                            int billMonth = Integer.parseInt(Month);
                            int billDay = Integer.parseInt(Day);

                            if(id_shop.equals(shop_id )){
                                // Kiểm tra xem hóa đơn có nằm trong khoảng thời gian từ 12/3/2024 đến 12/7/2024 không

                                if(startYear != 0 && startMonth != 0 && startDayOfMonth != 0 && endYear == 0 && endMonth == 0 && endDayOfMonth == 0){

                                    if (billYear == startYear && billMonth == startMonth && billDay == startDayOfMonth ){
                                        totalAmount += total_price;
                                        countBill++;
                                        Bill_Admin billsAdmin = new  Bill_Admin(id_bill, dateTime, dateTime_end, code, table_id, user_id, total_price, name_user,shop_id);
                                        Bill_AdminArayList.add(billsAdmin);
                                    }

                                } else{
                                    if(startYear != 0 && startMonth != 0 && startDayOfMonth != 0 && endYear != 0 && endMonth != 0 && endDayOfMonth != 0){
                                        // Kiểm tra xem hóa đơn có nằm trong khoảng thời gian từ 12/3/2024 đến 12/7/2024 không
                                        if ((billYear > startYear || (billYear == startYear && billMonth > startMonth) || (billYear == startYear && billMonth == startMonth && billDay >= startDayOfMonth)) &&
                                                (billYear < endYear || (billYear == endYear && billMonth < endMonth) || (billYear == endYear && billMonth == endMonth && billDay <= endDayOfMonth))) {
                                            totalAmount += total_price;
                                            countBill++;
                                            Bill_Admin billsAdmin = new Bill_Admin(id_bill, dateTime, dateTime_end, code, table_id, user_id, total_price, name_user, shop_id);
                                            Bill_AdminArayList.add(billsAdmin);
                                        }

                                    } else {
                                        totalAmount += total_price;
                                        countBill++;

                                        Bill_Admin billsAdmin = new  Bill_Admin(id_bill, dateTime, dateTime_end, code, table_id, user_id, total_price, name_user,shop_id);
                                        Bill_AdminArayList.add(billsAdmin);
                                    }


                                }

                            }


                        }
                        dayMonthYearr2.setText( countBill +" hóa đơn");
                        String formattedTotalAmount = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalAmount);
                        totalAmountTextView.setText(formattedTotalAmount + " VND");
                        // Cập nhật dữ liệu mới cho adapter
                        billAdminAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}