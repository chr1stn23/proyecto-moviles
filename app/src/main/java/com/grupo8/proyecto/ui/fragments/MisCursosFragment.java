package com.grupo8.proyecto.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MisCursosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue requestQueue;

    private CalendarView calendarView;
    private TextView coursesTextView, userName;
    private TextView workshopsTextView;
    private ImageView userImageView;

    public MisCursosFragment() {
        // Required empty public constructor
    }

    public static MisCursosFragment newInstance(String param1, String param2) {
        MisCursosFragment fragment = new MisCursosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_cursos, container, false);

        requestQueue = Volley.newRequestQueue(requireContext());
        fetchUserData();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        coursesTextView = view.findViewById(R.id.courses);
        workshopsTextView = view.findViewById(R.id.workshops);
        userImageView = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.txtNombre);



        long today = Calendar.getInstance().getTimeInMillis();
        calendarView.setMinDate(today);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Highlight the selected date
                Calendar selectedDateCalendar = Calendar.getInstance();
                selectedDateCalendar.set(year, month, dayOfMonth);
                calendarView.setDate(selectedDateCalendar.getTimeInMillis());


                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                coursesTextView.setText("Cursos para " + selectedDate);
                workshopsTextView.setText("Talleres para " + selectedDate);
            }
        });
    }

    private void fetchUserData() {
        SharedPreferences prefs = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            String url = "https://www.apirecursos.somee.com/api/v1/entities/GetUsers";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject userObject = response.getJSONObject(i);
                                    if (userObject.getInt("id") == userId) {
                                        User user = new User(
                                                userObject.getInt("id"),
                                                userObject.getString("nombre"),
                                                userObject.getString("email"),
                                                userObject.getString("fechaRegistro"),
                                                userObject.getString("tipoUsuario"),
                                                convertDriveUrl(userObject.getString("fotoPerfil"))
                                        );

                                        updateUI(user);
                                        break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }
            );

            requestQueue.add(jsonArrayRequest);
        }
    }

    private String convertDriveUrl(String driveUrl) {
        // Extrae el ID del archivo de la URL original
        String fileId = driveUrl.split("/d/")[1].split("/")[0];
        // Crea una URL directa a la imagen
        return "https://drive.google.com/uc?export=view&id=" + fileId;
    }

    private void updateUI(User user) {
        userName.setText("Hola, " + user.getNombre());
        Picasso.get().load(user.getFotoPerfil()).into(userImageView);
    }


}
