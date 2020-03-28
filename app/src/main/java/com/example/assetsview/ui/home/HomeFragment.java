package com.example.assetsview.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.assetsview.R;
import com.example.assetsview.entity.Type;
import com.example.assetsview.service.Service;
import com.example.assetsview.service.ServiceImpl;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends DialogFragment {

    private PieChart expensePieChart;
    private PieChart incomePieChart;
    private PieChart investPieChart;
    private TextView startDate;
    private TextView endDate;
    private TextView textView;
    private Button button;
    private Service service;
    private int startMonth;
    private int startYear;
    private int endMonth;
    private int endYear;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        service = new ServiceImpl(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textView = view.findViewById(R.id.home_text);
        textView.setText("RANGE: ");

        startDate = view.findViewById(R.id.home_date_start);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment.getInstance(month, year);
                dialogFragment.show(HomeFragment.this.getFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month) {
                        startMonth = month;
                        startYear = year % 2000;
                        startDate.setText(format(year, month));
                    }
                });
            }
        });

        endDate = view.findViewById(R.id.home_date_end);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment.getInstance(month, year);
                dialogFragment.show(HomeFragment.this.getFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month) {
                        endMonth = month;
                        endYear = year % 2000;
                        endDate.setText(format(year, month));
                    }
                });
            }
        });

        Calendar c = Calendar.getInstance();
        startYear = c.get(Calendar.YEAR) % 2000;
        endYear = c.get(Calendar.YEAR) % 2000;
        startMonth = c.get(Calendar.MONTH);
        endMonth = c.get(Calendar.MONTH);
        startDate.setText(format(startYear, startMonth));
        endDate.setText(format(endYear, endMonth));

        expensePieChart = view.findViewById(R.id.expense_chart);
        service.fetchGraphData(Type.EXPENSE, expensePieChart, startMonth + 1, startYear, endMonth + 1, endYear);

        incomePieChart = view.findViewById(R.id.income_chart);
        service.fetchGraphData(Type.INCOME, incomePieChart, startMonth + 1, startYear, endMonth + 1, endYear);

        investPieChart = view.findViewById(R.id.invest_chart);
        service.fetchGraphData(Type.INVEST, investPieChart, startMonth + 1, startYear, endMonth + 1, endYear);

        // button click
        button = view.findViewById(R.id.home_filter);
//        button.setBackgroundColor(Color.rgb(255, 195, 0));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.fetchGraphData(Type.EXPENSE, expensePieChart, startMonth + 1, startYear, endMonth + 1, endYear);
                service.fetchGraphData(Type.INCOME, incomePieChart, startMonth + 1, startYear, endMonth + 1, endYear);
                service.fetchGraphData(Type.INVEST, investPieChart, startMonth + 1, startYear, endMonth + 1, endYear);
            }
        });

        return view;
    }

    private String format(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year + 2000);
        c.set(Calendar.MONTH, month);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy", Locale.US);
        return sdf.format(c.getTime());
    }

}
