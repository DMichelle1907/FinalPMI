package com.example.finalpmi.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalpmi.R;
import com.example.finalpmi.databinding.FragmentAddusersgroupsBinding;

public class UsersAddGroupFragment extends Fragment {

    private FragmentAddusersgroupsBinding binding;
    private Button btnAgregaraGrupo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UsersViewModel usersViewModel =
                new ViewModelProvider(this).get(UsersViewModel.class);

        binding = FragmentAddusersgroupsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnAgregaraGrupo =  root.findViewById(R.id.btnAgregarUGrupo);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}