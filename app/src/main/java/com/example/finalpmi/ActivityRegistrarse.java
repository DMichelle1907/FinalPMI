package com.example.finalpmi;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalpmi.Data.Users;

public class ActivityRegistrarse extends AppCompatActivity {
    EditText edtNombres, edtApellidos, edtCorreo, edtTelefono, edtDni, edtFecha;
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
        edtFecha = (EditText) findViewById(R.id.ArFecha);
        Spinner = (Spinner) findViewById(R.id.spinner);
        btnRegistrarse = (Button) findViewById(R.id.btnARegistrarse);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(adapter);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los datos ingresados por el usuario
                String nombres = edtNombres.getText().toString();
                String apellidos = edtApellidos.getText().toString();
                String correo = edtCorreo.getText().toString();
                String telefono = edtTelefono.getText().toString();
                String dni = edtDni.getText().toString();
                String fecha = edtFecha.getText().toString();
                String carrera = Spinner.getSelectedItem().toString();

            }
        });


    }
}