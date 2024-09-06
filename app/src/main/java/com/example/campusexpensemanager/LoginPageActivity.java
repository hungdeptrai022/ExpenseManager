package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DatabaseSQLite.UserDatabase;
import com.example.campusexpensemanager.Models.User;

public class LoginPageActivity extends AppCompatActivity {
    private Button btnLogIn, btnReturnToStart;
    private EditText edtEmail, edtPass;
    public UserDatabase userDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_page);

        btnLogIn = findViewById(R.id.btnLoginToDashboard);
        btnReturnToStart = findViewById(R.id.btnExitFromLogin);

        edtEmail = findViewById(R.id.edtStartEmail);
        edtPass  =findViewById(R.id.edtStartPassword);

        userDatabase = new UserDatabase(LoginPageActivity.this);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                // Validation checks
                if (TextUtils.isEmpty(input)) {
                    edtEmail.setError("Username, Email, or Phone cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    edtPass.setError("Password cannot be empty");
                    return;
                }

                User data = userDatabase.getUserByUsernameEmailOrPhone(input, pass);

                if (data != null && data.getUsername()!=null ){
                    //thanh cong
                    String phone = data.getPhone();
                    String gmail = data.getEmail();
                    String username = data.getUsername();
                    String password = data.getPassword();
                    Intent intent = new Intent(LoginPageActivity.this, DashBoardActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("email",gmail);
                    intent.putExtra("phone", phone);
                    intent.putExtra("pass", password);
                    Toast.makeText(LoginPageActivity.this,"Welcome "+ username, Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                }else{
                    //dang nhap linh tinh
                    Toast.makeText(LoginPageActivity.this, "Account Invalid", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnReturnToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginPageActivity.this, StartingActivity.class);
                startActivity(intent);
            }
        });
    }
}