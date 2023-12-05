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

import java.util.HashMap;
import java.util.Map;

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

        // Crear un objeto Usuario

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre",Users.getNombres());
            jsonObject.put("apellidos",Users.getApellidos());
            jsonObject.put("identidad",Users.getDni());
            jsonObject.put("telefono",Users.getTelefono());
            jsonObject.put("password",Users.getPassword());
            jsonObject.put("email",Users.getCorreo());
            jsonObject.put("foto",Users.getPhoto());
            jsonObject.put("carrera",Users.getCarrera());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor JSON
                        try{
                            JSONObject jsonObject1=new JSONObject(response);

                            if(jsonObject1.length()>0){

                                Intent new_window=new Intent(getApplicationContext(), ActivityMenu.class);//new_window=nueva ventana
                                startActivity(new_window);
                            }else{
                                message("Alerta","Ya existe una cuenta!");
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                            Log.d("JSON", String.valueOf(e));
                            Toast.makeText(getApplicationContext(), "Error:"+e, Toast.LENGTH_LONG).show();
                        }
                    }

                    private void message(String alerta, String s) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar errores de la solicitud
            }
        })

        {
            @Override
            public byte[] getBody() {
                return jsonObject.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
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
                            JSONObject jsonArray=new JSONObject(response);

                            String[] careers=new String[jsonArray.length()];
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject career_object=jsonArray.getJSONObject(String.valueOf(i));//career_object=objeto carrera
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