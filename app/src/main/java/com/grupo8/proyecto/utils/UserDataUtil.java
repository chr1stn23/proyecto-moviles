// UserDataUtil.java
package com.grupo8.proyecto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.grupo8.proyecto.data.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDataUtil {

    public interface UserDataCallback {
        void onUserDataLoaded(User user);
        void onError(Exception e);
    }

    public static void fetchUserData(Context context, UserDataCallback callback) {
        SharedPreferences prefs = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            String url = "https://www.apirecursos.somee.com/api/v1/entities/GetUsers";
            RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                                        callback.onUserDataLoaded(user);
                                        break;
                                    }
                                }
                            } catch (JSONException e) {
                                callback.onError(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onError(error);
                        }
                    }
            );

            requestQueue.add(jsonArrayRequest);
        }
    }

    private static String convertDriveUrl(String driveUrl) {
        String fileId = driveUrl.split("/d/")[1].split("/")[0];
        return "https://drive.google.com/uc?export=view&id=" + fileId;
    }
}
