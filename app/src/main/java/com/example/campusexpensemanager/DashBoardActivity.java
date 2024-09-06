package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.campusexpensemanager.DatabaseSQLite.UserDatabase;

public class DashBoardActivity extends AppCompatActivity {
    public UserDatabase userDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asm_dashboard);
//        CardView overviewCard = findViewById(R.id.overview_card);
        CardView recordCard = findViewById(R.id.record_card);
        CardView budgetCard = findViewById(R.id.budget_card);
        CardView profileCard = findViewById(R.id.profile_card);
        userDatabase = new UserDatabase(DashBoardActivity.this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String pass = intent.getStringExtra("pass");
        recordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, ViewTransactionActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });
//        overviewCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DashBoardActivity.this, OverViewActivity.class);
//                intent.putExtra("username",username);
//                intent.putExtra("email",email);
//                intent.putExtra("phone", phone);
//                intent.putExtra("pass",pass);
//                startActivity(intent);
//            }
//        });
        budgetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, AddExpenseActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashBoardActivity.this, ProfileActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });
    }
}
