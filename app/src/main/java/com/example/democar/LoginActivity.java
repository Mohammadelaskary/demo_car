package com.example.democar;

import static com.example.democar.SignUpActivity.isValidEmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.democar.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    boolean isUser, isWorkshop;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new SpotsDialog(this, R.style.Custom);

        setUpTextWatchers();
        attachOnClickToButtons();

    }

    private void attachOnClickToButtons() {
        binding.login.setOnClickListener(this);
        binding.signUp.setOnClickListener(this);
    }

    private void setUpTextWatchers() {
        binding.email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.email.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.email.setError(null);
            }
        });
        binding.password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.password.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.password.setError(null);

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(LoginActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        switch (id) {
            case R.id.login: {
                if (isValidData()) login();
            }
            break;
            case R.id.sign_up: {
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent, options.toBundle());
            }
            break;
        }

    }

    AlertDialog progressDialog;

    private void login() {
        String email = binding.email.getEditText().getText().toString().trim();
        String password = binding.password.getEditText().getText().toString().trim();

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userId = mAuth.getCurrentUser().getUid();
                Toast.makeText(this, "Login successfully..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(LoginActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                startActivity(intent, options.toBundle());
                finish();
            } else {
                String errorMessage = task.getException().getMessage();
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        });
    }

    private boolean isValidData() {
        String email = binding.email.getEditText().getText().toString().trim();
        String password = binding.password.getEditText().getText().toString().trim();
        if (!isValidEmail(email))
            binding.email.setError("Enter valid email");
        if (password.length() < 6 && password.length() > 0)
            binding.password.setError("Password should be more than 6 characters length");
        else if (password.length() == 0)
            binding.password.setError("Please enter password");

        return isValidEmail(email) && password.length() >= 6;
    }

    @Override
    protected void onDestroy() {
        binding = null;
        mAuth = null;

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }
}