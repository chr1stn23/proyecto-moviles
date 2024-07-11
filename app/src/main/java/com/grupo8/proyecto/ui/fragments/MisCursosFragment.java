package com.grupo8.proyecto.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Course;
import com.grupo8.proyecto.data.Taller;
import com.grupo8.proyecto.data.User;
import com.grupo8.proyecto.ui.adapters.CursoTallerAdapter;
import com.grupo8.proyecto.ui.adapters.MisCursosAdapter;
import com.grupo8.proyecto.ui.adapters.MisTalleresAdapter;
import com.grupo8.proyecto.utils.UserDataUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MisCursosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue requestQueue;
    private CalendarView calendarView;
    private TextView coursesTextView, userName;
    private TextView workshopsTextView;
    private ImageView userImageView, notificationBell;
    private List<Course> cursosList;
    private List<Taller> talleresList;
    private RecyclerView cursosRecyclerView;
    private RecyclerView talleresRecyclerView;
    private MisCursosAdapter cursosAdapter;
    private MisTalleresAdapter talleresAdapter;
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

        userImageView = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.txtNombre);
        notificationBell = view.findViewById(R.id.userNotificationBell);

        //Manejar datos del header
        UserDataUtil.fetchUserData(requireContext(), new UserDataUtil.UserDataCallback() {
            @Override
            public void onUserDataLoaded(User user) {
                // Actualizar nombre de usuario
                userName.setText("Hola, " + user.getNombre());
                //Cargar imagen de perfil
                Picasso.get().load(user.getFotoPerfil()).into(userImageView);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        notificationBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDataUtil.fetchNotifications(requireContext(), new UserDataUtil.NotificationsCallback() {
                    @Override
                    public void onNotificationsLoaded(JSONArray notifications) {
                        showNotifications(notifications);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        // Inicialización de RecyclerViews y adaptadores
        cursosList = new ArrayList<>();
        talleresList = new ArrayList<>();
        cursosRecyclerView = view.findViewById(R.id.cursosRecyclerView);
        talleresRecyclerView = view.findViewById(R.id.talleresRecyclerView);
        cursosAdapter = new MisCursosAdapter(requireContext(), cursosList);
        talleresAdapter = new MisTalleresAdapter(requireContext(), talleresList);

        cursosRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        talleresRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cursosRecyclerView.setAdapter(cursosAdapter);
        talleresRecyclerView.setAdapter(talleresAdapter);

        // Cargar cursos y talleres
        loadCursos();
        loadTalleres();

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

    private void showNotifications(JSONArray notifications) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Notificaciones");

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < notifications.length(); i++) {
            try {
                JSONObject notification = notifications.getJSONObject(i);
                message.append(notification.getString("mensaje")).append("\n\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        builder.setMessage(message.toString());

        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadCursos() {
        int userId = getUserIdFromSharedPreferences(); // Obtener el ID de usuario guardado
        String url = "https://www.apirecursos.somee.com/api/v1/entities/GetCursosbyUser?idusuario=" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        cursosList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONObject cursoObject = jsonObject.getJSONObject("curso");
                                Course curso = new Course(
                                        cursoObject.getInt("id"),
                                        cursoObject.getString("titulo"),
                                        UserDataUtil.convertDriveUrl(cursoObject.getString("urlImagen")),
                                        cursoObject.getInt("duracion"),
                                        cursoObject.getString("fechaInicio"),
                                        cursoObject.getString("fechaFin"),
                                        cursoObject.getString("descripcion")
                                );
                                cursosList.add(curso);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        cursosAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    private void loadTalleres() {
        int userId = getUserIdFromSharedPreferences(); // Obtener el ID de usuario guardado
        String url = "https://www.apirecursos.somee.com/api/v1/entities/GetTallerbyUser?idusuario=" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        talleresList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONObject tallerObject = jsonObject.getJSONObject("taller");
                                Taller taller = new Taller(
                                        tallerObject.getInt("id"),
                                        tallerObject.getString("titulo"),
                                        UserDataUtil.convertDriveUrl(tallerObject.getString("urlImagen")),
                                        tallerObject.getInt("duracion"),
                                        tallerObject.getInt("tipo"),
                                        tallerObject.getString("ubicacion"),
                                        tallerObject.getString("fecha"),
                                        tallerObject.getString("descripcion")
                                );
                                talleresList.add(taller);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        talleresAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    // Método para obtener el ID de usuario guardado en SharedPreferences
    private int getUserIdFromSharedPreferences() {
        SharedPreferences prefs = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        return prefs.getInt("userId", -1); // -1 como valor por defecto si no se encuentra
    }


}//Fin clase
