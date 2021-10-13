package com.example.democar.Adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.democar.BookAppointmentActivity;
import com.example.democar.DatesActivity;
import com.example.democar.LoginActivity;
import com.example.democar.MainActivity;
import com.example.democar.Model.WorkShop;
import com.example.democar.databinding.WorkshopsItemBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class WorkshopsAdapter extends RecyclerView.Adapter<WorkshopsAdapter.WorkshopsViewHolder> {
    List<WorkShop> workShops;
    Context context;
    String isMechanic;

    public WorkshopsAdapter(List<WorkShop> workShops, Context context,String isMechanic) {
        this.workShops = workShops;
        this.context = context;
        this.isMechanic = isMechanic;
    }

    @NonNull
    @Override
    public WorkshopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkshopsItemBinding binding = WorkshopsItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new WorkshopsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopsViewHolder holder, int position) {
        WorkShop workShop = workShops.get(position);
        String name = workShop.getWorkshopName();
        String description = workShop.getDescription();
        List<String> imageUrls    = workShop.getImageUrls();

        holder.binding.workshopName.setText(name);
        holder.binding.workshopDescribtion.setText(description);
        setUpRecycler(imageUrls,holder.binding.workshopsImages);

        holder.binding.bookAppointment.setOnClickListener(v->{
            Intent intent = new Intent(context, BookAppointmentActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(context, android.R.anim.slide_in_left, android.R.anim.fade_out);
            intent.putExtra("workshop",workShop);
            intent.putStringArrayListExtra("availableDates", (ArrayList<String>) workShop.getAvailableCalendars());
            intent.putStringArrayListExtra("imagesUrls", (ArrayList<String>) workShop.getImageUrls());
            context.startActivity(intent,options.toBundle());
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = null;
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);
            if (isMechanic.equals("mechanic")) {
                intent = new Intent(context, DatesActivity.class);
                intent.putStringArrayListExtra("availableDates", (ArrayList<String>) workShop.getAvailableCalendars());
                intent.putStringArrayListExtra("imagesUrls", (ArrayList<String>) workShop.getImageUrls());
                intent.putExtra("workshopId",workShop.getWorkshopId());
                Log.d("======mechamic",isMechanic+"");
                context.startActivity(intent,options.toBundle());
            }

        });

        if (isMechanic.equals("mechanic"))
            holder.binding.bookAppointment.setVisibility(View.GONE);


    }
    ImagesAdapter adapter;
    private void setUpRecycler(List<String> imageUrls, RecyclerView workshopsImages) {
        adapter = new ImagesAdapter(imageUrls,context);
        workshopsImages.setAdapter(adapter);
        workshopsImages.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
    }


    @Override
    public int getItemCount() {
        return workShops==null?0: workShops.size();
    }

    static class WorkshopsViewHolder extends RecyclerView.ViewHolder{
        WorkshopsItemBinding binding;
        public WorkshopsViewHolder(@NonNull WorkshopsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
