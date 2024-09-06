package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DatabaseSQLite.UserDatabase;

public class RegisterPageActivity extends AppCompatActivity {
    private EditText edtRegisterYourName, edtRegisterYourEmail, edtRegisterYourPhone, edtRegisterYourPassword;
    private Button btnRegisterToDashBoard, btnReturnToStart;
    private CheckBox cbShowPass;
    public UserDatabase userDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        edtRegisterYourName = findViewById(R.id.edtRegisterYourName);
        edtRegisterYourEmail =findViewById(R.id.edtRegisterYourEmail);
        edtRegisterYourPhone = findViewById(R.id.edtRegisterYourPhone);
        edtRegisterYourPassword = findViewById(R.id.edtRegisterYourPassword);

        btnRegisterToDashBoard = findViewById(R.id.btnRegisterToDashBoard);
        btnReturnToStart = findViewById(R.id.btnExitFromRegister);

        CheckBox cbShowPass = findViewById(R.id.cbShowPassword);
        cbShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Hiển thị mật khẩu
                    edtRegisterYourPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Ẩn mật khẩu
                    edtRegisterYourPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // Di chuyển con trỏ về cuối cùng của text
                edtRegisterYourPassword.setSelection(edtRegisterYourPassword.length());
            }
        });

        btnReturnToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(RegisterPageActivity.this, StartingActivity.class);
                startActivity(intent);
            }
        });

        userDatabase= new UserDatabase(RegisterPageActivity.this);
        btnRegisterToDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();

            }
        });
    }

    public void signUp(){
        String user = edtRegisterYourName.getText().toString().trim();
        String email = edtRegisterYourEmail.getText().toString().trim();
        String phone = edtRegisterYourPhone.getText().toString().trim();
        String password = edtRegisterYourPassword.getText().toString().trim();
        if (TextUtils.isEmpty(user)){
            edtRegisterYourName.setError("Username cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtRegisterYourPassword.setError("Password cannot be empty");
            return;
        }
        else if (password.length() < 6) {
            edtRegisterYourPassword.setError("Password must be at least 6 characters long");
            return;
        } else if (!password.matches(".*[0-9].*") || !password.matches(".*[a-zA-Z].*")) {
            edtRegisterYourPassword.setError("Password must contain at least one letter and one number");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            edtRegisterYourEmail.setError("Email cannot be empty");
            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtRegisterYourEmail.setError("Invalid email format");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            edtRegisterYourPhone.setError("Phone cannot be empty");
            return; }
//        else if (!phone.matches("\\d{10}")) {
//            edtRegisterYourPhone.setError("Phone number must be 10 digits");
//            return;
//        }
        if (userDatabase.isEmailRegistered(email)){
            edtRegisterYourEmail.setError("Email is already registered");
            return;
        }
        long insert = userDatabase.addNewUser(user, email , phone, password);
        if (insert == -1){
            //loi
            Toast.makeText(RegisterPageActivity.this, "Insert Fail", Toast.LENGTH_LONG).show();
        }else {
            //thanh cong
            Toast.makeText(RegisterPageActivity.this, "Insert Successfully", Toast.LENGTH_SHORT).show();

            Intent intent =new Intent(RegisterPageActivity.this, StartingActivity.class);
            startActivity(intent);
        }
    }
}