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

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPhone, editTextEmail, editTextPassword;
    private Button buttonUpdate,btnReturn;
    public UserDatabase userDatabase;
    private String originalEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        btnReturn = findViewById(R.id.btnReturnFromEditPrf);

        userDatabase = new UserDatabase(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String phone = intent.getStringExtra("phone");
        String password = intent.getStringExtra("pass");
        originalEmail = intent.getStringExtra("email");

        editTextUsername.setText(username);
        editTextPhone.setText(phone);
        editTextEmail.setText(originalEmail);
        editTextPassword.setText(password);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editTextUsername.getText().toString().trim();
                String newPhone = editTextPhone.getText().toString().trim();
                String newEmail = editTextEmail.getText().toString().trim();
                String newpassword = editTextPassword.getText().toString().trim();
                if (TextUtils.isEmpty(newEmail)) {
                    newEmail = originalEmail;
                } else if (TextUtils.isEmpty(newPhone)) {
                    newPhone = phone;
                } else if (TextUtils.isEmpty(newpassword)) {
                    newpassword = password;
                }
                else if (TextUtils.isEmpty(newUsername)) {
                    newUsername = username;
                }


                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("username", newUsername);
                intent.putExtra("phone", newPhone);
                intent.putExtra("email", newEmail);
                intent.putExtra("pass",password);
                startActivity(intent);

            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editTextUsername.getText().toString().trim();
                String newPhone = editTextPhone.getText().toString().trim();
                String newEmail = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (TextUtils.isEmpty(newUsername)){
                    editTextUsername.setError("Username cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Password cannot be empty");
                    return;
                } else if (password.length() < 6) {
                    editTextPassword.setError("Password must be at least 6 characters long");
                    return;
                } else if (!password.matches(".*[0-9].*") || !password.matches(".*[a-zA-Z].*")) {
                    editTextPassword.setError("Password must contain at least one letter and one number");
                    return;
                }
                if (TextUtils.isEmpty(newEmail)) {
                    editTextEmail.setError("Email cannot be empty");
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    editTextEmail.setError("Invalid email format");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    editTextPhone.setError("Phone cannot be empty");
                    return;}
//                 else if (!phone.matches("\\d{10}")) {
//                    editTextPhone.setError("Phone number must be 10 digits");
//                    return;
//                }


                if(!newUsername.isEmpty() && !newPhone.isEmpty() && !newEmail.isEmpty()){
                    userDatabase.updateUser(originalEmail, newUsername, newPhone, newEmail);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("username", newUsername);
                    resultIntent.putExtra("phone", newPhone);
                    resultIntent.putExtra("email", newEmail);
                    resultIntent.putExtra("pass",password);
                    setResult(RESULT_OK, resultIntent);
                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
