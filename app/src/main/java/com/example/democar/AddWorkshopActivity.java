package com.example.democar;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.democar.Adapters.AddImagesAdapter;
import com.example.democar.Model.WorkShop;
import com.example.democar.databinding.ActivityAddWorkshopBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddWorkshopActivity extends AppCompatActivity implements View.OnClickListener,AddImage {
    ActivityAddWorkshopBinding binding;
    ActivityResultLauncher<Intent> openGalleryResultLauncher;
    List<String> imagesUris = new ArrayList<>();
    List<String> imagesUrls = new ArrayList<>();
    AddImagesAdapter adapter;
    FirebaseDatabase database;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddWorkshopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        attachButtonToListener();
        storage = FirebaseStorage.getInstance().getReference("Images");
        openGalleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getData().getClipData() != null) {
                            int count = result.getData().getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                            for(int i = 0; i < count; i++) {
                                Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                imagesUris.add(imageUri.toString());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Uri imageUri = result.getData().getData();
                            imagesUris.add(imageUri.toString());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        setUpRecyclerView();
        addEditTextWatcher();
    }

    private void addEditTextWatcher() {
        binding.workshopName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.workshopName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.workshopName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.workshopName.setError(null);
            }
        });
        binding.workshopDescribtion.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.workshopDescribtion.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.workshopDescribtion.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.workshopDescribtion.setError(null);
            }
        });
    }

    private void setUpRecyclerView() {
        imagesUris.add("");
        adapter = new AddImagesAdapter(imagesUris,this,this);
        binding.images.setAdapter(adapter);
        binding.images.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void attachButtonToListener() {
        binding.add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.add:{
                String workshopName = binding.workshopName.getEditText().getText().toString().trim();
                String workshopDescription = binding.workshopDescribtion.getEditText().getText().toString().trim();
                if (isValidData(workshopName,workshopDescription)){
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Thank you for your patience..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    uploadImagesAndData();
                }
            } break;
            case R.id.back:{
                onBackPressed();
            } break;
        }
    }


    ProgressDialog progressDialog;
    StorageReference storage;
    int index = 1;
    private void uploadImagesAndData() {
        if (index < imagesUris.size()) {
            String imageFileName;

            imageFileName = FirebaseAuth.getInstance().getUid() + System.currentTimeMillis() + "." + getFileExtension(Uri.parse(imagesUris.get(index)));
            final StorageReference fileReference = storage.child(imageFileName);
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imagesUris.get(index)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = fileReference.putBytes(data);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imagesUrls.add(downloadUri.toString());
                        index++;
                        Log.d("====done", downloadUri.toString());

                        uploadImagesAndData();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "فشل في تحميل الصورة، برجاء المحاولة في وقت آخر"
                            , Toast.LENGTH_LONG).show();
                }
            });
        }  else{
            uploadData();
        }
    }
    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = Objects.requireNonNull(getApplicationContext()).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
    private void uploadData() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String workshopId = FirebaseDatabase.getInstance().getReference().push().getKey();
        String workshopName = binding.workshopName.getEditText().getText().toString().trim();
        String workshopDescription = binding.workshopDescribtion.getEditText().getText().toString().trim();
        WorkShop workShop = new WorkShop(workshopId,workshopName,userId,workshopDescription,imagesUrls);
        DatabaseReference reference = database.getReference("Workshops");
        reference.push().setValue(workShop).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(AddWorkshopActivity.this, "Workshop Created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddWorkshopActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(AddWorkshopActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent,options.toBundle());
                }
            }
        });
    }

    private boolean isValidData(String workshopName, String workshopDescription) {
        if (workshopName.isEmpty())
            binding.workshopName.setError("Please Enter Workshop Name");
        if (workshopDescription.isEmpty())
            binding.workshopDescribtion.setError("Please Enter Workshop Description");
        boolean isValid = false;
        if (!workshopName.isEmpty()&&!workshopDescription.isEmpty())
            isValid = true;
        return isValid;
    }


    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        openGalleryResultLauncher.launch(intent);
    }

    @Override
    public void OnAddImageClicked() {
        openGallery();
    }
}