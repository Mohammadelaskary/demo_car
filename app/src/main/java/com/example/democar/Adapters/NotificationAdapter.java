package com.example.democar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democar.Model.NotificationModel;
import com.example.democar.SeenInterface;
import com.example.democar.databinding.NotificationItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    List<NotificationModel> notificationModels;
    Context context;

    public NotificationAdapter(List<NotificationModel> notificationModels, Context context) {
        this.notificationModels = notificationModels;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationItemBinding binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notificationModel = notificationModels.get(position);
        String body = notificationModel.getBody();
        boolean seen = notificationModel.isSeen();
        holder.binding.notificationBody.setText(body);

        if (seen)
            holder.itemView.setAlpha(0.5f);


    }




    @Override
    public int getItemCount() {
        return notificationModels ==null?0: notificationModels.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        NotificationItemBinding binding;
        public NotificationViewHolder(@NonNull NotificationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
