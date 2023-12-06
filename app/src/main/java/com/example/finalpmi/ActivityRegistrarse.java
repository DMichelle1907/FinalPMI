package com.example.finalpmi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ActivityRegistrarse extends AppCompatActivity {
    EditText edtNombres, edtApellidos, edtCorreo, edtTelefono, edtDni, edtPassword;
    Button btnRegistrarse;
    Spinner Spinner;
    ImageView Img;
    String currentPhotoPath;

    FirebaseFirestore mfirestore;
    StorageReference mstorage;
    Uri selectedImage;
    //String currentPhotoPath;
    File foto;
    String base64Image;
    static final int Peticion_ElegirGaleria = 103;

    static final int Peticion_AccesoCamara = 101;
    static final int Peticion_TomarFoto = 102;
    Integer carreraIds = null;
    List<String> nombresCarreras = new ArrayList<>();
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
            jsonObject.put("carrera",carreraIds);
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
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d("json", String.valueOf(jsonArray));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject careerObject = jsonArray.getJSONObject(i);
                                String id = careerObject.getString("id");
                                String name = careerObject.getString("carrera");

                                nombresCarreras.add(name);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityRegistrarse.this, android.R.layout.simple_spinner_item, nombresCarreras);
                            spinner.setAdapter(adapter);

                            // Aquí se define el OnItemSelectedListener
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    // Obtener el ID seleccionado según la posición del Spinner
                                    carreraIds = position;

                                    // Puedes usar el ID seleccionado como desees aquí
                                    Log.d("ID seleccionado", String.valueOf(carreraIds));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // Acción en caso de que no se seleccione nada
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                    }
                });

        queue.add(request);

    }
    private void messageErrorVolley(String error, String s, ActivityRegistrarse activityRegistrarse) {
    }

    private void message(String alerta, String correoYaEstaEnUso, ActivityRegistrarse activityRegistrarse) {
    }

    public void registrar_usuario_respaldo_firebase() {
        // Respaldo de datos para evitar errores
        String nombres = edtNombres.getText().toString();
        String apellidos = edtApellidos.getText().toString();
        String correo = edtCorreo.getText().toString();
        String telefono = edtTelefono.getText().toString();
        String dni = edtDni.getText().toString();
        String password = edtPassword.getText().toString();
        // long carrera = Spinner.getSelectedItemId();

        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || telefono.isEmpty() || dni.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Favor, revisar que todos los campos estén llenos", Toast.LENGTH_SHORT).show();

        } else {
            try {
                postUser(nombres, apellidos, correo, telefono, dni, password);
            } catch (Exception e) {
                e.printStackTrace(); // o maneja la excepción de alguna manera
                Toast.makeText(getApplicationContext(), "Error al registrar usuario", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void postUser(String nombres, String apellidos, String correo, String telefono, String dni, String password) {
        // Validación del formato del correo electrónico
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(getApplicationContext(), "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return; // salir del método si el correo electrónico no es válido
        }

        // Mapa con datos del usuario
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombres);
        map.put("apellido", apellidos);
        map.put("correo", correo);
        map.put("telefono", telefono);
        map.put("dni", dni);
        map.put("password", password);

        // Agregar usuario a la colección en Firebase
        mfirestore.collection("usuario")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getApplicationContext(), "Registro completo", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(ActivityRegistrarse.this, ActivityReenvio.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Registro no ingresado", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void subirImagenAFirebaseStorage() {
        StorageReference filePath = mstorage.child("perfiles").child(selectedImage.getLastPathSegment());

        filePath.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Operación exitosa
                        Toast.makeText(getApplicationContext(), "Registro completo", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar el error aquí
                        Toast.makeText(getApplicationContext(), "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        // Puedes usar taskSnapshot para obtener información sobre el progreso
                    }
                });
    }

    public void subirImagenAFirebase_galeria() {
        // Crear una referencia al lugar donde se almacenará la imagen en Firebase Storage
        StorageReference filePath = mstorage.child("perfiles").child(selectedImage.getLastPathSegment());

        // Subir la imagen
        filePath.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // La imagen se subió exitosamente
                        Toast.makeText(ActivityRegistrarse.this, "Se subió exitosamente la foto.", Toast.LENGTH_SHORT).show();
                        obtenerImagenBase64();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar el caso en que la subida falle
                        Toast.makeText(ActivityRegistrarse.this, "Error al subir la foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void obtenerImagenBase64() {
        StorageReference filePath = mstorage.child("perfiles").child("101998");

        filePath.getDownloadUrl().addOnSuccessListener(uri -> {
            // Obtén la URL de descarga de la imagen
            String downloadUrl = uri.toString();

            // Inicia una tarea asíncrona para descargar y convertir la imagen a base64
            new DownloadImageTask().execute(downloadUrl);
        }).addOnFailureListener(e -> {
            // Manejar el caso en que la obtención de la URL falle
            Toast.makeText(ActivityRegistrarse.this, "Error al obtener la URL de la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            try {
                // Descargar la imagen desde la URL
                InputStream inputStream = new java.net.URL(url).openStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                // Convertir la imagen a bytes
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = ((InputStream) inputStream).read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                // Convertir la imagen a base64
                return Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String base64Image) {
            // Manejar la cadena de base64 de la imagen
            if (base64Image != null) {
                // Hacer algo con la cadena de base64, por ejemplo, mostrarla en un TextView
                // textView.setText(base64Image);
                Toast.makeText(ActivityRegistrarse.this, "Imagen en Base64 obtenida exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityRegistrarse.this, "Error al obtener la imagen en Base64", Toast.LENGTH_SHORT).show();
            }
        }
    }
}