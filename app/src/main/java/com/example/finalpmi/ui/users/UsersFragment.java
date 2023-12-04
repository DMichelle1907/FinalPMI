package com.example.finalpmi.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.finalpmi.R;
import com.example.finalpmi.databinding.FragmentUsersBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


    public class UsersFragment extends Fragment {

        private FragmentUsersBinding binding;
        private FloatingActionButton btnAgregarUsuario, btnBuscar;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            UsersViewModel usersViewModel =
                    new ViewModelProvider(this).get(UsersViewModel.class);

            binding = FragmentUsersBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            // Haciendo referencia a los botones
            btnAgregarUsuario = root.findViewById(R.id.btnAgregarUsuario);
            btnBuscar = root.findViewById(R.id.btnBuscarUsuario);

            btnAgregarUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Inicia la navegación hacia el fragment ProfileFragment
                    Navigation.findNavController(view).navigate(R.id.action_nav_users_to_nav_usersadd);
                }
            });

            btnBuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Agrega el código para el botón de búsqueda aquí
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
