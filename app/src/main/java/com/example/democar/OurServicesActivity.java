package com.example.democar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.democar.databinding.ActivityOurServicesBinding;

public class OurServicesActivity extends AppCompatActivity {
    ActivityOurServicesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOurServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(v->{
            onBackPressed();
        });
    }
}