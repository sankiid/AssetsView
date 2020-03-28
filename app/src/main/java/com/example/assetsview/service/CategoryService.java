package com.example.assetsview.service;

import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.assetsview.entity.Category;
import com.example.assetsview.entity.Type;

public interface CategoryService {

    void save(Category category);

    void fetchAllCategory(final AdapterView adapterView);

    void fetchCategoryByType(Type type, int spinnerItem, int spinnerDropdownItem, Spinner spinner);

}
