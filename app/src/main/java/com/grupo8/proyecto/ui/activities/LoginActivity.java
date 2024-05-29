package com.grupo8.proyecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.grupo8.proyecto.MainActivity;
import com.grupo8.proyecto.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText txtUser, txtPassword;
    private TextView txtRegistrar;
    private Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        enlazarElementos();

        btnContinuar.setOnClickListener(this);
        txtRegistrar.setOnClickListener(this);
    }

    private void enlazarElementos(){
        txtUser = findViewById(R.id.txtUser);
        txtPassword = findViewById(R.id.txtPassword);
        btnContinuar = findViewById(R.id.btnContinuar);
        txtRegistrar = findViewById(R.id.txtRegistrar);
    }

    private boolean verificarUsuario(String user, String password){
        if (user.equals("admin") && password.equals("admin")){
            SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("sesionIniciada", true);
            editor.apply();
            return true;
        }
        return false;
    }

    public void NavigateToRegistrer(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == btnContinuar){
            String user = txtUser.getText().toString();
            String password = txtPassword.getText().toString();
            if(verificarUsuario(user, password)){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        if (v == txtRegistrar){
            NavigateToRegistrer();
        }
    }
}