package com.example.campusexpensemanager.Models;

public class Categories  {
    public int category_id;
    public String category_name;
    public double category_budget;
    public double remaining_budget;

    public Categories(int category_id, String category_name, double category_budget, double remaining_budget) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_budget = category_budget;
        this.remaining_budget = remaining_budget;
    }

    public float spending_on_total_progress;
    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public double getCategory_budget() {
        return category_budget;
    }

    public void setCategory_budget(int category_budget) {
        this.category_budget = category_budget;
    }

    public double getRemaining_budget() {
        return remaining_budget;
    }

    public void setRemaining_budget(int remaining_budget) {
        this.remaining_budget = remaining_budget;
    }


}
