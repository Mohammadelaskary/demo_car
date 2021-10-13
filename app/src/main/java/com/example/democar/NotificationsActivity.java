package com.example.democar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.democar.Adapters.NotificationAdapter;
import com.example.democar.Model.NotificationModel;
import com.example.democar.databinding.ActivityNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityNotificationsBinding binding;
    NotificationAdapter adapter;

    List<NotificationModel> notificationModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setUpToolbar();
        changeStatusBarColor();
        attachOnlclickToButtons();
        attachrecyclerViewToAdapter();
        getNotifications();

    }

    private void getNotifications() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        Query query = reference.orderByChild("userId").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationModels.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    NotificationModel model = dataSnapshot.getValue(NotificationModel.class);
                    notificationModels.add(model);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void attachrecyclerViewToAdapter() {
        adapter = new NotificationAdapter(notificationModels,this);
        binding.notifications.setAdapter(adapter);
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
                ActivityOptions.makeCustomAnimation(NotificationsActivity.this, android.R.anim.slide_in_left, android.R.anim.fade_out);
        switch (id){
            case R.id.back:{
                onBackPressed();
                finish();
            } break;
        }
    }
    void setSeen (String notificationId){
        Map<String,Object> seenMap = new HashMap<>();
        seenMap.put("seen",true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        Query query = reference.orderByChild("notificationId").equalTo(notificationId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    dataSnapshot.getRef().updateChildren(seenMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (NotificationModel model:notificationModels){
            setSeen(model.getNotificationId());
        }
    }
}