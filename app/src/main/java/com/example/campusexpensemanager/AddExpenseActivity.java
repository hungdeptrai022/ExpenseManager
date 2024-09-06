package com.example.campusexpensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Adapter.ExpenseAdapter;
import com.example.campusexpensemanager.DatabaseSQLite.DatabaseHelper;
import com.example.campusexpensemanager.Models.Categories;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    private TextView tvRemainingBudget,tvTotalBudget, tvTotalSpent;
    private EditText etName, etBudget ;
    private Button btnAddExpense, btnEditExpense, btnReturn;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private static final int EDIT_EXPENSE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        btnReturn = findViewById(R.id.btnReturnFromExpenseOverview);

        tvRemainingBudget = findViewById(R.id.tvRemainingBudget); // Initialize TextView for RemainingBudget
        tvTotalBudget = findViewById(R.id.tvTotalBudget);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String pass = intent.getStringExtra("pass");
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddExpenseActivity.this, DashBoardActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

        btnEditExpense = findViewById(R.id.btnEditExpense);
        btnEditExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddExpenseActivity.this, BudgetEditingActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

        etName = findViewById(R.id.etName);
        etBudget = findViewById(R.id.etBudget);
        btnAddExpense = findViewById(R.id.btnCreateExpense);
        recyclerView = findViewById(R.id.recyclerView);
        dbHelper = new DatabaseHelper(this);

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String budgetStr = etBudget.getText().toString().trim();
                if (name.isEmpty()) {
                    etName.setError("Expense name is required");
                    etName.requestFocus();
                    return;
                }
                if (budgetStr.isEmpty()) {
                    etBudget.setError("Budget is required");
                    etBudget.requestFocus();
                    return;
                }
                int budget;
                try {
                    budget = Integer.parseInt(budgetStr);
                } catch (NumberFormatException e) {
                    etBudget.setError("Budget must be a valid number");
                    etBudget.requestFocus();
                    return;
                }
                if (budget <= 0) {
                    etBudget.setError("Budget must be greater than zero");
                    etBudget.requestFocus();
                    return;
                }
                // Add expense to database
                dbHelper.addExpense(name, budget);

                // Update the budget displays
                updateBudgetDisplays();

                Toast.makeText(v.getContext(), "Expense added successfully", Toast.LENGTH_SHORT).show();
                displayExpenses();
                // Clear the input fields
                etName.setText("");
                etBudget.setText("");
            }
        });

        // Initial display of expenses and budgets
        displayExpenses();
        updateBudgetDisplays();
    }
    private void displayExpenses() {
        Cursor cursor = dbHelper.getAllExpenses();
        List<Categories> expenseList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double budget = cursor.getInt(2);
                double remaining = cursor.getInt(3);
                expenseList.add(new Categories(id, name, budget, remaining));
            } while (cursor.moveToNext());
        }
        ExpenseAdapter adapter = new ExpenseAdapter(expenseList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
    private void updateBudgetDisplays() {
        // Calculate and display total budget and remaining budget
        double totalBudget = dbHelper.getTotalBudget();
        double remainingBudget = dbHelper.getTotalRemaining();

        tvTotalBudget.setText(String.format("Total Budget: %.2f", totalBudget));
        tvRemainingBudget.setText(String.format("Remaining Budget: %.2f", remainingBudget));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_EXPENSE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh the RecyclerView data
            displayExpenses();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        displayExpenses();
        updateBudgetDisplays();
    }
    public void refreshBudgetDisplay() {
        updateBudgetDisplays();
    }

}