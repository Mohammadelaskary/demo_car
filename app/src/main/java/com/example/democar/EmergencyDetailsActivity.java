package com.example.democar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.democar.databinding.ActivityEmergencyDetailsBinding;

public class EmergencyDetailsActivity extends AppCompatActivity {
    ActivityEmergencyDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityEmergencyDetailsBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());
         Intent intent = getIntent();
         String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        binding.title.setText(title);
        binding.body.setText(body);
        binding.back.setOnClickListener(v->{
            onBackPressed();
        });
    }
}