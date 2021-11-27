package com.example.democar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.democar.databinding.ActivityWinchesBinding;

public class WinchesActivity extends AppCompatActivity {
    ActivityWinchesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWinchesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AddressDialog addressDialog = new AddressDialog(this);
        binding.request.setOnClickListener(v->addressDialog.show());
        binding.request1.setOnClickListener(v->addressDialog.show());
        binding.back.setOnClickListener(v-> onBackPressed());
    }
}