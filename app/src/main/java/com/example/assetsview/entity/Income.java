package com.example.assetsview.entity;

public class Income extends Base {

    public Income(Category category, String amount, String date, String description) {
        super(category, amount, date, description, Type.INCOME);
    }
}
