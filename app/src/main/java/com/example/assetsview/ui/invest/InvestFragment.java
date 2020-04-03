package com.example.assetsview.ui.invest;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.assetsview.Constants;
import com.example.assetsview.R;
import com.example.assetsview.Utils;
import com.example.assetsview.entity.Category;
import com.example.assetsview.entity.Expense;
import com.example.assetsview.entity.Invest;
import com.example.assetsview.entity.Type;
import com.example.assetsview.service.CategoryService;
import com.example.assetsview.service.CategoryServiceImpl;
import com.example.assetsview.service.Service;
import com.example.assetsview.service.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InvestFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private TextView invDate;
    private Spinner invCategory;
    private EditText invAmount;
    private EditText invDesc;
    private String category;
    private Service service;
    private Button button;
    private ListView invListView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        service = new ServiceImpl(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invest, container, false);

        // category view
        invCategory = view.findViewById(R.id.inv_cat);
        CategoryService categoryService = new CategoryServiceImpl(this);
        categoryService.fetchCategoryByType(Type.INVEST, android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item, invCategory);
        invCategory.setOnItemSelectedListener(this);

        // date view
        invDate = view.findViewById(R.id.inv_date);
        invDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(InvestFragment.this.getContext(), InvestFragment.this, year, month, day);
                dialog.show();
            }
        });

        invAmount = view.findViewById(R.id.inv_amt);
        invDesc = view.findViewById(R.id.inv_desc);
        button = view.findViewById(R.id.inv_save);
        button.setOnClickListener(this);

        // loading existing expense data
        invListView = view.findViewById(R.id.inv_list_view);
        service.fetchDate(invListView, Type.INVEST, R.layout.inv_table_view);

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        invDate.setText(sdf.format(c.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        long date = Utils.parseDate(invDate.getText().toString().trim());
        String amount = Utils.parseAmount(invAmount.getText().toString().trim());
        String desc = invDesc.getText().toString().trim();
        Category category = new Category(this.category, Type.INVEST);

        service.save(new Invest(category, amount, date, desc), Type.INVEST.getName());
    }
}
