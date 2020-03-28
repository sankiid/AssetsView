package com.example.assetsview.entity;

public class Invest extends Base {

    public Invest(Category category, String amount, String date, String description) {
        super(category, amount, date, description, Type.INVEST);
    }
}
