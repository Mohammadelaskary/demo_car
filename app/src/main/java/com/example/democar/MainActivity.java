package com.example.democar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;

import com.example.democar.Adapters.WorkshopsAdapter;
import com.example.democar.Model.User;
import com.example.democar.Model.WorkShop;
import com.example.democar.Notification.NotificationService;
import com.example.democar.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    WorkshopsAdapter adapter;
    FirebaseDatabase database;
    List<WorkShop> workShops = new ArrayList<>();
    boolean isUser,isLoaded = false,isUploaded = false;
    String isMechanic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        database = FirebaseDatabase.getInstance();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        deleteCache(MainActivity.this);
        changeStatusBarColor();
        setupToolbar();
        attachOnClickToButtons();
        getUserType(userId);


    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolBar);
    }

    void getUserType(String userId){
        Log.d("----currentUserId",userId);
        DatabaseReference reference = database.getReference("Users");
        Query query = reference.orderByChild("id").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    isMechanic = user.getMechanic();
                }
                getWorkshopsData(isMechanic);
                Log.d("isMechanic",isMechanic+"");
                binding.notificationButton.setVisibility(View.VISIBLE);
                if (isMechanic.equals("mechanic")){
                    binding.notificationButton.setIcon(getDrawable(R.drawable.ic_notifications));
                    binding.addWorkshop.setVisibility(View.VISIBLE);
                    updateToken();
                } else {
                    binding.notificationButton.setIcon(getDrawable(R.drawable.ic_exclamation_mark));
                    binding.addWorkshop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    String userId ;
    private void getWorkshopsData(String isMechanic) {
        DatabaseReference reference = database.getReference("Workshops");

        if (!isLoaded)
            binding.loading.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.loading.hide();
                workShops.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    WorkShop workShop = dataSnapshot.getValue(WorkShop.class);
                    binding.notificationButton.setVisibility(View.VISIBLE);
                    if (isMechanic.equals("mechanic")) {
                        if (workShop.getUserId().equals(userId)) {
                            workShops.add(workShop);

                        }
                    } else {
                        workShops.add(workShop);
                    }
                }
                isLoaded = true;
                if (workShops.isEmpty())
                    binding.noWorkshops.setVisibility(View.VISIBLE);
                attachRecyclerViewToAdapter();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                uploadToken(task.getResult().toString());
            }
        });
    }

    private void uploadToken(String token) {
        Map<String,Object> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        DatabaseReference reference = database.getReference("Workshops");
        Query query = reference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    dataSnapshot.getRef().updateChildren(tokenMap);
                }
                isUploaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void attachRecyclerViewToAdapter() {
        adapter = new WorkshopsAdapter(workShops,this,isMechanic);
        Collections.reverse(workShops);
        binding.workshops.setAdapter(adapter);

    }


    private void attachOnClickToButtons() {
        binding.notificationButton.setOnClickListener(this);
        binding.optionsButton.setOnClickListener(this);
        binding.addWorkshop.setOnClickListener(this);
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
        ActivityOptions  options =
                ActivityOptions.makeCustomAnimation(MainActivity.this, android.R.anim.slide_in_left, android.R.anim.fade_out);
        switch (id){
            case R.id.notification_button: {
                if (isMechanic.equals("mechanic")){
                    intent = new Intent(MainActivity.this, NotificationsActivity.class);
            } else {
                    intent = new Intent(MainActivity.this, EmergencyActivity.class);
                }
                startActivity(intent,options.toBundle());
            } break;
            case R.id.options_button:{
                PopupMenu popup = new PopupMenu(this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.logout, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        ActivityOptions options = null;
                        Intent intent = null;
                        if (id == R.id.logout) {

                            deleteCache(MainActivity.this);
                            intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            options =
                                    ActivityOptions.makeCustomAnimation(MainActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                            removeToken(userId);
                            FirebaseAuth.getInstance().signOut();

                        }
                        startActivity(intent,options.toBundle());
                        return true;
                    }
                });
                popup.show();
            } break;
            case R.id.add_workshop:{
                intent = new Intent(MainActivity.this, AddWorkshopActivity.class);
                options =
                        ActivityOptions.makeCustomAnimation(MainActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent,options.toBundle());
            } break;
        }

    }

    private void removeToken(String id) {
        Map<String,Object> tokenMap = new HashMap<>();
        tokenMap.put("token",null);
        DatabaseReference reference = database.getReference("Workshops");
        Query query = reference.orderByChild("userId").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dataSnapshot.getRef().updateChildren(tokenMap);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    @Override
    protected void onDestroy() {


        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}