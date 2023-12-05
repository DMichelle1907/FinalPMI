package com.example.finalpmi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);

                    Intent intent = new Intent(Inicio.this, MainActivity.class);
                    startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
