package com.example.assetsview.ui.expense;

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
import com.example.assetsview.entity.Type;
import com.example.assetsview.service.CategoryService;
import com.example.assetsview.service.CategoryServiceImpl;
import com.example.assetsview.service.Service;
import com.example.assetsview.service.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private TextView expDate;
    private Spinner expCategory;
    private EditText expAmount;
    private EditText expDesc;
    private String category;
    private Service service;
    private Button button;
    private ListView expenseListView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        service = new ServiceImpl(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        // category view
        expCategory = view.findViewById(R.id.exp_cat);
        CategoryService categoryService = new CategoryServiceImpl(this);
        categoryService.fetchCategoryByType(Type.EXPENSE, android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item, expCategory);
        expCategory.setOnItemSelectedListener(this);

        // date view
        expDate = view.findViewById(R.id.exp_date);
        expDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(ExpenseFragment.this.getContext(), ExpenseFragment.this, year, month, day);
                dialog.show();
            }
        });

        expAmount = view.findViewById(R.id.exp_amt);
        expDesc = view.findViewById(R.id.exp_desc);
        button = view.findViewById(R.id.exp_save);
        button.setOnClickListener(this);

        // loading existing expense data
        expenseListView = view.findViewById(R.id.exp_list_view);
        service.fetchDate(expenseListView, Type.EXPENSE, R.layout.exp_table_view);

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        expDate.setText(sdf.format(c.getTime()));
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
        String date = Utils.parseDate(expDate.getText().toString().trim(), Type.EXPENSE.getName());
        String amount = Utils.parseAmount(expAmount.getText().toString().trim());
        String desc = expDesc.getText().toString().trim();
        Category category = new Category(this.category, Type.EXPENSE);

        service.save(new Expense(category, amount, date, desc), Type.EXPENSE.getName());
    }
}
