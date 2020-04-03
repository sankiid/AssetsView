package com.example.assetsview.entity;

public abstract class Base {

    private Category category;
    private String amount;
    private long date;
    private String description;
    private Type type;

    public Base(Category category, String amount, long date, String description, Type type) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
