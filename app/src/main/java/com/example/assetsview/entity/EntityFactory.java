package com.example.assetsview.entity;

public class EntityFactory {

    public Base getEntity(Type type, Category category, long date, String amount, String desc) {
        switch (type) {
            case INCOME:
                return new Income(category, amount, date, desc);
            case INVEST:
                return new Invest(category, amount, date, desc);
            case EXPENSE:
                return new Expense(category, amount, date, desc);
        }
        return null;
    }

}
