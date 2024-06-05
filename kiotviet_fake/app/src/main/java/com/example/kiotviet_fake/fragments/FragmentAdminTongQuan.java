package com.example.kiotviet_fake.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.Bills_Admin;
import com.example.kiotviet_fake.database.select.GetRevenue;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminTongQuan extends Fragment {
    private TextView totalRevenue;
    private TextView totalBill;
    private TextView selectedDateText;
    private String id_shop;
    private List<BarEntry> entries = new ArrayList<>();
    private LinearLayout dayMonthYear;

    public FragmentAdminTongQuan() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_admin_tong_quan, container, false);

        totalRevenue = view.findViewById(R.id.totalRevenue);
        totalBill = view.findViewById(R.id.totalBill);
        dayMonthYear = view.findViewById(R.id.dayMonthYear);
        selectedDateText = view.findViewById(R.id.selectedDateText);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        id_shop = sharedPreferences.getString("shop_id", "");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetRevenueCaNam();
        OnClickButton();
        setupBarChart(view);
    }

    private void OnClickButton() {
        dayMonthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });
    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn kiểu xem");

        String[] options = {"Xem theo tháng", "Xem cả năm"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showMonthPickerDialog();
                        break;
                    case 1:
                         showYearPickerDialog();
                        break;
                }
            }


        });

        builder.show();
    }
    private void showYearPickerDialog() {
        GetRevenueCaNam();
    }
    private void GetRevenueCaNam() {
        GetRevenue getRevenue = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(GetRevenue.class);

        Call<String> call = getRevenue.GetRevenue_Admin(id_shop);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        // Xóa dữ liệu cũ trong entries trước khi thêm dữ liệu mới
                        entries.clear();

                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

//                            String year = jsonObject.getString("year");
                            String Month = jsonObject.getString("Month");
                            Double Total_Revenue = jsonObject.getDouble("TotalRevenue");

                                int monthValue = Integer.parseInt(Month);
                                float Revenue = Total_Revenue.floatValue();
                                entries.add(new BarEntry(monthValue, Revenue));

                        }
                        selectedDateText.setText("Xem cả năm");
                        CountQuantityAndTotalBills(0,0);

                        setupBarChart(getView()); // Cập nhật biểu đồ với dữ liệu mới
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
    private void showMonthPickerDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.month_year_picker_dialog, null);
        NumberPicker monthPicker = dialogView.findViewById(R.id.monthPicker);
        NumberPicker yearPicker = dialogView.findViewById(R.id.yearPicker);

        // Thiết lập giá trị cho NumberPickers
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(Calendar.getInstance().get(Calendar.MONTH) + 1); // Tháng hiện tại
        monthPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearPicker.setMinValue(currentYear - 100); // 100 năm trước
        yearPicker.setMaxValue(currentYear + 100); // 100 năm sau
        yearPicker.setValue(currentYear); // Năm hiện tại
        yearPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        builder.setTitle("Chọn tháng và năm");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedMonth = monthPicker.getValue();
                int selectedYear = yearPicker.getValue();

                // Xử lý logic với tháng và năm đã chọn
                selectedDateText.setText(String.format("%02d/%d", selectedMonth, selectedYear));
                // Gọi phương thức để truy vấn dữ liệu doanh thu cho tháng được chọn
                CountQuantityAndTotalBills(selectedMonth ,selectedYear);
                GetRevenue(selectedMonth, selectedYear);
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void GetRevenue(int selectedMonth, int selectedYear) {

        GetRevenue getRevenue = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(GetRevenue.class);

        Call<String> call = getRevenue.GetRevenue_Admin(id_shop);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        // Xóa dữ liệu cũ trong entries trước khi thêm dữ liệu mới
                        entries.clear();

                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String year = jsonObject.getString("year");
                            String Month = jsonObject.getString("Month");
                            Double Total_Revenue = jsonObject.getDouble("TotalRevenue");
                            if(year.equals(String.valueOf(selectedYear)) && Month.equals(String.valueOf(selectedMonth))){
                                int monthValue = Integer.parseInt(Month);
                                float Revenue = Total_Revenue.floatValue();
                                entries.add(new BarEntry(monthValue, Revenue));

                                if(Total_Revenue.equals(null)){
                                    totalRevenue.setText(0);
                                }else {
                                    String formattedTotalAmount = NumberFormat.getNumberInstance(Locale.getDefault()).format(Total_Revenue);
                                    totalRevenue.setText(formattedTotalAmount);
                                }

                            }

                        }

                        setupBarChart(getView()); // Cập nhật biểu đồ với dữ liệu mới
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void CountQuantityAndTotalBills(int month , int year) {
        Bills_Admin billAdmin = RetrofitClient.getRetrofitInstance("11177575", "60-dayfreetrial").create(Bills_Admin.class);

        Call<String> call = billAdmin.getBills_Admin(id_shop);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        double totalAmount = 0;
                        int countBill = 0;
                        boolean hasBills = false; // Biến kiểm tra xem có hóa đơn nào được tìm thấy không

                        // Định dạng của chuỗi datetime
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat outputYearFormat = new SimpleDateFormat("yyyy");
                        SimpleDateFormat outputMonthFormat = new SimpleDateFormat("MM");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String dateTime_end = jsonObject.getString("dateTimeEnd");
                            double total_price = jsonObject.getDouble("total_price");



                            // Chuyển đổi chuỗi datetime thành đối tượng Date
                            Date datetime = inputFormat.parse(dateTime_end);
                            // Lấy năm và tháng từ đối tượng Date
                            String Year = outputYearFormat.format(datetime);
                            String Month = outputMonthFormat.format(datetime);

                            int billYear = Integer.parseInt(Year);
                            int billMonth = Integer.parseInt(Month);
                            if (year != 0 && month != 0) { // Nếu đang xem theo một tháng cụ thể
                                if (billYear == year && billMonth == month) {
                                    totalAmount += total_price;
                                    countBill++;
                                    hasBills = true;
                                }
                            } else { // Nếu đang xem cả năm
                                totalAmount += total_price;
                                countBill++;
                                hasBills = true;
                            }
                        }

                        if (!hasBills) { // Nếu không có hóa đơn nào được tìm thấy
                            totalAmount = 0; // Đặt tổng số tiền thành 0
                        }

                        String formattedTotalAmount = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalAmount);
                        totalRevenue.setText(formattedTotalAmount);

                        String count_Bill = NumberFormat.getNumberInstance(Locale.getDefault()).format(countBill);
                        totalBill.setText(count_Bill + " hóa đơn");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void setupBarChart(View view) {
        BarChart barChart = view.findViewById(R.id.barChart);
        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo tháng");
        dataSet.setValueTextSize(12f);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(0.5f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setLabelCount(4, true);
        yAxisLeft.setTextSize(12f);
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return formatCurrency(value);
            }
        });

        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        if (dataSet.getEntryCount() <= 3) {
            barData.setBarWidth(0.2f);
            barChart.getXAxis().setGranularity(1f);
        }

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
    }

    private String formatCurrency(float value) {
        long roundedValue = Math.round(value);
        if (roundedValue >= 1000000) {
            return String.format("%d tr", roundedValue / 1000000);
        } else {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            return numberFormat.format(roundedValue);
        }
    }
}
