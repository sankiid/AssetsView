package com.example.assetsview.ui.income;

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
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.assetsview.Constants;
import com.example.assetsview.R;
import com.example.assetsview.Utils;
import com.example.assetsview.entity.Category;
import com.example.assetsview.entity.Expense;
import com.example.assetsview.entity.Income;
import com.example.assetsview.entity.Type;
import com.example.assetsview.service.CategoryService;
import com.example.assetsview.service.CategoryServiceImpl;
import com.example.assetsview.service.Service;
import com.example.assetsview.service.ServiceImpl;
import com.example.assetsview.ui.expense.ExpenseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class IncomeFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private TextView incDate;
    private Spinner incCategory;
    private EditText incAmount;
    private EditText incDesc;
    private String category;
    private Service service;
    private Button button;
    private ListView incenseListView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        service = new ServiceImpl(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        // category view
        incCategory = view.findViewById(R.id.inc_cat);
        CategoryService categoryService = new CategoryServiceImpl(this);
        categoryService.fetchCategoryByType(Type.INCOME, android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item, incCategory);
        incCategory.setOnItemSelectedListener(this);

        // date view
        incDate = view.findViewById(R.id.inc_date);
        incDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(IncomeFragment.this.getContext(), IncomeFragment.this, year, month, day);
                dialog.show();
            }
        });

        incAmount = view.findViewById(R.id.inc_amt);
        incDesc = view.findViewById(R.id.inc_desc);
        button = view.findViewById(R.id.inc_save);
        button.setOnClickListener(this);

        // loading existing income data
        incenseListView = view.findViewById(R.id.inc_list_view);
        service.fetchDate(incenseListView, Type.INCOME, R.layout.inc_table_view);

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        incDate.setText(sdf.format(c.getTime()));
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
        long date = Utils.parseDate(incDate.getText().toString().trim());
        String amount = Utils.parseAmount(incAmount.getText().toString().trim());
        String desc = incDesc.getText().toString().trim();
        Category category = new Category(this.category, Type.INCOME);

        service.save(new Income(category, amount, date, desc), Type.INCOME.getName());
    }
}
