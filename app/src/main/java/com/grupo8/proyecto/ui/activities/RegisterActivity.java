package com.grupo8.proyecto.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.grupo8.proyecto.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegistrar, btnVolver;
    private TextInputEditText txtNombre, txtApellido, txtUsuario, txtPassword, txtEmail, txtTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        enlazarElementos();

        btnRegistrar.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
    }

    private void enlazarElementos() {
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefono = findViewById(R.id.txtTelefono);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegistrar) {

        }

        if (v == btnVolver) {
            finish();
        }
    }
}