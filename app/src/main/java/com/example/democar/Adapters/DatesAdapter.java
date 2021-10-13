package com.example.democar.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democar.databinding.DatesItemBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.DatesViewHolder> {
    List<String> calendarList;
    Context context;

    public DatesAdapter(List<String> calendarList,Context context) {
        this.calendarList = calendarList;
        this.context = context;
    }

    @NonNull
    @Override
    public DatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DatesViewHolder(DatesItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DatesViewHolder holder, int position) {
        int currentPoristion = position;
        String day = calendarList.get(currentPoristion);
        holder.binding.date.setText(day);
        holder.itemView.setOnLongClickListener(v -> {
            final CharSequence[] items = {"Delete date"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    calendarList.remove(currentPoristion);
                    notifyDataSetChanged();
                }
            });
            builder.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return calendarList==null?0:calendarList.size();
    }

    static class DatesViewHolder extends RecyclerView.ViewHolder {
        DatesItemBinding binding;
        public DatesViewHolder(@NonNull DatesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
