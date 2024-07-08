package com.grupo8.proyecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupo8.proyecto.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView durationTextView;
    private TextView dateTextView;
    private TextView dateEndTextView;
    private TextView locationTextView;
    private Button registerButton;
    private Button deleteButton;
    private int userId;
    private int courseId;
    private int tallerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            finish();
        }

        imageView = findViewById(R.id.detail_image);
        titleTextView = findViewById(R.id.detail_title);
        descriptionTextView = findViewById(R.id.detail_description);
        durationTextView = findViewById(R.id.detail_duration);
        dateTextView = findViewById(R.id.detail_date);
        dateEndTextView = findViewById(R.id.detail_date_end);
        locationTextView = findViewById(R.id.detail_location);
        registerButton = findViewById(R.id.detail_register_button);
        deleteButton = findViewById(R.id.detail_delete_button);

        deleteButton.setEnabled(false);
        deleteButton.setVisibility(View.INVISIBLE);

        if (getIntent().hasExtra("cursoId")) {
            courseId = getIntent().getIntExtra("cursoId", -1);
        } else if (getIntent().hasExtra("tallerId")) {
            tallerId = getIntent().getIntExtra("tallerId", -1);
        }


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String imageUrl = intent.getStringExtra("imageUrl");
        String description = intent.getStringExtra("description");
        int duration = intent.getIntExtra("duration", 0);
        String date = intent.getStringExtra("date");
        String dateEnd = intent.getStringExtra("dateEnd");
        String location = intent.getStringExtra("location");


        if(!Objects.equals(location, "")){
            //Mostrar datos del taller
            Picasso.get().load(imageUrl).into(imageView);
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            durationTextView.setText("Duración: " + duration + " horas");
            dateTextView.setText("Fecha: " + date);
            locationTextView.setText("Ubicación: " + location);

            dateEndTextView.setEnabled(false);

            //verificar que el taller este registrado
            checkIfTallerRegistered();
        }else if (!Objects.equals(dateEnd, "")){
            //Mostrar datos del curso
            Picasso.get().load(imageUrl).into(imageView);
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            durationTextView.setText("Duración: " + duration + " horas");
            dateTextView.setText("Fecha Inicio: " + date);
            dateEndTextView.setText("Fecha Final: " + dateEnd);

            locationTextView.setEnabled(false);

            //verificar que el curso este registrado
            checkIfCourseRegistered();
        }


    }

    private void checkIfCourseRegistered() {
        String url = "https://www.apirecursos.somee.com/api/v1/entities/RegistroCurso";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    boolean isRegistered = false;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject registro = response.getJSONObject(i);
                            JSONObject usuario = registro.getJSONObject("usuario");
                            JSONObject curso = registro.getJSONObject("curso");
                            if (usuario.getInt("id") == userId && curso.getInt("id") == courseId) {
                                isRegistered = true;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (isRegistered) {
                        registerButton.setText("Registrado");
                        registerButton.setOnClickListener(v ->
                                Toast.makeText(this, "El curso ya está registrado", Toast.LENGTH_SHORT).show());

                        deleteButton.setVisibility(View.VISIBLE);
                        deleteButton.setEnabled(true);
                    } else {
                        registerButton.setText("Registrar");
                        registerButton.setOnClickListener(v -> registerCourse());
                    }
                },
                error -> Toast.makeText(this, "Error al verificar registro", Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void checkIfTallerRegistered() {
        String url = "https://www.apirecursos.somee.com/api/v1/entities/RegistroTaller";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    boolean isRegistered = false;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject registro = response.getJSONObject(i);
                            JSONObject usuario = registro.getJSONObject("usuario");
                            JSONObject taller = registro.getJSONObject("taller");
                            if (usuario.getInt("id") == userId && taller.getInt("id") == tallerId) {
                                isRegistered = true;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (isRegistered) {
                        registerButton.setText("Registrado");
                        registerButton.setOnClickListener(v ->
                                Toast.makeText(this, "El taller ya está registrado", Toast.LENGTH_SHORT).show());

                        deleteButton.setVisibility(View.VISIBLE);
                        deleteButton.setEnabled(true);
                    } else {
                        registerButton.setText("Registrar");
                        registerButton.setOnClickListener(v -> registerTaller());
                    }
                },
                error -> Toast.makeText(this, "Error al verificar registro", Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void registerCourse() {
        String url = "https://www.apirecursos.somee.com/api/v1/privilegio/CreateRegistroCurso";
        JSONObject postData = new JSONObject();
        try {
            postData.put("usuarioId", userId);
            postData.put("cursoId", courseId);
            postData.put("estado", true);
            postData.put("roadmapId", 1);
            postData.put("fechaRegistro", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show(),
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        Toast.makeText(this, "Solicitud incorrecta", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void registerTaller() {
        String url = "https://www.apirecursos.somee.com/api/v1/privilegio/CreateRegistroTaller";
        JSONObject postData = new JSONObject();
        try {
            postData.put("usuarioId", userId);
            postData.put("tallerId", tallerId);
            postData.put("estado", true);
            postData.put("roadmapId", 1);
            postData.put("fechaRegistro", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show(),
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        Toast.makeText(this, "Solicitud incorrecta", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }



}//Fin clase
