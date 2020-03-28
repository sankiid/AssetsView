package com.example.assetsview.ui.category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.assetsview.Constants;
import com.example.assetsview.R;
import com.example.assetsview.entity.Category;
import com.example.assetsview.entity.Type;
import com.example.assetsview.service.CategoryService;
import com.example.assetsview.service.CategoryServiceImpl;

public class CategoryFragment extends Fragment implements View.OnClickListener {

    private EditText categoryName;
    private EditText categoryType;
    private Button categorySave;
    private CategoryService service;
    private ListView categoryListView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        categoryListView = view.findViewById(R.id.cat_list_view);
        service = new CategoryServiceImpl(this);
        service.fetchAllCategory(categoryListView);


        categoryName = view.findViewById(R.id.cat_name);
        categoryType = view.findViewById(R.id.cat_type);
        categorySave = view.findViewById(R.id.cat_save);
        categorySave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        service = null;
    }

    @Override
    public void onClick(View v) {
        String name = categoryName.getText().toString().trim().toLowerCase();
        Type type = Type.of(categoryType.getText().toString().trim().toLowerCase());
        Category category = new Category(name, type);
        service.save(category);
    }
}
