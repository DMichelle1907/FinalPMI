package com.example.finalpmi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalpmi.Data.ResApi;
import com.example.finalpmi.Data.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityRegistrarse extends AppCompatActivity {
    EditText edtNombres, edtApellidos, edtCorreo, edtTelefono, edtDni, edtPassword;
    Button btnRegistrarse;
    Spinner Spinner;
    ImageView Img;
    String currentPhotoPath;
    static final int Peticion_ElegirGaleria = 103;

    static final int Peticion_AccesoCamara = 101;
    static final int Peticion_TomarFoto = 102;
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
        Img = (ImageView)findViewById(R.id.ImgPerfil);

        ListaCarreras(Spinner);
        Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogoOpciones();
            }
        });


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

    public void GuardarDatos() {
      //Aqui Guardar los datos
    }
    private void Permisos(){
        // Metodo para obtener los permisos requeridos de la aplicacion
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},Peticion_AccesoCamara);
        }
        else
        {
            dispatchTakePictureIntent();
            //TomarFoto();
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, Peticion_AccesoCamara);
        }
    }

    private void MostrarDialogoOpciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una opción");
        String[] opciones = {"Tomar foto", "Elegir de la galería"};
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Tomar foto
                   Permisos();
                } else {
                    // Abrir galería
                    AbrirGaleria();
                }
            }
        });
        builder.show();
    }
    private void AbrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Peticion_ElegirGaleria);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==  Peticion_AccesoCamara){
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }{
                Toast.makeText(getApplicationContext(),"Se necesita permiso de la camara",Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Peticion_TomarFoto) {
            try {
                File foto = new File(currentPhotoPath);
                Img.setImageURI(Uri.fromFile(foto));
            } catch (Exception ex) {
                ex.toString();
            }
        } else if (requestCode == Peticion_ElegirGaleria && resultCode == RESULT_OK) {
            // Elegir de la galería
            Uri selectedImage = data.getData();
            Img.setImageURI(selectedImage);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.toString();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.tarea23pmi.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Peticion_TomarFoto);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use  with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String convertImage64(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imagearray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagearray,Base64.DEFAULT);
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


    private void ListaCarreras(android.widget.Spinner spinner){//fill_career=llenar carreras
        Message messageListaCarrera = new Message();

        String url=ResApi.url_server+ResApi.select_careers;
        RequestQueue queue=Volley.newRequestQueue(this);//queue=cola
        Log.d("url", url);
        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONObject jsonArray=new JSONObject(response);
                            Log.d("json", String.valueOf(jsonArray));
                            String[] careers=new String[jsonArray.length()];
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject career_object=jsonArray.getJSONObject(String.valueOf(i));//career_object=objeto carrera
                                String id=career_object.getString("id");
                                String name=career_object.getString("carrera");
                                String career=id+"-"+name;
                                careers[i]=career;
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityRegistrarse.this, android.R.layout.simple_spinner_item, careers);
                            spinner.setAdapter(adapter);

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