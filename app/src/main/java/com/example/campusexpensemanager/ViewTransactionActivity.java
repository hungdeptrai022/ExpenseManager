package com.example.campusexpensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Adapter.TransactionAdapter;
import com.example.campusexpensemanager.DatabaseSQLite.DatabaseHelper;
import com.example.campusexpensemanager.Models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ViewTransactionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private Button btnAddRecord, btnReturn;
    private DatabaseHelper dbHelper;
    private static final int ADD_TRANSACTION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        recyclerView = findViewById(R.id.rvTransaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnAddRecord = findViewById(R.id.btn_add_transaction);
        btnReturn = findViewById(R.id.btnReturnFromSpend);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String pass = intent.getStringExtra("pass");
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTransactionActivity.this, DashBoardActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

        dbHelper = new DatabaseHelper(this);

        List<Transaction> transactionList = fetchTransactions();

        adapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(adapter);

        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTransactionActivity.this, AddTransactionActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",pass);
                startActivityForResult(intent, ADD_TRANSACTION_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TRANSACTION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh the transaction list when returning from AddTransactionActivity
            List<Transaction> transactionList = fetchTransactions();
            adapter.updateTransactions(transactionList);
        }
    }

    private List<Transaction> fetchTransactions(){
        List<Transaction> transactionList = new ArrayList<>();

        Cursor cursor = dbHelper.getAllTransactions();
        if(cursor.moveToFirst()){
            do {
                String expenseName = cursor.getString(0);
                double amount = cursor.getDouble(1);
                String detail = cursor.getString(2);
                String date = cursor.getString(3);

                transactionList.add(new Transaction(expenseName, amount, detail, date));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactionList;
    }


}