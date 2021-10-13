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
        Emergency emergency = new Emergency("What Do You Do When A Car Tire Explodes?","Having your tire blowout while driving can be one of the scariest moments on the road. While you should always try to prevent tire blowouts, it’s also helpful to know what to do in the moment. If you find yourself facing a tire blow out, remember to never jerk your steering wheel or slam on the brakes as this can lead to immense danger. Instead, keep calm, let your car naturally slow down, and get to a safe place!\n" +
                "       -١-\n" +
                "Listen for telltale signs. There are three sounds associated with tire blowouts, and they happen in quick succession. First, you’ll hear a loud boom, shortly followed by a whooshing sound as the air rushes out of your tire. When the air has left, you’ll hear a flapping sound, which is the sound of your tire hitting the road\n" +
                "        -٢-\n" +
                "Recognize changes in your steering wheel. After a tire blowout, your car will probably become difficult to steer. This can be indicative of a slower blowout, which might not have caused as much noise as a blowout explosion.\n" +
                "     -٣-\n" +
                "Notice if the car starts pulling in one direction. In the event of a blowout, your car will start pulling sharply towards the direction of the blown tire, regardless if the front or wheel tire blew out. This, more than any other sign, is an indication that your tire has blown out");
        Emergency emergency1 = new Emergency("What Do You Do When Smoke Comes Out Of The Car?","A little bit of white smoke from the exhaust on a cold morning can just mean steam, and is fairly normal.\n" +
                "\n" +
                "But what’s a normal amount of smoke from your car – and what should you call the service department up for?\n" +
                "\n" +
                "Overall, your car shouldn’t blow a lot of smoke, especially if it’s a newer model that should have all sorts of filters in the engine to stop pollution. Older cars should usually run fairly clear as well – as long as they’ve been properly serviced – but you might expect an older car to blow a little bit more smoke than usual, but not so much that you’re blinding other cars on the road.\n" +
                "\n" +
                "There are three colours of smoke that commonly indicate a problem with the engine, and you should take notice if your car starts to produce excess smoke.\n" +
                "BLUE SMOKE\n" +
                "Blue smoke usually indicates a problem with oil, and can often be confused for white smoke leading to bigger problems down the line if the right parts aren’t addressed at service.  While it’s commonly called ‘blue’ smoke, it’s commonly more of a purple-grey or a very light blue.\n" +
                "\n" +
                "This smoke is often accompanied by an overall loss of power, or the car struggling to accelerate. This is certainly the case with turbocharged cars – as blue smoke can also mean that the turbocharger has something wrong with it.\n" +
                "\n" +
                "On a regular, petrol-powered vehicle, blue smoke from the exhaust usually means that the car is burning oil – but there can be several causes for this. There may be a leaking valve, which is letting oil get into places in the engine that it’s not supposed to, causing smoke to come out of the exhaust. If your exhaust starts blowing blue smoke, it's best to pull over and call your roadside assistance provider, if you have one. If you keep driving, not only are you creating a hazard for other road users, any leaking oil could catch fire in your engine bay.\n" +
                "\n" +
                "\n" +
                "BLACK SMOKE\n" +
                "Black smoke from the exhaust points to a problem with the fuel, which is either contaminated or mixing too heavily with oil in the engine (it’s supposed to mix, but with the right balance of oil and fuel). If it’s just a burst of smoke that then clears, then there shouldn’t be a serious problem – but it’s probably worth mentioning it to your service advisor at your next service.\n" +
                "\n" +
                "If there is an excess amount of black smoke pouring out of your exhaust, then you have a bigger problem. The sensors, injection system or fuel line may not be functioning properly, all of which will need a trip to the workshop to address the problem. Leaving the problem may cause you to use more fuel than usual, as well as creating a hazard on the road and potentially making your car unroadworthy. As with blue smoke, it's best not to keep driving with it, as any fluids coming out of the fuel line are potentially flammable.\n" +
                "\n" +
                "\n" +
                "HOW TO FIX IT\n" +
                "Fixing a smoking car is a little trickier than topping up fluids, because it’s usually an indicator of a problem that could be more serious than you realise.\n" +
                "\n" +
                "You can diagnose or isolate the problem, which will help you tell the service advisor, so that they can get a better idea of what to treat when they’re looking at your vehicle. Take note of any loss of power, or rough running at speed or idle and noises coming from the engine.\n" +
                "\n" +
                "Get it towed to a service centre – try not to drive it, because that may damage the engine more.");

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