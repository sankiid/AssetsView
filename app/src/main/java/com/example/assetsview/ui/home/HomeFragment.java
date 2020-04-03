package com.example.assetsview.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Calendar;

import static com.example.assetsview.Utils.format;

public class HomeFragment extends DialogFragment {

    public static final String MM_YY = "MM/yy";
    private PieChart expensePieChart;
    private PieChart incomePieChart;
    private PieChart investPieChart;
    private TextView startDate;
    private TextView endDate;
    private TextView textView;
    private Button button;
    private Service service;
    private long startTime;
    private long endTime;

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
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        startDate.setText(format(c.getTimeInMillis(), MM_YY));
                        c.set(Calendar.DAY_OF_MONTH, 0);
                        startTime = c.getTimeInMillis();
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
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                        endTime = c.getTimeInMillis();
                        endDate.setText(format(endTime, MM_YY));
                    }
                });
            }
        });

        Calendar c = Calendar.getInstance();
        startDate.setText(format(c.getTimeInMillis(), MM_YY));
        endDate.setText(format(c.getTimeInMillis(), MM_YY));

        endTime = c.getTimeInMillis();
        c.set(Calendar.DAY_OF_MONTH, 0);
        startTime = c.getTimeInMillis();

        expensePieChart = view.findViewById(R.id.expense_chart);
        service.fetchGraphData(Type.EXPENSE, expensePieChart, startTime, endTime);

        incomePieChart = view.findViewById(R.id.income_chart);
        service.fetchGraphData(Type.INCOME, incomePieChart, startTime, endTime);

        investPieChart = view.findViewById(R.id.invest_chart);
        service.fetchGraphData(Type.INVEST, investPieChart, startTime, endTime);

        // button click
        button = view.findViewById(R.id.home_filter);
//        button.setBackgroundColor(Color.rgb(255, 195, 0));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("fun", "start " + startTime + ", end " + endTime);
                service.fetchGraphData(Type.EXPENSE, expensePieChart, startTime, endTime);
                service.fetchGraphData(Type.INCOME, incomePieChart, startTime, endTime);
                service.fetchGraphData(Type.INVEST, investPieChart, startTime, endTime);
            }
        });

        return view;
    }
}
