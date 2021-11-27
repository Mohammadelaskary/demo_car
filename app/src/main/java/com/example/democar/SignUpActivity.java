package com.example.democar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.democar.Model.User;
import com.example.democar.Model.WorkShop;
import com.example.democar.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new SpotsDialog(this, R.style.Custom);
        setUpTextWatchers();
        attachButtonsToListener();

    }



    private void attachButtonsToListener() {
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
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login:{
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(SignUpActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                startActivity(intent,options.toBundle());
                finish();
            } break;
            case R.id.sign_up:{
                if (isValidData()) signUp();
            } break;
        }
    }

    private void uploadData() {
            boolean isMechanic = binding.mechanic.isChecked();
            String mechanic;
            if (isMechanic)
                mechanic = "mechanic";
            else
                mechanic = "notMechanic";
            String userId = mAuth.getCurrentUser().getUid();
            String userName = binding.userName.getEditText().getText().toString().trim();
            String email    = binding.email.getEditText().getText().toString().trim();
            String phone    = binding.phone.getEditText().getText().toString().trim();
            User user = new User(userId,userName,email,phone,mechanic);
            uploadUser(user);
    }

    private void uploadUser(User user) {
        DatabaseReference reference = database.getReference("Users");
        reference.push().setValue(user).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "User created successfully..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.putExtra("user", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(SignUpActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                startActivity(intent, options.toBundle());
                finish();
            } else {
                Log.d("signUpError",task.getException().getMessage());
            }
            progressDialog.dismiss();
        });
    }
    AlertDialog progressDialog;
    private void signUp() {
        String email = binding.email.getEditText().getText().toString().trim();
        String password = binding.password.getEditText().getText().toString().trim();

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                uploadData();
                Log.d("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
            } else {
                String errorMessage = task.getException().getMessage();
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                Log.d("signUpError",errorMessage);
                progressDialog.dismiss();
            }
        });
    }

    private boolean isValidData() {
        String email = binding.email.getEditText().getText().toString().trim();
        String password = binding.password.getEditText().getText().toString().trim();
        String phone    = binding.phone.getEditText().getText().toString().trim();

        if (!isValidEmail(email))
            binding.email.setError("Enter valid email");

        if (password.length()<6&&password.length()>0)
            binding.password.setError("Password should be more than 6 characters length");
        else if (password.length()==0)
            binding.password.setError("Please enter password");

        if (phone.length()!=8)
            binding.phone.setError("Phone must be 8 characters length");

        return  isValidEmail(email)&&password.length()>=6&&phone.length()==8;
    }

    @Override
    protected void onDestroy() {
        binding = null;
        mAuth = null;
        progressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}