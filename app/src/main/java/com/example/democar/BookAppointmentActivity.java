package com.example.democar;

import static com.example.democar.Notification.ApiClient.PROJECT_ID;
import static com.example.democar.Notification.ApiInterface.SERVER_KEY;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.democar.Adapters.ImagesAdapter;
import com.example.democar.Model.NotificationModel;
import com.example.democar.Model.WorkShop;
import com.example.democar.Notification.ApiClient;
import com.example.democar.Notification.ApiInterface;
import com.example.democar.Notification.Message;
import com.example.democar.Notification.Notification;
import com.example.democar.Notification.RootModel;
import com.example.democar.databinding.ActivityBookAppointmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    ActivityBookAppointmentBinding binding;
    List<String> availableDays = new ArrayList<>();
    List<String> imagesUrls = new ArrayList<>();
    FirebaseDatabase database;
    WorkShop workShop;
    String token;
    ApiInterface apiInterface;
    ImagesAdapter adapter;
    String name,workShopOwnerUserId;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        database = FirebaseDatabase.getInstance();

        workShop = getIntent().getParcelableExtra("workshop");
        workShopOwnerUserId = workShop.getUserId();
        name = workShop.getWorkshopName();
        String description = workShop.getDescription();
        imagesUrls = getIntent().getStringArrayListExtra("imagesUrls");
        setUpImages();
        token = workShop.getToken();
        availableDays = getIntent().getStringArrayListExtra("availableDates");
        binding.workshopDescribtion.setText(description);
        binding.name.setText(name);
        binding.bookAppointment.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        binding.chooseDate.setOnClickListener(this);
        changeStatusBarColor();

    }

    private void setUpImages() {
        adapter = new ImagesAdapter(imagesUrls,this);
        binding.images.setAdapter(adapter);
        binding.images.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.book_appointment:{
                showAlertDialog();
            } break;
            case R.id.back: onBackPressed(); break;
            case R.id.choose_date:{
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd;
                dpd = DatePickerDialog.newInstance(
                        BookAppointmentActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );


// If you're calling this from a support Fragment
//                if (availableDays!=null) {
                if (availableDays!=null){
                    dpd.setSelectableDays(convertDaysToCalendars(availableDays));
                    dpd.show(getSupportFragmentManager(), "DatePicker");

                } else
                    Toast.makeText(BookAppointmentActivity.this, "No dates Available", Toast.LENGTH_SHORT).show();

//                } else
//                    Toast.makeText(BookAppointmentActivity.this, "No Available Dates Found!", Toast.LENGTH_SHORT).show();
            } break;
        }
    }

    private Calendar[] convertDaysToCalendars(List<String> availableDays) {
        Calendar [] calendars  = new Calendar[availableDays.size()];
        for (int i = 0; i < availableDays.size(); i++) {
            String [] dateSplit = availableDays.get(i).split("/");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateSplit[0]));
            calendar.set(Calendar.MONTH,Integer.parseInt(dateSplit[1])-1);
            calendar.set(Calendar.YEAR,Integer.parseInt(dateSplit[2]));
            calendars[i] = calendar;
        }
        return calendars;
    }

    private void showAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure to book this appointment?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendDate();
                        Toast.makeText(BookAppointmentActivity.this, "Appointment Booked", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void sendDate() {
        String notificationId = database.getReference().push().getKey();
        NotificationModel notificationModel = new NotificationModel(notificationId,workShopOwnerUserId,name +" has New Date at "+day);
        DatabaseReference reference = database.getReference("Notifications");
        reference.push().setValue(notificationModel).addOnCompleteListener(task -> {
            Toast.makeText(this, "Date uploaded", Toast.LENGTH_SHORT).show();
            pushNotification();
            onBackPressed();
        });
    }


    private void pushNotification() {
        Notification notification = new Notification(name+" Has New Date at "+day ,"New Date");
        RootModel model = new RootModel(token,notification);
        apiInterface.sendNotification(model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("myNotificationResponse",response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("myNotificationErro",t.getMessage().toString());
            }
        });
    }

    String day;
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        day = dayOfMonth + "/" + monthOfYear + "/" + year;
        if (!availableDays.contains(day)){
                binding.day.setText(dayOfMonth+"");
                binding.month.setText(getMonth(monthOfYear));
                binding.year.setText(year+"");
                binding.choosenDate.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Day not Available", Toast.LENGTH_SHORT).show();
                binding.choosenDate.setVisibility(View.GONE);
            }
    }
    String getMonth(int month){
        switch (month){
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "Jul";
            case 7: return "Aug";
            case 8: return "Sep";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default: return "Error";
        }
    }
}