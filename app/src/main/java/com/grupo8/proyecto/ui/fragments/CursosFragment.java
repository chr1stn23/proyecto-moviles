package com.grupo8.proyecto.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.grupo8.proyecto.ui.adapters.CursoTallerAdapter;
import com.grupo8.proyecto.data.Course;
import com.grupo8.proyecto.data.Taller;
import com.grupo8.proyecto.data.User;
import com.grupo8.proyecto.ui.adapters.*;
import com.grupo8.proyecto.utils.UserDataUtil;
import com.squareup.picasso.Picasso;

import com.grupo8.proyecto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CursosFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView userName;
    private ImageView userImageView, notificationBell;

    private RecyclerView recyclerView;
    private CursoTallerAdapter adapter;
    private List<Object> itemList;

    private SearchView searchView;
    private Button navCursos, navTalleres;

    public CursosFragment() {
        // Required empty public constructor
    }

    public static CursosFragment newInstance(String param1, String param2) {
        CursosFragment fragment = new CursosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = new ArrayList<>();
        adapter = new CursoTallerAdapter(itemList, getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cursos, container, false);

        notificationBell = view.findViewById(R.id.userNotificationBell);

        userName = view.findViewById(R.id.txtNombre);
        userImageView = view.findViewById(R.id.userImage);
        recyclerView = view.findViewById(R.id.homeRecyclerView);
        searchView = view.findViewById(R.id.busqueda);

        navCursos = view.findViewById(R.id.nav_cursos);
        navTalleres = view.findViewById(R.id.nav_talleres);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

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

        //funcionalidad barra busqueda


        // Configurar botones de navegación
        navCursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.clear();
                loadCourses();
            }
        });

        navTalleres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.clear();
                loadTalleres();
            }
        });

        //Cargar cursos por defecto
        loadCourses();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userImageView = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.txtNombre);
        notificationBell = view.findViewById(R.id.userNotificationBell);

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

    private void loadCourses() {
        String url = "https://www.apirecursos.somee.com/api/v1/entities/GetCurso";
        UserDataUtil.fetchDataFromApi(requireContext(), url, new UserDataUtil.ApiCallback() {
            @Override
            public void onSuccess(JSONArray response) {
                List<Course> newCourses = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject courseObject = response.getJSONObject(i);
                        Course curso = new Course(
                                courseObject.getInt("id"),
                                courseObject.getString("titulo"),
                                UserDataUtil.convertDriveUrl(courseObject.getString("urlImagen")),
                                courseObject.getInt("duracion"),
                                courseObject.getString("fechaInicio"),
                                courseObject.getString("fechaFin"),
                                courseObject.getString("descripcion")
                        );
                        newCourses.add(curso);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                itemList.addAll(newCourses);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadTalleres() {
        String url = "https://www.apirecursos.somee.com/api/v1/entities/GetTaller";
        UserDataUtil.fetchDataFromApi(requireContext(), url, new UserDataUtil.ApiCallback() {
            @Override
            public void onSuccess(JSONArray response) {
                List<Taller> newTalleres = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject tallerObject = response.getJSONObject(i);
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
                        newTalleres.add(taller);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                itemList.addAll(newTalleres);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }



}//Fin clase