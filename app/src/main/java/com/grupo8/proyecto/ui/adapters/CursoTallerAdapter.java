package com.grupo8.proyecto.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Course;
import com.grupo8.proyecto.data.Taller;
import com.grupo8.proyecto.ui.activities.DetailActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CursoTallerAdapter extends RecyclerView.Adapter<CursoTallerAdapter.ViewHolder> implements Filterable {

    private List<Object> itemList;
    private List<Object> itemListFull;
    private Context context;
    private int userId;

    public CursoTallerAdapter(List<Object> itemList, Context context) {
        this.itemList = itemList;
        this.itemListFull = new ArrayList<>(itemList);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof Course) {
            return 0;
        } else if (itemList.get(position) instanceof Taller) {
            return 1;
        }
        return -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_taller, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (itemList.get(position) instanceof Course) {
            Course curso = (Course) itemList.get(position);
            holder.title.setText(curso.getTitle());
            holder.description.setText(curso.getDescription());
            Picasso.get().load(curso.getUrlImage()).into(holder.imageView);

            //Manejar clic en cada item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("cursoId", curso.getId());
                intent.putExtra("title", curso.getTitle());
                intent.putExtra("imageUrl", curso.getUrlImage());
                intent.putExtra("description", curso.getDescription());
                intent.putExtra("duration", curso.getDuration());
                intent.putExtra("date", curso.getStartDate());
                intent.putExtra("dateEnd", curso.getEndDate());
                intent.putExtra("location", ""); // Los cursos no tienen ubicación
                context.startActivity(intent);
            });

            // Verificar si el curso ya está registrado
            checkIfCourseRegistered(holder, curso.getId());

        } else if (itemList.get(position) instanceof Taller) {
            Taller taller = (Taller) itemList.get(position);
            holder.title.setText(taller.getTitle());
            holder.description.setText(taller.getDescription());
            Picasso.get().load(taller.getUrlImage()).into(holder.imageView);

            //Manejar clic en cada item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("tallerId", taller.getId());
                intent.putExtra("title", taller.getTitle());
                intent.putExtra("imageUrl", taller.getUrlImage());
                intent.putExtra("description", taller.getDescription());
                intent.putExtra("duration", taller.getDuration());
                intent.putExtra("date", taller.getDate());
                intent.putExtra("dateEnd", "");//los talleres son de fecha unica
                intent.putExtra("location", taller.getUbication());
                context.startActivity(intent);
            });

            // Verificar si el taller ya está registrado
            checkIfTallerRegistered(holder, taller.getId());
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void checkIfCourseRegistered(ViewHolder holder, int courseId) {
        SharedPreferences prefs = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(context, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

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
                        holder.registerButton.setText("Registrado");
                        holder.registerButton.setOnClickListener(v ->
                                Toast.makeText(context, "El curso ya está registrado", Toast.LENGTH_SHORT).show());
                    } else {
                        holder.registerButton.setText("Registrar");
                        holder.registerButton.setOnClickListener(v -> registerCourse(courseId));
                    }
                },
                error -> Toast.makeText(context, "Error al verificar registro", Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    private void checkIfTallerRegistered(ViewHolder holder, int tallerId) {
        SharedPreferences prefs = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(context, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

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
                        holder.registerButton.setText("Registrado");
                        holder.registerButton.setOnClickListener(v ->
                                Toast.makeText(context, "El taller ya está registrado", Toast.LENGTH_SHORT).show());
                    } else {
                        holder.registerButton.setText("Registrar");
                        holder.registerButton.setOnClickListener(v -> registerTaller(tallerId));
                    }
                },
                error -> Toast.makeText(context, "Error al verificar registro", Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    private void registerCourse(int courseId) {
        SharedPreferences prefs = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(context, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

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

        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> Toast.makeText(context, "Curso registrado con éxito", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(context, "Error al registrar el curso", Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);*/
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show(),
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        Toast.makeText(context, "Solicitud incorrecta", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    private void registerTaller(int tallerId) {
        SharedPreferences prefs = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(context, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

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

        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> Toast.makeText(context, "Taller registrado con éxito", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(context, "Error al registrar el taller", Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);*/
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show(),
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        Toast.makeText(context, "Solicitud incorrecta", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView imageView;
        public Button registerButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            imageView = itemView.findViewById(R.id.item_image);
            registerButton = itemView.findViewById(R.id.btn_register);
        }
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Object> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull); // Si no hay filtro, mostrar la lista completa
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim(); // Convertir a minúsculas y eliminar espacios en blanco

                for (Object item : itemListFull) {
                    if (item instanceof Course) {
                        Course curso = (Course) item;
                        if (curso.getTitle().toLowerCase().contains(filterPattern) ||
                                curso.getDescription().toLowerCase().contains(filterPattern)) {
                            filteredList.add(curso);
                        }
                    } else if (item instanceof Taller) {
                        Taller taller = (Taller) item;
                        if (taller.getTitle().toLowerCase().contains(filterPattern) ||
                                taller.getDescription().toLowerCase().contains(filterPattern)) {
                            filteredList.add(taller);
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



}//Fin clase
