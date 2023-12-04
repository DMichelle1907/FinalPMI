package com.example.finalpmi.ui.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.finalpmi.databinding.FragmentCreateGroupBinding;
import com.example.finalpmi.R;
import com.example.finalpmi.ui.profile.ProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateGroupFragment extends Fragment {
     private FloatingActionButton btnAddUsuarioAGrupo;
    private FragmentCreateGroupBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel homeViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentCreateGroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnAddUsuarioAGrupo = root.findViewById(R.id.btnAgregarUGrupo);
        btnAddUsuarioAGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inicia la navegación hacia el fragment ProfileFragment
                Navigation.findNavController(view).navigate(R.id.action_createGroupFragment_to_usersAddGroupFragment);
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