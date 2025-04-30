package com.example.project_prm.Activity.Admin.Statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.BaseActivity;
import com.example.project_prm.Dao.OrderDao;
import com.example.project_prm.Dao.ProductDao;
import com.example.project_prm.Dao.UserDao;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.StatisticViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends BaseActivity {

    private BarChart barChart;
    private PieChart pieChart;
    private StatisticViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_statistic, findViewById(R.id.content_frame));

        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);

        viewModel = new ViewModelProvider(this).get(StatisticViewModel.class);

        observeData();
    }
    private void observeData() {
        // Quan sát dữ liệu đơn hàng trong ngày
        viewModel.getTodayOrders().observe(this, todayOrders -> {
            Log.d("Order in day", String.valueOf(todayOrders)); // Ghi log đúng cách
            updateBarChart();
        });

        // Quan sát tổng số đơn hàng
        viewModel.getTotalOrders().observe(this, totalOrders -> {
            Log.d("Total Orders", String.valueOf(totalOrders));
            updateBarChart();
        });

        // Quan sát tổng số sản phẩm
        viewModel.getTotalProducts().observe(this, totalProducts -> {
            Log.d("Total Products", String.valueOf(totalProducts));
            updatePieChart();
        });

        // Quan sát tổng số tài khoản
        viewModel.getTotalAccounts().observe(this, totalAccounts -> {
            Log.d("Total Accounts", String.valueOf(totalAccounts));
            updatePieChart();
        });
    }
    private void updateBarChart() {
        Integer todayOrders = viewModel.getTodayOrders().getValue();
        Integer totalOrders = viewModel.getTotalOrders().getValue();

        if (todayOrders != null && totalOrders != null) {
            setupBarChart(todayOrders, totalOrders);
        }
    }

    // Cập nhật biểu đồ tròn
    private void updatePieChart() {
        Integer totalProducts = viewModel.getTotalProducts().getValue();
        Integer totalAccounts = viewModel.getTotalAccounts().getValue();

        if (totalProducts != null && totalAccounts != null) {
            setupPieChart(totalProducts, totalAccounts);
        }
    }
    private void setupBarChart(int todayOrders, int totalOrders) {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setFitBars(true);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, todayOrders));
        entries.add(new BarEntry(2, totalOrders));

        BarDataSet dataSet = new BarDataSet(entries, "Orders");
        dataSet.setColors(Color.BLUE, Color.GREEN);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        // Set ValueFormatter để hiển thị số nguyên
        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value); // Chuyển float thành int
            }
        });

        barChart.setData(barData);
        barChart.invalidate();
    }



    private void setupPieChart(int totalProducts, int totalAccounts) {
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(50f);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalProducts, "Products"));
        entries.add(new PieEntry(totalAccounts, "Accounts"));

        PieDataSet dataSet = new PieDataSet(entries, "Statistics");
        dataSet.setColors(Color.MAGENTA, Color.CYAN);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        PieData pieData = new PieData(dataSet);
        // Set ValueFormatter để hiển thị số nguyên
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        pieChart.setData(pieData);
        pieChart.invalidate();
    }


}