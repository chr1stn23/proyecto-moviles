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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private List<Long> talleresDatesList; // Lista para almacenar las fechas de los talleres
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

        // Inicializar la cola de solicitudes Volley
        requestQueue = Volley.newRequestQueue(requireContext());

        // Inicializar listas y adaptadores
        cursosList = new ArrayList<>();
        talleresList = new ArrayList<>();
        talleresDatesList = new ArrayList<>(); // Inicializar lista de fechas de talleres
        cursosAdapter = new MisCursosAdapter(requireContext(), cursosList);
        talleresAdapter = new MisTalleresAdapter(requireContext(), talleresList);

        // Cargar cursos y talleres
        loadCursos();
        loadTalleres();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_cursos, container, false);

        // Obtener referencias de vistas
        userImageView = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.txtNombre);
        notificationBell = view.findViewById(R.id.userNotificationBell);

        // Cargar datos del usuario y notificaciones
        loadUserData();
        setupNotificationClickListener();

        // Configurar RecyclerViews
        cursosRecyclerView = view.findViewById(R.id.cursosRecyclerView);
        talleresRecyclerView = view.findViewById(R.id.talleresRecyclerView);
        cursosRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        talleresRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cursosRecyclerView.setAdapter(cursosAdapter);
        talleresRecyclerView.setAdapter(talleresAdapter);

        // Configurar CalendarView y sus listeners
        setupCalendarView(view);

        return view;
    }

    // Método para cargar datos del usuario
    private void loadUserData() {
        UserDataUtil.fetchUserData(requireContext(), new UserDataUtil.UserDataCallback() {
            @Override
            public void onUserDataLoaded(User user) {
                // Actualizar nombre de usuario
                userName.setText("Hola, " + user.getNombre());
                // Cargar imagen de perfil
                Picasso.get().load(user.getFotoPerfil()).into(userImageView);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Método para configurar el click listener del icono de notificación
    private void setupNotificationClickListener() {
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

    // Método para configurar el CalendarView
    private void setupCalendarView(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        coursesTextView = view.findViewById(R.id.courses);
        workshopsTextView = view.findViewById(R.id.workshops);

        // Listener para el cambio de día seleccionado
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Crear un objeto Calendar con la fecha seleccionada
                Calendar selectedDateCalendar = Calendar.getInstance();
                selectedDateCalendar.set(year, month, dayOfMonth);
                long selectedDateMillis = selectedDateCalendar.getTimeInMillis();

                // Verificar si la fecha seleccionada está en la lista de fechas de talleres
                boolean isTallerDate = false;
                for (long tallerDateMillis : talleresDatesList) {
                    Calendar tallerCalendar = Calendar.getInstance();
                    tallerCalendar.setTimeInMillis(tallerDateMillis);

                    if (tallerCalendar.get(Calendar.YEAR) == year &&
                            tallerCalendar.get(Calendar.MONTH) == month &&
                            tallerCalendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                        isTallerDate = true;
                        break;
                    }
                }

                if (isTallerDate) {
                    // Cambiar el color del texto del CalendarView para el día seleccionado
                    workshopsTextView.setTextColor(Color.RED);

                    // Actualizar textos según la fecha seleccionada
                    coursesTextView.setText("Cursos para " + formatDate(dayOfMonth, month + 1, year));
                    workshopsTextView.setText("¡HOY SE REALIZA UN TALLER!");
                } else {
                    // Restaurar el color del texto si no es un día de taller
                    coursesTextView.setTextColor(Color.BLACK);
                    workshopsTextView.setTextColor(Color.BLACK);

                    // Actualizar textos según la fecha seleccionada
                    coursesTextView.setText("Cursos para " + formatDate(dayOfMonth, month + 1, year));
                    workshopsTextView.setText("Talleres para " + formatDate(dayOfMonth, month + 1, year));
                }
            }
        });
    }

    // Método para formatear la fecha
    private String formatDate(int day, int month, int year) {
        return day + "/" + month + "/" + year;
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
                        talleresDatesList.clear();
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

                                // Parsear y agregar la fecha del taller a la lista
                                String fechaTaller = tallerObject.getString("fecha");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = sdf.parse(fechaTaller);
                                talleresDatesList.add(date.getTime());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
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
