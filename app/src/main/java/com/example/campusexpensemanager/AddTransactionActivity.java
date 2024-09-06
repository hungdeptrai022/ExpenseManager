package com.example.campusexpensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Adapter.TransactionAdapter;
import com.example.campusexpensemanager.DatabaseSQLite.DatabaseHelper;
import com.example.campusexpensemanager.Models.Categories;
import com.example.campusexpensemanager.Models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {
    private Spinner spinnerExpenseName;
    private EditText etTransactionMoney, etTransactionDetail, etTransactionDate;
    private Button btnSaveTransaction, btnReturn;
    private DatabaseHelper dbHelper; // Assuming you have a DBHelper class for database interactions
    private List<Categories> expenseList; // List of expenses to populate spinner

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        btnReturn = findViewById(R.id.btnReturnFromAddSpend);

        spinnerExpenseName = findViewById(R.id.spinnerCategories);
        etTransactionMoney = findViewById(R.id.et_transaction_amount);
        etTransactionDetail = findViewById(R.id.edtDescription1);
        etTransactionDate = findViewById(R.id.et_date);
        btnSaveTransaction = findViewById(R.id.btnAddTransaction);

        dbHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String pass = intent.getStringExtra("pass");
        loadExpenseNames();

        btnSaveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });
//        displayTransactions();
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTransactionActivity.this, ViewTransactionActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });
    }

    //    private void displayTransactions() {
//        Cursor cursor = dbHelper.getTransactionsForExpense(expenseId);
//        List<Transaction> transactionList = new ArrayList<>();
//        if (cursor.moveToFirst()) {
//            do {
//                int id = cursor.getInt(0);
//                double amount = cursor.getDouble(2);
//                String date = cursor.getString(3);
//                transactionList.add(new Transaction(id, expenseId, amount, date));
//            } while (cursor.moveToNext());
//        }
//
//
//        TransactionAdapter adapter = new TransactionAdapter(transactionList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }
    private void loadExpenseNames() {
        List<String> expenseNames = dbHelper.getAllExpenseNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExpenseName.setAdapter(adapter);
    }
    private void addTransaction() {
        String expenseName = spinnerExpenseName.getSelectedItem().toString();
        String amountText = etTransactionMoney.getText().toString().trim();
        String detail = etTransactionDetail.getText().toString();
        String date = etTransactionDate.getText().toString();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String pass = intent.getStringExtra("pass");



        if (expenseName.isEmpty()) {
            Toast.makeText(this, "Please select an expense category.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountText.isEmpty()) {
            etTransactionMoney.setError("Please enter the amount.");
            etTransactionMoney.requestFocus();
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            etTransactionMoney.setError("Invalid amount.");
            etTransactionMoney.requestFocus();
            return;
        }

        if (amount <= 0) {
            etTransactionMoney.setError("Amount must be greater than zero.");
            etTransactionMoney.requestFocus();
            return;
        }
        double remainingBudget = dbHelper.getRemainingBudget(expenseName);
        if (amount > remainingBudget){
            Toast.makeText(this,"Transaction amount exceeds the remaining budget!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (date.isEmpty()) {
            etTransactionDate.setError("Please enter a date.");
            etTransactionDate.requestFocus();
            return;
        }

        if (detail.isEmpty()) {
            detail = "No details provided"; // Set a default value if no detail is provided
        }

        // Insert the transaction into the database
        dbHelper.addTransaction(expenseName, amount, detail, date);

        // Update the remaining budget of the corresponding expense
        dbHelper.updateExpenseRemainingBudget(expenseName, amount);

        Toast.makeText(this, "Transaction added successfully!", Toast.LENGTH_SHORT).show();

        // Clear the form
        etTransactionMoney.setText("");
        etTransactionDetail.setText("");
        etTransactionDate.setText("");

        // Return to ViewTransactionActivity and notify that data was updated
        Intent resultIntent = new Intent();
        intent.putExtra("username",username);
        intent.putExtra("email",email);
        intent.putExtra("phone", phone);
        intent.putExtra("pass",pass);

        setResult(RESULT_OK, resultIntent);
        finish(); // Close AddTransactionActivity
    }
}