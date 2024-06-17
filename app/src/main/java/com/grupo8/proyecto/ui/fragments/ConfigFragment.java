package com.grupo8.proyecto.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.grupo8.proyecto.MainActivity;
import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.User;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigFragment extends Fragment {

    private RequestQueue requestQueue;
    private TextView userName, userEmail, userRegistrationDate, userType;
    private ImageView profileImage;
    private Button cerrarSesionButton;
    private View msgCerrarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        userRegistrationDate = view.findViewById(R.id.user_registration_date);
        userType = view.findViewById(R.id.user_type);
        profileImage = view.findViewById(R.id.profile_image);
        cerrarSesionButton = view.findViewById(R.id.btn_cerrar_sesion);

        requestQueue = Volley.newRequestQueue(requireContext());
        fetchUserData();

        cerrarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarConfirmacionCerrarSesion();
            }
        });

        return view;
    }

    private void fetchUserData() {
        String url = "https://www.apirecursos.somee.com/api/v1/entities/GetUsers";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject userObject = response.getJSONObject(0);

                            User user = new User(
                                    userObject.getInt("id"),
                                    userObject.getString("nombre"),
                                    userObject.getString("email"),
                                    userObject.getString("fechaRegistro"),
                                    userObject.getString("tipoUsuario"),
                                    convertDriveUrl(userObject.getString("fotoPerfil"))
                            );

                            updateUI(user);
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

    private String convertDriveUrl(String driveUrl) {
        // Extrae el ID del archivo de la URL original
        String fileId = driveUrl.split("/d/")[1].split("/")[0];
        // Crea una URL directa a la imagen
        return "https://drive.google.com/uc?export=view&id=" + fileId;
    }

    private void updateUI(User user) {
        userName.setText("Nombre: " + user.getNombre());
        userEmail.setText("Email: " + user.getEmail());
        userRegistrationDate.setText("Fecha de Registro: " + user.getFechaRegistro());
        userType.setText("Tipo de Usuario: " + user.getTipoUsuario());
        Picasso.get().load(user.getFotoPerfil()).into(profileImage);
    }

    private void mostrarConfirmacionCerrarSesion() {
        // Inflar la vista de confirmación msg_cerrar.xml
        if (msgCerrarView == null) {
            LayoutInflater inflater = LayoutInflater.from(requireContext());
            msgCerrarView = inflater.inflate(R.layout.msg_cerrar, (ViewGroup) requireView(), false);

            // Configurar botones de la vista de confirmación
            Button buttonYes = msgCerrarView.findViewById(R.id.button_yes);
            Button buttonCancel = msgCerrarView.findViewById(R.id.button_cancel);

            buttonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarSesion();
                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remover la vista de confirmación
                    ViewGroup parent = (ViewGroup) msgCerrarView.getParent();
                    if (parent != null) {
                        parent.removeView(msgCerrarView);
                    }
                    msgCerrarView = null;
                }
            });

            // Agregar la vista de confirmación al contenedor principal
            ViewGroup root = (ViewGroup) requireView();
            root.addView(msgCerrarView);
        }
    }

    private void cerrarSesion() {
        // Llamar al método de cerrar sesión en MainActivity
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.cerrarSesion();

        // Limpiar la vista de confirmación
        ViewGroup parent = (ViewGroup) msgCerrarView.getParent();
        if (parent != null) {
            parent.removeView(msgCerrarView);
        }
        msgCerrarView = null;
    }



}
