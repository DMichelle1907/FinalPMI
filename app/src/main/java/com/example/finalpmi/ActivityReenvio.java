package com.example.finalpmi;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityReenvio extends AppCompatActivity {

    private Button btnEnviar, btnVolverEnviar;
    private CountDownTimer countDownTimer;
    private long tiempoRestanteEnMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reenvio);

    }
}
