package com.example.assetsview.entity;

public enum Type {

    INCOME("income"),

    INVEST("invest"),

    EXPENSE("expense"),

    HEADER_TYPE("type");

    private String name;

    private Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Type of(String name) {
        for (Type type : Type.values()) {
            if (name.equals(type.getName())) {
                return type;
            }
        }
        return null;
    }

}
