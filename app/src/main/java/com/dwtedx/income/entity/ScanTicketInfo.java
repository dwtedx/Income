package com.dwtedx.income.entity;

public class ScanTicketInfo {

    private String name;
    private int quantity;
    private double value;
    private boolean isAdd;

    public ScanTicketInfo() {
        this.quantity = 1;
    }

    public ScanTicketInfo(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
