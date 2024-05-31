package com.grupo8.proyecto.ui.activities;

import static com.grupo8.proyecto.data.Contants.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.grupo8.proyecto.MainActivity;
import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

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

    private void NavigateToMain(String user, String password){
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL + "Login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                User u = gson.fromJson(response, User.class);
                if (u != null) {
                    SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("sesionIniciada", true);
                    editor.apply();
                    Log.i(null, "Usuario encontrado");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                String err = new String(error.networkResponse.data);
                Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() {
                JSONObject body = new JSONObject();
                try{
                    body.put("username", user);
                    body.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                return body.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void NavigateToRegistrer(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public boolean validarCampos(String user,String password){
        return !user.isEmpty() && !password.isEmpty();
    }

    @Override
    public void onClick(View v) {
        if (v == btnContinuar){
            String user = txtUser.getText().toString();
            String password = txtPassword.getText().toString();
            if (!validarCampos(user,password)){
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            }
            NavigateToMain(user, password);
        }

        if (v == txtRegistrar){
            NavigateToRegistrer();
        }
    }
}