package com.example.finalpmi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalpmi.Data.AlertMessage;
import com.example.finalpmi.Data.ResApi;
import com.example.finalpmi.Data.Users;
import com.example.finalpmi.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityRegistrarse extends AppCompatActivity {
    EditText edtNombres, edtApellidos, edtCorreo, edtTelefono, edtDni, edtPassword;
    Button btnRegistrarse;
    Spinner Spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        edtNombres = (EditText) findViewById(R.id.ArNombres);
        edtApellidos = (EditText) findViewById(R.id.ArApellidos);
        edtCorreo = (EditText) findViewById(R.id.ArCorreo);
        edtTelefono = (EditText) findViewById(R.id.ArTelefono);
        edtDni = (EditText) findViewById(R.id.ArDni);
        edtPassword = (EditText) findViewById(R.id.ArPassword);
        Spinner = (Spinner) findViewById(R.id.spinner);
        btnRegistrarse = (Button) findViewById(R.id.btnARegistrarse);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los datos ingresados por el usuario
                String nombres = edtNombres.getText().toString();
                String apellidos = edtApellidos.getText().toString();
                String correo = edtCorreo.getText().toString();
                String telefono = edtTelefono.getText().toString();
                String dni = edtDni.getText().toString();
                String password = edtPassword.getText().toString();
                long carrera = Spinner.getSelectedItemId();

                // Crear un objeto Usuario
                Users usuario = new Users(nombres, apellidos, correo, telefono, dni, password, carrera);
                NewUser(usuario);
            }
        });
    }
    private void NewUser(Users usuario){
        RequestQueue queue= Volley.newRequestQueue(this);//queue=cola

        String url= ResApi.url_server+ResApi.insert_user;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>() {
                    AlertMessage message = new AlertMessage();
                    @Override
                    public void onResponse(String response){
                        Log.e("array",response+"");
                        try{
                            JSONArray jsonArray=new JSONArray(response);
                            Log.e("array",jsonArray+"");

                            if(jsonArray.length()>0){
                                message("Alerta", "Correo ya esta en uso",ActivityRegistrarse.this);
                            }else{
                                Intent new_window=new Intent(getApplicationContext(), HomeFragment.class);//new_window=nueva ventana
                                startActivity(new_window);
                            }

                        }catch(Exception e){
                            Log.e("array","Error");
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("Volley Error", errorMessage);
                    }
                }){

        };

        queue.add(request);
    }
    private void ListaCarreras(){//fill_career=llenar carreras
        Message messageListaCarrera = new Message();

        String url=ResApi.url_server+ResApi.select_careers;
        RequestQueue queue=Volley.newRequestQueue(this);//queue=cola

        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            String[] careers=new String[jsonArray.length()];
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject career_object=jsonArray.getJSONObject(i);//career_object=objeto carrera
                                String id=career_object.getString("id");
                                String name=career_object.getString("carrera");
                                String career=id+"-"+name;
                                careers[i]=career;
                            }

                            ArrayAdapter<String> adapter=new ArrayAdapter<>(ActivityRegistrarse.this, android.R.layout.simple_spinner_item, careers);//adapter=adaptador
                            Spinner.setAdapter(adapter);

                        }catch(JSONException e){
                            e.printStackTrace();
                            messageListaCarrera("Error", "Revisa bien: "+e, ActivityRegistrarse.this);
                        }
                    }

                    private void messageListaCarrera(String error, String s, ActivityRegistrarse activityRegistrarse) {
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                messageErrorVolley("Error", "Revisa bien: "+error, ActivityRegistrarse.this);
            }
        });

        queue.add(request);
    }
    private void messageErrorVolley(String error, String s, ActivityRegistrarse activityRegistrarse) {
    }

    private void message(String alerta, String correoYaEstaEnUso, ActivityRegistrarse activityRegistrarse) {
    }
}