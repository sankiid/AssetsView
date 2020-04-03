package com.example.assetsview.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.assetsview.R;
import com.example.assetsview.Utils;
import com.example.assetsview.entity.Base;
import com.example.assetsview.entity.Type;

import java.util.List;

public class EntityListAdaptor extends ArrayAdapter<Base> {

    public static final String DD_MM_YY = "dd/MM/yy";
    private Context context;
    private int resource;
    private int[] color;

    public EntityListAdaptor(@NonNull Context context, int resource, @NonNull List<Base> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.color = new int[]{Color.rgb(236, 239, 241), Color.rgb(207, 216, 220)};
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        long date = getItem(position).getDate();
        String category = getItem(position).getCategory().getName();
        String amount = getItem(position).getAmount();
        String desc = getItem(position).getDescription();
        Type type = getItem(position).getType();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);
        int col = Color.rgb(120, 144, 156);
        if (position != 0) {
            col = color[position % color.length];
        }
        convertView.setBackgroundColor(col);

        TextView dateView = getDateView(type, convertView);
        TextView catView = getCatView(type, convertView);
        TextView amtView = getAmountView(type, convertView);
        TextView descView = getDescView(type, convertView);

        dateView.setText(Utils.format(date, DD_MM_YY));
        catView.setText(category);
        amtView.setText(amount);
        descView.setText(desc);

        return convertView;
    }

    private TextView getDateView(Type type, View convertView) {
        switch (type) {
            case EXPENSE:
                return convertView.findViewById(R.id.lt_exp_date);
            case INCOME:
                return convertView.findViewById(R.id.lt_inc_date);
            case INVEST:
                return convertView.findViewById(R.id.lt_inv_date);
        }
        return null;
    }

    private TextView getCatView(Type type, View convertView) {
        switch (type) {
            case EXPENSE:
                return convertView.findViewById(R.id.lt_exp_cat);
            case INCOME:
                return convertView.findViewById(R.id.lt_inc_cat);
            case INVEST:
                return convertView.findViewById(R.id.lt_inv_cat);
        }
        return null;
    }

    private TextView getAmountView(Type type, View convertView) {
        switch (type) {
            case EXPENSE:
                return convertView.findViewById(R.id.lt_exp_amt);
            case INCOME:
                return convertView.findViewById(R.id.lt_inc_amt);
            case INVEST:
                return convertView.findViewById(R.id.lt_inv_amt);
        }
        return null;
    }

    private TextView getDescView(Type type, View convertView) {
        switch (type) {
            case EXPENSE:
                return convertView.findViewById(R.id.lt_exp_desc);
            case INCOME:
                return convertView.findViewById(R.id.lt_inc_desc);
            case INVEST:
                return convertView.findViewById(R.id.lt_inv_desc);
        }
        return null;
    }
}
