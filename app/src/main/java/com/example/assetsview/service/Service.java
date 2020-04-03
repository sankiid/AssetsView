package com.example.assetsview.service;

import android.widget.AdapterView;

import com.example.assetsview.entity.Base;
import com.example.assetsview.entity.Type;
import com.github.mikephil.charting.charts.PieChart;

public interface Service {
    
    void save(Base entity, String tag);

    void fetchDate(final AdapterView adapterView, Type type, final int layout);

    void fetchGraphData(Type invest, PieChart pieChart, long startTime, long endTime);
}
