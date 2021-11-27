package com.example.democar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.democar.Adapters.FrequentlyQuestionsAdapter;
import com.example.democar.Model.Question;
import com.example.democar.databinding.ActivityFrequentlyQuestionsBinding;

import java.util.ArrayList;
import java.util.List;

public class FrequentlyQuestionsActivity extends AppCompatActivity {
    ActivityFrequentlyQuestionsBinding binding;
    List<Question> questions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrequentlyQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setData();
        Log.d("questios_number",questions.size()+"");
        setUpRecyclerView();
        attachButtonsToListener();
    }

    private void attachButtonsToListener() {
        binding.back.setOnClickListener(v->{
            onBackPressed();
        });
    }
    FrequentlyQuestionsAdapter adapter;
    private void setUpRecyclerView() {

        adapter = new FrequentlyQuestionsAdapter(questions,this);
        binding.questions.setAdapter(adapter);
        setUpSearchView();
    }

    private void setUpSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }



    private void setData() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.car_oil_1);
        images.add(R.drawable.car_oil_2);
        images.add(R.drawable.car_oil_3);
        Question question = new Question("When should i change car oil?",
                "Depending on vehicle age, type of oil and driving conditions, oil change intervals will vary. It used to be normal to change the oil every 3,000 miles, but with modern lubricants most engines today have recommended oil change intervals of 5,000 to 7,500 miles. Moreover, if your car's engine requires full-synthetic motor oil, it might go as far as 15,000 miles between services! ",
                images);
        questions.add(question);


        List<Integer> images1 = new ArrayList<>();
        images1.add(R.drawable.change_tire);
        Question question1 = new Question("When should i change car tires?",
                "Find a Safe Place to Pull Over\n" +
                        "Use Your Hazard Lights and Parking Brake. \n" +
                        "Check for Materials.\n" +
                        "Loosen the Lug Nuts\n" +
                        "Lift Your Vehicle Off the Ground.\n" +
                        "Remove the Lug Nuts and the Tire\n" +
                        "Place the Spare Tire on the Car\n" +
                        "Replace the Lug Nuts.\n",
                images1);
        questions.add(question1);
        List<Integer> images2 = new ArrayList<>();
        images2.add(R.drawable.change_windscreen_1);
        images2.add(R.drawable.change_windscreen_2);
        Question question2 = new Question("When should i change car windscreen wipers?",
                "Recommend that you replace your wiper blades every 12 months to ensure they continue to provide maximum visibility.",
                images2);
        questions.add(question2);
        Log.d("image_number",images.size()+"");
    }
}