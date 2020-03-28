package com.example.assetsview.service;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.example.assetsview.R;
import com.example.assetsview.entity.Category;
import com.example.assetsview.entity.Type;
import com.example.assetsview.ui.category.CategoryListAdaptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.assetsview.Constants.CATEGORY;
import static com.example.assetsview.Constants.url;

public class CategoryServiceImpl implements CategoryService {

    private final RetryPolicy retryPolicy;
    private final Fragment fragment;
    private final List<Category> categories;

    public CategoryServiceImpl(Fragment fragment) {
        this.fragment = fragment;
        this.categories = new ArrayList<>();
        categories.add(new Category("Category", Type.HEADER_TYPE));
        retryPolicy = new DefaultRetryPolicy(30000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    @Override
    public void save(final Category category) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(fragment.getContext(), response, Toast.LENGTH_LONG).show();
                categories.add(category);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "addCategory");
                params.put("name", category.getName());
                params.put("type", category.getType().getName());

                return params;
            }
        };

        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(fragment.getContext());
        queue.add(stringRequest);
    }

    @Override
    public void fetchAllCategory(final AdapterView adapterView) {
        String _url = url + "?action=getCategory&type=all";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response, adapterView);
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

    private void parseResponse(String response, final AdapterView adapterView) {
        try {
            JSONObject jobj = new JSONObject(response);
            JSONArray jarray = jobj.getJSONArray("categories");

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jo = jarray.getJSONObject(i);

                String name = jo.getString("NAME");
                String type = jo.getString("TYPE");
                Category category = new Category(name, Type.of(type));
                categories.add(category);
            }
        } catch (JSONException e) {
            Log.e(CATEGORY, "some error happened " + e.getMessage());
        }
        CategoryListAdaptor listAdapter = new CategoryListAdaptor(fragment.getContext(), R.layout.category_table_view, categories);
        adapterView.setAdapter(listAdapter);
    }

    @Override
    public void fetchCategoryByType(Type type, final int spinnerItem, final int spinnerDropdownItem, final Spinner spinner) {
        String _url = url + "?action=getCategory&type=" + type.getName();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse2(response, spinnerItem, spinnerDropdownItem, spinner);
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

    private void parseResponse2(String response, final int spinnerItem, final int spinnerDropdownItem, final Spinner spinner) {
        List<String> catList = new ArrayList<>();
        try {
            JSONObject jobj = new JSONObject(response);
            JSONArray jarray = jobj.getJSONArray("categories");

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jo = jarray.getJSONObject(i);

                String name = jo.getString("NAME");
                catList.add(name);
            }
        } catch (JSONException e) {
            Log.e(CATEGORY, "some error happened " + e.getMessage());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragment.getContext(), spinnerItem, catList);
        adapter.setDropDownViewResource(spinnerDropdownItem);
        spinner.setAdapter(adapter);
    }
}
