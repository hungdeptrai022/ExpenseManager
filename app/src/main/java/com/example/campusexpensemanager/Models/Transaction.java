package com.example.campusexpensemanager.Models;

public class Transaction {
    private int id;
    private String ExpenseName;
    private double amount;
    private String detail; // Add this field
    private String date; // Add this field

    public Transaction(String expenseName, double amount, String detail, String date) {

        this.ExpenseName = expenseName;
        this.amount = amount;
        this.detail = detail;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpenseName() {
        return ExpenseName;
    }

    public void setExpenseName(String expenseName) {
        ExpenseName = expenseName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
