package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DatabaseSQLite.DatabaseHelper;
import com.example.campusexpensemanager.Models.Categories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BudgetEditingActivity extends AppCompatActivity {
    private List<Categories> expenseList;
    private Button btnUpdateExpense, btnReturnToBudgetFromEdit;
    private EditText edtEditCategoryName, edtEditTotal, edtEditDate;
    private Spinner spinnerExpenseName;
    public DatabaseHelper expenseCategoriesDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_edit);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String pass = intent.getStringExtra("pass");

        // Initialize UI components
        spinnerExpenseName = findViewById(R.id.spinnerEditExpense);
        btnUpdateExpense = findViewById(R.id.btnUpdateExpense);
        edtEditTotal = findViewById(R.id.edtEditTotal);
        btnReturnToBudgetFromEdit = findViewById(R.id.btnReturnToBudgetFromEdit);

        // Initialize the database
        expenseCategoriesDatabase = new DatabaseHelper(this);

        // Load expense names into spinner
        loadExpenseNames();

        // Set click listener for the Update button
        btnUpdateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = spinnerExpenseName.getSelectedItem().toString();
                String budgetStr = edtEditTotal.getText().toString().trim();

                double newBudget;
                try {
                    newBudget = Double.parseDouble(budgetStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(BudgetEditingActivity.this, "Invalid budget format", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (budgetStr.isEmpty()) {
                    Toast.makeText(BudgetEditingActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int rowsAffected = expenseCategoriesDatabase.editExpense(categoryName, newBudget);

                if (rowsAffected > 0) {
                    Toast.makeText(BudgetEditingActivity.this, "Expense updated successfully", Toast.LENGTH_SHORT).show();

                    // Transition to AddExpenseActivity
                    Intent intent = new Intent(BudgetEditingActivity.this, AddExpenseActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("email",email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("pass",pass);
                    startActivity(intent);
                } else {
                    Toast.makeText(BudgetEditingActivity.this, "Expense update failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listener for the Return button
        btnReturnToBudgetFromEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetEditingActivity.this, AddExpenseActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass", pass);
                startActivity(intent);
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish(); // Optional: Close BudgetEditingActivity
            }
        });
    }

    private void loadExpenseNames() {
        List<String> expenseNames = expenseCategoriesDatabase.getAllExpenseNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExpenseName.setAdapter(adapter);
    }


}
