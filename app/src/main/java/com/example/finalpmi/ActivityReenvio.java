package com.example.finalpmi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalpmi.Data.ResApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ActivityReenvio extends AppCompatActivity {

    private EditText edtCodigo, editTextNuevaContraseña;
    private TextView txtCorreoDeRee;
    private Button sendCodeButton, actualizarContraseñaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reenvio);

        edtCodigo = findViewById(R.id.edtCodigo);
        editTextNuevaContraseña = findViewById(R.id.editTextText2);
        txtCorreoDeRee = findViewById(R.id.txtCorreoDeRee);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        actualizarContraseñaButton = findViewById(R.id.button);

        String correoUsuario = getIntent().getStringExtra("correo");
        txtCorreoDeRee.setText(correoUsuario);
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        actualizarContraseñaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo=edtCodigo.getText().toString();
                String nuevaCont=editTextNuevaContraseña.getText().toString();
                String correo=txtCorreoDeRee.getText().toString();

                recuperar(correo, nuevaCont, codigo);
            }
        });

    }

    private void recuperar(String correo, String password, String codigo) {

        Message message = new Message();
        String url =  ResApi.url_server+ResApi.update_password;
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("JSON", String.valueOf(url));
        // Crear un objeto Usuario

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",correo);
            jsonObject.put("recoveryCode",codigo);
            jsonObject.put("newPassword",password);
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

                                Intent new_window=new Intent(getApplicationContext(), MainActivity.class);//new_window=nueva ventana
                                startActivity(new_window);

                            }else{
                                message("Alerta","Faltan datos");
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

}