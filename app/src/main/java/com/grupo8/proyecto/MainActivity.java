package com.grupo8.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grupo8.proyecto.databinding.ActivityMainBinding;
import com.grupo8.proyecto.ui.fragments.*;
import com.grupo8.proyecto.ui.activities.*;

public class MainActivity extends AppCompatActivity {

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

}