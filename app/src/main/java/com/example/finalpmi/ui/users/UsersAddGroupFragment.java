package com.example.finalpmi.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalpmi.Data.ResApi;
import com.example.finalpmi.MainActivity;
import com.example.finalpmi.R;
import com.example.finalpmi.databinding.FragmentAddusersgroupsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersAddGroupFragment extends Fragment {

    private FragmentAddusersgroupsBinding binding;
    private Button btnAgregaraGrupo;
    private ArrayList<String> usersList;
    private ArrayAdapter<String> adapter;

  ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UsersViewModel usersViewModel =
                new ViewModelProvider(this).get(UsersViewModel.class);

        binding = FragmentAddusersgroupsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnAgregaraGrupo =  root.findViewById(R.id.btnAgregarUGrupo);
        listView = root.findViewById(R.id.ListUsuariosG);

        // Inicializa usersList
        usersList = new ArrayList<>();

        // Inicializa ArrayAdapter
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, usersList);

        // Establece el adaptador en el ListView
        listView.setAdapter(adapter);

        // Realiza la solicitud a la red
        fetchDataForListView();

        return root;
    }

    private void fetchDataForListView() {
        // Reemplaza con el endpoint de tu API para obtener usuarios por carrera
        String url =  ResApi.url_server+ResApi.select_userByCareer+"?id_carrera="+1;
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsear la respuesta JSON manualmente
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("users");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject usuarioJson = jsonArray.getJSONObject(i);
                                int id = usuarioJson.getInt("id");
                                String nombre = usuarioJson.getString("nombre");
                                String apellido = usuarioJson.getString("apellido");

                                String nombreCompleto = nombre + " " + apellido;
                                usersList.add(nombreCompleto);
                            }
                            adapter.notifyDataSetChanged(); // Notifica al adaptador sobre el cambio de datos
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error al procesar la respuesta JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(requireContext(), "Error de red al obtener datos", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}