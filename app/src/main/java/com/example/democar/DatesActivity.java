package com.example.democar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.democar.Adapters.DatesAdapter;
import com.example.democar.Model.WorkShop;
import com.example.democar.databinding.ActivityDatesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class DatesActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDatesBinding binding;
    FirebaseDatabase database;
    List<String> availableDatesString = new ArrayList<>();
    DatesAdapter adapter;
    String workshopId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
   //     getAvailableDates();
        binding.datePicker.setMinDate(System.currentTimeMillis()-1);
        if (getIntent().getStringArrayListExtra("availableDates")!=null)
            availableDatesString.addAll(getIntent().getStringArrayListExtra("availableDates"));
        workshopId = getIntent().getStringExtra("workshopId");
        setUpRecyclerView();
        attachButtonsToListner();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                String day = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;
                if (!availableDatesString.contains(day)){
                    availableDatesString.add(day);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DatesActivity.this, "Date already added!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



    private void attachButtonsToListner() {
        binding.back.setOnClickListener(this);
        binding.changeDates.setOnClickListener(this);
    }

    private void setUpRecyclerView() {
        adapter = new DatesAdapter(availableDatesString,DatesActivity.this);
        binding.selectedDates.setAdapter(adapter);
        binding.selectedDates.setLayoutManager(new GridLayoutManager(this,2));
    }


    private void getAvailableDates() {
        progressDialog.show();
        DatabaseReference reference = database.getReference("Workshops");
        Query query = reference.orderByChild("workshopId").equalTo(workshopId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availableDatesString.clear();
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    WorkShop workShop = dataSnapshot.getValue(WorkShop.class);
                    Log.d("====dates",workShop.getAvailableCalendars().size()+"");
                    if (workShop.getAvailableCalendars()!=null){
                        availableDatesString.addAll(workShop.getAvailableCalendars());

                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.back: onBackPressed(); break;
            case R.id.change_dates:{
                if (availableDatesString.isEmpty())
                    Toast.makeText(DatesActivity.this, "No dates found", Toast.LENGTH_SHORT).show();
                else
                    uploadDates();
            } break;
        }
    }
    ProgressDialog progressDialog;
    private void uploadDates() {
        progressDialog.show();
        Map<String,Object> calendarsMap = new HashMap<>();
        calendarsMap.put("availableCalendars",availableDatesString);
        DatabaseReference reference = database.getReference("Workshops");
        Query query = reference.orderByChild("workshopId").equalTo(workshopId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    dataSnapshot.getRef().updateChildren(calendarsMap);
                }
                progressDialog.dismiss();
                onBackPressed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}