package com.example.democar.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.democar.AddImage;
import com.example.democar.databinding.WorkshopImageItemBinding;

import java.util.List;

public class AddImagesAdapter extends RecyclerView.Adapter<AddImagesAdapter.AddImagesViewHolder> {
    List<String> imagesUrls;
    Context context;
    ActivityResultLauncher<Intent> openGalleryResultLauncher;
    AddImage addImage;

    public AddImagesAdapter(List<String> imagesUrls, Context context,AddImage addImage) {
        this.imagesUrls = imagesUrls;
        this.context = context;
        this.addImage = addImage;
    }

    @NonNull
    @Override
    public AddImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddImagesViewHolder(WorkshopImageItemBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddImagesViewHolder holder, int position) {
        int currentPosition = position;
        String url;
            url = imagesUrls.get(currentPosition);
        holder.binding.workshopImage.setImageURI(Uri.parse(url));
        if (currentPosition==0){
            holder.binding.removeImage.setVisibility(View.GONE);
            holder.binding.addImage.setVisibility(View.VISIBLE);
        } else {
            holder.binding.removeImage.setVisibility(View.VISIBLE);
            holder.binding.addImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition ==0){
                    addImage.OnAddImageClicked();
                }
            }
        });

        holder.binding.removeImage.setOnClickListener(v->{
            Log.d("====currentPosition",currentPosition+"");
            imagesUrls.remove(currentPosition);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return imagesUrls==null?0: imagesUrls.size();
    }

    static class AddImagesViewHolder extends RecyclerView.ViewHolder{
        WorkshopImageItemBinding binding;
        public AddImagesViewHolder(@NonNull WorkshopImageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
