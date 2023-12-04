package com.example.finalpmi.ui.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.finalpmi.R;
import com.example.finalpmi.databinding.FragmentGroupsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupsFragment extends Fragment {

    private FragmentGroupsBinding binding;
    private FloatingActionButton btnCrearGrupo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GroupsViewModel homeViewModel =
                new ViewModelProvider(this).get(GroupsViewModel.class);

        binding = FragmentGroupsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        btnCrearGrupo = root.findViewById(R.id.btnCreargrupo);
        btnCrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inicia la navegación hacia el fragment ProfileFragment
                Navigation.findNavController(view).navigate(R.id.action_nav_groups_to_createGroupFragment);
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