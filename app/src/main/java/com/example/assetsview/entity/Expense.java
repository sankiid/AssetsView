package com.example.assetsview.entity;

public class Expense extends Base {
    
    public Expense(Category category, String amount, long date, String description) {
        super(category, amount, date, description, Type.EXPENSE);
    }
}
