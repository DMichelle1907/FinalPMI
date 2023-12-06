package com.example.finalpmi;

import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalpmi.Data.ResApi;

import org.json.JSONException;
import org.json.JSONObject;


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

    }

}