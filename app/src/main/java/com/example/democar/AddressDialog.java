package com.example.democar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.democar.databinding.AddressDialogLayoutBinding;

public class AddressDialog extends Dialog {
    public AddressDialog(@NonNull Context context) {
        super(context);
    }
    AddressDialogLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = AddressDialogLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.order.setOnClickListener(v->{
            String address = binding.address.getEditText().getText().toString().trim();
            if (!address.isEmpty()) {
                Toast.makeText(getContext(), "Winch Ordered Successfully", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please enter your address!", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
