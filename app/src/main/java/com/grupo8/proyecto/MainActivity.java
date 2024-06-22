package com.grupo8.proyecto;

import static androidx.core.graphics.drawable.DrawableCompat.applyTheme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grupo8.proyecto.databinding.ActivityMainBinding;
import com.grupo8.proyecto.ui.fragments.*;
import com.grupo8.proyecto.ui.activities.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupo8.proyecto.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;


public class  MainActivity extends  BaseActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!sesionActiva()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cambiarFragment(new InicioFragment());

        applyTheme();
        //requestQueue = Volley.newRequestQueue(this);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                cambiarFragment(new InicioFragment());
            } else if (item.getItemId() == R.id.cursos) {
                cambiarFragment(new CursosFragment());
            } else if (item.getItemId() == R.id.miscursos) {
                cambiarFragment(new MisCursosFragment());
            } else if (item.getItemId() == R.id.config) {
                cambiarFragment(new ConfigFragment());
            }
            return true;
        });



    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void cambiarFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private boolean sesionActiva(){
        SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        return prefs.getBoolean("sesionIniciada", false);
    }

    public void cerrarSesion() {
        SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("sesionIniciada", false);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();  // Cierra MainActivity y redirige al LoginActivity
    }



}