package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private TextView textViewUsername, textViewPhone, textViewEmail,textViewPassword;
    private Button btnLogOut, btnEditProfile, btnReturn;
    private String username, phone, email, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewUsername = findViewById(R.id.username_value);
        textViewEmail = findViewById(R.id.email_value);
        textViewPhone = findViewById(R.id.phone_value);
        textViewPassword = findViewById(R.id.txtPassValue);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        password = intent.getStringExtra("pass");

        updateProfileDisplay();

        btnLogOut = findViewById(R.id.logout_button);
        btnEditProfile = findViewById(R.id.buttonEdit);
        btnReturn = findViewById(R.id.btnReturnFromProfile);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, DashBoardActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("phone", phone);
                intent.putExtra("pass",password);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, StartingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                editIntent.putExtra("username", username);
                editIntent.putExtra("phone", phone);
                editIntent.putExtra("email", email);
                editIntent.putExtra("pass",password);
                startActivityForResult(editIntent, EDIT_PROFILE_REQUEST_CODE);
            }
        });

    }
    private void updateProfileDisplay() {
        textViewUsername.setText(username);
        textViewPhone.setText(phone);
        textViewEmail.setText(email);
        textViewPassword.setText(password);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve updated information from the result intent
            if (data != null) {
                username = data.getStringExtra("username");
                phone = data.getStringExtra("phone");
                email = data.getStringExtra("email");
                password=data.getStringExtra("pass");

                // Refresh the profile display
                updateProfileDisplay();
            }
        }
    }
}
