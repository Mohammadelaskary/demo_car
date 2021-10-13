package com.example.democar.Adapters;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.democar.databinding.WorkshopImageItemBinding;
import com.example.democar.databinding.WorkshopsItemBinding;

import java.net.URI;
import java.net.URL;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {
    List<String> imagesUrls;
    Context context;

    public ImagesAdapter(List<String> imagesUrls, Context context) {
        this.imagesUrls = imagesUrls;
        this.context = context;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImagesViewHolder(WorkshopImageItemBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        String url = imagesUrls.get(position);
        if (URLUtil.isValidUrl(url)) {
            Glide.with(context).load(url).into(holder.binding.workshopImage);
        }else {
            holder.binding.workshopImage.setImageURI(Uri.parse(url));
        }
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
