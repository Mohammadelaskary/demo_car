package com.example.democar.Adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democar.EmergencyDetailsActivity;
import com.example.democar.MainActivity;
import com.example.democar.Model.Emergency;
import com.example.democar.databinding.EmergencyItemBinding;

import java.util.ArrayList;
import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder> {
    List<Emergency> emergencies;
    Context context;

    public EmergencyAdapter(List<Emergency> emergencies, Context context) {
        this.emergencies = emergencies;
        this.context = context;
    }

    @NonNull
    @Override
    public EmergencyAdapter.EmergencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EmergencyItemBinding binding = EmergencyItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new EmergencyAdapter.EmergencyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyAdapter.EmergencyViewHolder holder, int position) {
        Emergency emergency = emergencies.get(position);
        String body = emergency.getSteps();
        String question = emergency.getQuestion();
        ArrayList<Integer> images = emergency.getImages();
        holder.binding.question.setText(question);
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, EmergencyDetailsActivity.class);
            intent.putExtra("title",question);
            intent.putExtra("body",body);
            intent.putIntegerArrayListExtra("images",images);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(context, android.R.anim.slide_in_left, android.R.anim.fade_out);
            context.startActivity(intent,options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return emergencies==null?0:emergencies.size();
    }

    static class EmergencyViewHolder extends RecyclerView.ViewHolder {
        EmergencyItemBinding binding;
        public EmergencyViewHolder(@NonNull EmergencyItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
