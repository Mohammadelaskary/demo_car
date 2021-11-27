package com.example.democar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.democar.Adapters.EmergencyAdapter;
import com.example.democar.Model.Emergency;
import com.example.democar.databinding.ActivityEmergencyBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmergencyActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEmergencyBinding binding;
    EmergencyAdapter adapter;
    List<Emergency> emergencies=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setUpToolbar();
        changeStatusBarColor();
        attachOnlclickToButtons();
        addEmergenciesDummyData();
        attachrecyclerViewToAdapter();

    }

    private void addEmergenciesDummyData() {
        Emergency emergency = new Emergency("How can i change car tires?", "Find a Safe Place to Pull Over\n" +
                "Use Your Hazard Lights and Parking Brake. \n" +
                "Check for Materials.\n" +
                "Loosen the Lug Nuts\n" +
                "Lift Your Vehicle Off the Ground.\n" +
                "Remove the Lug Nuts and the Tire\n" +
                "Place the Spare Tire on the Car\n" +
                "Replace the Lug Nuts.\n",
                new ArrayList<Integer>(){{
                    add(R.drawable.tire1);
                            add(R.drawable.tire2);
                            add(R.drawable.tire3);
                            add(R.drawable.tire4);
                            add(R.drawable.tire5);
                }});
        Emergency emergency1 = new Emergency("What i do for smoke comes out of my car?", "Do a quick visual inspection. You could have run over a plastic bag that is burning on the catalytic converter. Never touch or work on a hot engine. Wait until the engine cools down and check the fluids. However, if you see fluids dripping or puddles forming under the hood or on the ground, it’s time to call for a tow. ",
                new ArrayList<Integer>(){{
                    add(R.drawable.smoke1);
                    add(R.drawable.smoke2);
                    add(R.drawable.smoke3);
                    add(R.drawable.smoke4);
                }});
        emergencies.clear();
        emergencies.add(emergency);
        emergencies.add(emergency1);

    }

    private void attachrecyclerViewToAdapter() {
        adapter = new EmergencyAdapter(emergencies,this);
        binding.emergencies.setAdapter(adapter);
    }

    private void attachOnlclickToButtons() {
        binding.back.setOnClickListener(this);
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolBar);
    }
    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(EmergencyActivity.this, android.R.anim.slide_in_left, android.R.anim.fade_out);
        switch (id){
            case R.id.back:{
                onBackPressed();
                finish();
            } break;
        }
    }
}