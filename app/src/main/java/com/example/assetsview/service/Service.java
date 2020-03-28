package com.example.assetsview.service;

import android.widget.AdapterView;
import android.widget.ListView;

import com.example.assetsview.entity.Base;
import com.example.assetsview.entity.Type;
import com.github.mikephil.charting.charts.PieChart;

public interface Service {
    
    void save(Base entity, String tag);

    void fetchDate(final AdapterView adapterView, Type type, final int layout);

    void fetchGraphData(Type invest, PieChart pieChart,  int startMonth, int startYear, int endMonth, int endYear);
}
