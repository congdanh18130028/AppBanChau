package com.example.shopbanchau.models;

public class UserEdit {
    private String value;
    private String path;
    private String op;

    public UserEdit(String value, String path, String op) {
        this.value = value;
        this.path = path;
        this.op = op;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
