package com.example.campusexpensemanager.Models;

import java.util.Date;

public class TotalBudget {
    public int total_id;
    public int total_budget;
    public int remaining_budget;

    public int getTotal_id() {
        return total_id;
    }

    public void setTotal_id(int total_id) {
        this.total_id = total_id;
    }

    public int getTotal_budget() {
        return total_budget;
    }

    public void setTotal_budget(int total_budget) {
        this.total_budget = total_budget;
    }

    public int getRemaining_budget() {
        return remaining_budget;
    }

    public void setRemaining_budget(int remaining_budget) {
        this.remaining_budget = remaining_budget;
    }
}
