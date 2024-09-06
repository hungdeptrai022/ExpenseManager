package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DatabaseSQLite.DatabaseHelper;

public class OverViewActivity extends AppCompatActivity {
    private Button btnReturn;
    private EditText edtTotal, edtRemaining;
    private TextView tvYourName;
    public DatabaseHelper expensesCategoriesDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_dashboard);

        edtTotal = findViewById(R.id.edtTotalBudget1);
        edtRemaining = findViewById(R.id.edtRemainingBudget1);
        expensesCategoriesDatabase = new DatabaseHelper(this);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String pass = intent.getStringExtra("pass");
        tvYourName = findViewById(R.id.txtYourName);
        tvYourName.setText(username);

        btnReturn = findViewById(R.id.btnReturnFromHome);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverViewActivity.this, DashBoardActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });


    }


}
