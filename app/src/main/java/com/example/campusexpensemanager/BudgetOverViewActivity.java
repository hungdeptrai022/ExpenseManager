package com.example.campusexpensemanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DatabaseSQLite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class BudgetOverViewActivity extends AppCompatActivity {

    private Button btnCreateExpense, btnEditExpense, btnQuery, btnDelete, btnReturn;
    private TextView tvTotalBudget, tvTotalSpent, tvRemainingBudget;
    private ListView listViewExpenses;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> expenseList;
    private String selectedCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        // Initialize views
        tvTotalBudget = findViewById(R.id.tvTotalBudget);
//        tvTotalSpent = findViewById(R.id.tvTotalSpent);
        tvRemainingBudget = findViewById(R.id.tvRemainingBudget);
//        listViewExpenses = findViewById(R.id.listViewExpenses);

//        btnDelete = findViewById(R.id.btnDeleteExpense);
        btnEditExpense = findViewById(R.id.btnEditExpense);
        btnCreateExpense = findViewById(R.id.btnCreateExpense);
//        btnQuery = findViewById(R.id.btnQueryExpense);
        btnReturn = findViewById(R.id.btnReturnFromExpenseOverview);

    }
}
