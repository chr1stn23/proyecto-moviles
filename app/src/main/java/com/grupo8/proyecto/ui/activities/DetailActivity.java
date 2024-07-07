package com.grupo8.proyecto.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.grupo8.proyecto.R;
import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.detail_image);
        titleTextView = findViewById(R.id.detail_title);
        descriptionTextView = findViewById(R.id.detail_description);
        durationTextView = findViewById(R.id.detail_duration);
        dateTextView = findViewById(R.id.detail_date);
        dateEndTextView = findViewById(R.id.detail_date_end);
        locationTextView = findViewById(R.id.detail_location);
        registerButton = findViewById(R.id.detail_register_button);


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String imageUrl = intent.getStringExtra("imageUrl");
        String description = intent.getStringExtra("description");
        int duration = intent.getIntExtra("duration", 0);
        String date = intent.getStringExtra("date");
        String dateEnd = intent.getStringExtra("dateEnd");
        String location = intent.getStringExtra("location");


        /*Picasso.get().load(imageUrl).into(imageView);
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        durationTextView.setText("Duración: " + duration + " horas");
        dateTextView.setText("Fecha: " + date);
        locationTextView.setText("Ubicación: " + location);*/

        if(!Objects.equals(location, "")){
            //Mostrar datos del taller
            Picasso.get().load(imageUrl).into(imageView);
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            durationTextView.setText("Duración: " + duration + " horas");
            dateTextView.setText("Fecha: " + date);
            locationTextView.setText("Ubicación: " + location);

            dateEndTextView.setEnabled(false);
        }else if (!Objects.equals(dateEnd, "")){
            //Mostrar datos del curso
            Picasso.get().load(imageUrl).into(imageView);
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            durationTextView.setText("Duración: " + duration + " horas");
            dateTextView.setText("Fecha Inicio: " + date);
            dateEndTextView.setText("Fecha Final: " + dateEnd);

            locationTextView.setEnabled(false);
        }

        registerButton.setOnClickListener(v -> {
            // Lógica de registro
        });
    }
}//Fin clase
