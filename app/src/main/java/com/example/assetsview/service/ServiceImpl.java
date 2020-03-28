package com.example.assetsview.service;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assetsview.entity.Base;
import com.example.assetsview.entity.Category;
import com.example.assetsview.entity.EntityFactory;
import com.example.assetsview.entity.Type;
import com.example.assetsview.ui.EntityListAdaptor;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.assetsview.Constants.url;

public class ServiceImpl implements Service {

    private Fragment fragment;
    private final RetryPolicy retryPolicy;
    private final List<Base> entityList;
    private EntityFactory factory;
    private int[] colorTheme;

    public ServiceImpl(Fragment fragment) {
        this.fragment = fragment;
        this.entityList = new ArrayList<>();
        this.retryPolicy = new DefaultRetryPolicy(60000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.factory = new EntityFactory();
        colorTheme = new int[]{
                Color.rgb(220, 20, 60), Color.rgb(154, 205, 50), Color.rgb(255, 165, 0),
                Color.rgb(255, 127, 80), Color.rgb(34, 139, 34), Color.rgb(102, 205, 170),
                Color.rgb(0, 206, 209), Color.rgb(95, 158, 160), Color.rgb(100, 149, 237),
                Color.rgb(65, 105, 225), Color.rgb(238, 130, 238), Color.rgb(153, 50, 204),
                Color.rgb(245, 222, 179), Color.rgb(255, 192, 203), Color.rgb(188, 143, 143),
                Color.rgb(230, 230, 250), Color.rgb(240, 248, 255), Color.rgb(240, 255, 240)
        };
    }

    @Override
    public void save(final Base entity, final String tag) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(fragment.getContext(), response, Toast.LENGTH_LONG).show();
                entityList.add(entity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", getAddAction(entity.getType()));
                params.put("cat", entity.getCategory().getName());
                params.put("amt", entity.getAmount());
                params.put("desc", entity.getDescription());
                params.put("date", entity.getDate());

                return params;
            }
        };

        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(fragment.getContext());
        queue.add(stringRequest);
    }

    @Override
    public void fetchDate(final AdapterView adapterView, final Type type, final int layout) {
        final String tag = type.getName();
        String _url = url + "?action=" + getGetAction(type);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response, adapterView, tag, type, layout);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(fragment.getContext());
        queue.add(stringRequest);
    }

    private void parseResponse(String response, final AdapterView adapterView, String tag, Type type, int layout) {
        try {
            Base _base = this.factory.getEntity(type, new Category("Category", Type.HEADER_TYPE), "Date", "Amount", "Desc");
            this.entityList.add(_base);

            JSONObject jobj = new JSONObject(response);
            JSONArray jarray = jobj.getJSONArray("entity");

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jo = jarray.getJSONObject(i);

                String date = jo.getString("DATE");
                String category = jo.getString("CATEGORY");
                String amount = jo.getString("AMOUNT");
                String desc = jo.getString("DESC");

                Base base = this.factory.getEntity(type, new Category(category, type), date, amount, desc);
                this.entityList.add(base);
            }
        } catch (JSONException e) {
            Log.e(tag, "some error happened " + e.getMessage());
        }
        EntityListAdaptor listAdapter = new EntityListAdaptor(fragment.getContext(), layout, this.entityList);
        adapterView.setAdapter(listAdapter);
    }

    private String getAddAction(Type type) {
        switch (type) {
            case INCOME:
                return "addIncome";
            case INVEST:
                return "addInvest";
            case EXPENSE:
                return "addExpense";
        }
        return null;
    }

    private String getGetAction(Type type) {
        switch (type) {
            case INCOME:
                return "getIncome";
            case INVEST:
                return "getInvest";
            case EXPENSE:
                return "getExpense";
        }
        return null;
    }

    @Override
    public void fetchGraphData(final Type type, final PieChart pieChart, int startMonth, int startYear, int endMonth, int endYear) {
        final String tag = type.getName();
        String _url = url + "?action=" + getGraphAction(type) + "&sm=" + startMonth + "&sy=" + startYear + "&em=" + endMonth + "&ey=" + endYear;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response, type, pieChart);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(fragment.getContext());
        queue.add(stringRequest);
    }

    private void parseResponse(String response, Type type, PieChart pieChart) {
        try {
            JSONArray jarray = new JSONArray(response);
            List<PieEntry> pieEntries = new ArrayList<>();
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jo = jarray.getJSONObject(i);

                String category = jo.getString("CATEGORY");
                float amount = (float) jo.getDouble("AMOUNT");
                pieEntries.add(new PieEntry(amount, category));
            }

            pieChart.setVisibility(View.VISIBLE);
            pieChart.animateXY(500, 500);

            PieDataSet pieDataSet = new PieDataSet(pieEntries, type.getName());
            pieDataSet.setColors(colorTheme);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);

            Description description = new Description();
            description.setText(type.name() + " Chart");
            pieChart.setDescription(description);
            pieChart.invalidate();

        } catch (JSONException e) {
            Log.e(type.getName(), "some error happened " + e.getMessage());
        }

    }

    private String getGraphAction(Type type) {
        switch (type) {
            case INCOME:
                return "getIncomePerCategory";
            case INVEST:
                return "getInvestPerCategory";
            case EXPENSE:
                return "getExpensePerCategory";
        }
        return null;
    }
}
