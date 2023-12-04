package com.example.finalpmi.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalpmi.R;
import com.example.finalpmi.databinding.FragmentProfileEditBinding;

public class ProfileEditFragment extends Fragment {

    private FragmentProfileEditBinding binding;

    private Button btnGuardar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileEditViewModel profileEditViewModel =
                new ViewModelProvider(this).get(ProfileEditViewModel.class);

        binding = FragmentProfileEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnGuardar = root.findViewById(R.id.btnGuardarEdit);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

         return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}