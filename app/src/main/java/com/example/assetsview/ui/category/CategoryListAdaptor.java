package com.example.assetsview.ui.category;

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
import com.example.assetsview.entity.Category;
import com.example.assetsview.entity.Type;

import java.util.List;

public class CategoryListAdaptor extends ArrayAdapter<Category> {

    private Context context;
    private int resource;
    private int[] color;

    public CategoryListAdaptor(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.color = new int[]{Color.rgb(236, 239, 241), Color.rgb(207, 216, 220)};
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        Type type = getItem(position).getType();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);
        int col = Color.rgb(120, 144, 156);
        if (position != 0) {
            col = color[position % color.length];
        }
        convertView.setBackgroundColor(col);

        TextView textView1 = convertView.findViewById(R.id.lt_cat_name);
        TextView textView2 = convertView.findViewById(R.id.lt_cat_type);

        textView1.setText(name);
        textView2.setText(type.getName());

        return convertView;
    }
}
