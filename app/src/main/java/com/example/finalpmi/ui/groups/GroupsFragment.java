package com.example.finalpmi.ui.groups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalpmi.Data.ResApi;
import com.example.finalpmi.R;
import com.example.finalpmi.databinding.FragmentGroupsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    public void insert_group(){
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        String url=ResApi.url_server+ResApi.insert_group;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            if(jsonArray.length()>0){

                            }

                        }catch(JSONException e){

                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("Volley Error", errorMessage);

                    }
                }){
        };

        queue.add(request);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}