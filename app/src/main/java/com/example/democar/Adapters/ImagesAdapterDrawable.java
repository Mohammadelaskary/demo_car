package com.example.democar.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.democar.databinding.WorkshopImageItemBinding;

import java.util.List;

public class ImagesAdapterDrawable extends RecyclerView.Adapter<ImagesAdapterDrawable.ImagesViewHolder> {
    List<Integer> imagesUrls;

    public ImagesAdapterDrawable(List<Integer> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImagesViewHolder(WorkshopImageItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        int url = imagesUrls.get(position);
        holder.binding.workshopImage.setImageResource(url);
        holder.binding.workshopImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        holder.binding.removeImage.setVisibility(View.GONE);
        holder.binding.addImage.setVisibility(View.GONE);
        holder.binding.greyBackground.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return imagesUrls==null?0: imagesUrls.size();
    }

    static class ImagesViewHolder extends RecyclerView.ViewHolder{
        WorkshopImageItemBinding binding;
        public ImagesViewHolder(@NonNull WorkshopImageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
