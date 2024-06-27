package com.grupo8.proyecto.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupo8.proyecto.data.User;
import com.grupo8.proyecto.utils.UserDataUtil;
import com.squareup.picasso.Picasso;

import com.grupo8.proyecto.R;

import java.util.Calendar;

public class CursosFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView userName;
    private ImageView userImageView;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cursos, container, false);

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userImageView = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.txtNombre);

    }


}//Fin clase