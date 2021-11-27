package com.example.democar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.democar.Adapters.ImagesAdapterDrawable;
import com.example.democar.databinding.ActivityEmergencyDetailsBinding;

import java.util.ArrayList;

public class EmergencyDetailsActivity extends AppCompatActivity {
    ActivityEmergencyDetailsBinding binding;
    ImagesAdapterDrawable adapter;
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

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        ArrayList<Integer> images = getIntent().getIntegerArrayListExtra("images");
        adapter = new ImagesAdapterDrawable(images);
        binding.images.setAdapter(adapter);
        binding.images.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }
}