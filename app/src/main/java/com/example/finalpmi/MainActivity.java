package com.example.finalpmi;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalpmi.Data.ResApi;
import com.example.finalpmi.databinding.FragmentUsersBinding;
import com.example.finalpmi.ui.home.HomeViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FragmentUsersBinding binding;
    EditText edtCorreo, edtPassword;
    Button btnRegistrarse, btnIniciar;
    TextView nombrePerfil, correoPerfil, txtReenvio;
    FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfirestore = FirebaseFirestore.getInstance();
        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        txtReenvio = (TextView)findViewById(R.id.txtrecuperar);


        txtReenvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí inicias la ActivityReenvio
                Intent intent = new Intent(MainActivity.this, ActivityReenvio.class);
                startActivity(intent);
            }
        });



        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = edtCorreo.getText().toString();
                String password = edtPassword.getText().toString();

                Login(correo, password);
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ActivityRegistrarse.class);
                startActivity(intent);

            }
        });
    }

    private void Login(String correo, String password) {

        Message message = new Message();
        String url =  ResApi.url_server+ResApi.login;
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("JSON", String.valueOf(url));
        // Crear un objeto Usuario

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",correo);
            jsonObject.put("password",password);

            Log.d("Valores", String.valueOf(jsonObject));
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
                            Log.d("Respuesta", String.valueOf(jsonObject1));
                            if(jsonObject1.length()>0){

                                String correo = edtCorreo.getText().toString();
                                Intent new_window=new Intent(getApplicationContext(), ActivityMenu.class);//new_window=nueva ventana
                                new_window.putExtra("correo", correo);
                                startActivity(new_window);

                            }else{
                                message("Alerta","Numero de cuenta o usuario incorrecto");
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
    public void confirmar_usuario_firebase(){
        CollectionReference usuariosRef = mfirestore.collection("usuario");

// Define el correo y la contraseña ingresados por el usuario
        String correoIngresado = edtCorreo.getText().toString().trim();
        String pass_usuario = edtPassword.getText().toString().trim();

// Realiza la consulta en Firestore
        usuariosRef.whereEqualTo("correo", correoIngresado)
                .whereEqualTo("password", pass_usuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            // Usuario encontrado, las credenciales son correctas

                            Intent intent = new Intent(MainActivity.this, ActivityMenu.class);
                            startActivity(intent);
                        }
                    } else {
                        // Error al realizar la consulta
                        // Manejar el error según sea necesario
                    }
                });

    }
}