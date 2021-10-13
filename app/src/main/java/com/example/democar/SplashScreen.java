package com.example.democar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.Fade;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.democar.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//
        goToActivity();


    }



    private void goToActivity() {
        new CountDownTimer(1000, 1) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent intent;
                Pair<View,String> pair1 = Pair.create(binding.backgroundImage,ViewCompat.getTransitionName(binding.backgroundImage));
                Pair<View,String> pair2 = Pair.create(binding.logo,ViewCompat.getTransitionName(binding.logo));
                Pair<View,String> pair3 = Pair.create(binding.appName,ViewCompat.getTransitionName(binding.appName));
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        SplashScreen.this, pair1,pair2,pair3);
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    intent = new Intent(SplashScreen.this,LoginActivity.class);
                } else {
                    intent = new Intent(SplashScreen.this,MainActivity.class);
                }
                startActivity(intent, options.toBundle());
                finish();
            }

        }.start();
    }


}