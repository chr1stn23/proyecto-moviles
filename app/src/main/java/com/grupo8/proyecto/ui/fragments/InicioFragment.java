package com.grupo8.proyecto.ui.fragments;

import static com.grupo8.proyecto.data.Contants.BASE_URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Blog;
import com.grupo8.proyecto.data.User;
import com.grupo8.proyecto.ui.adapters.BlogAdapter;
import com.grupo8.proyecto.utils.UserDataUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment implements BlogAdapter.OnItemClickListener {

    private List<Blog> blogList;
    private List<Blog> filteredBlogList;
    private RecyclerView inicioRecyclerView;
    private BlogAdapter blogAdapter;
    private SearchView searchView;
    private ImageView userImageView, notificationBell;
    private TextView userName;

    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);

        userImageView = rootView.findViewById(R.id.userImage);
        userName = rootView.findViewById(R.id.txtNombre);
        notificationBell = rootView.findViewById(R.id.userNotificationBell);

        inicioRecyclerView = rootView.findViewById(R.id.homeRecyclerView);
        inicioRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        blogList = new ArrayList<>();
        filteredBlogList = new ArrayList<>();
        blogAdapter = new BlogAdapter(filteredBlogList, this);
        inicioRecyclerView.setAdapter(blogAdapter);
        // Datos del Header
        userImageView = rootView.findViewById(R.id.userImage);
        userName = rootView.findViewById(R.id.txtNombre);;

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


        searchView = rootView.findViewById(R.id.busqueda);

        // Configurar el listener para el SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBlogs(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBlogs(newText);
                return false;
            }
        });

        loadBlogsFromApi();
        return rootView;
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

    private void loadBlogsFromApi() {
        String url = BASE_URL + "GetBlogs";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Blog> blogs = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                int id = jsonObject.getInt("id");
                                String title = jsonObject.getString("titulo");
                                String content = jsonObject.getString("contenido");
                                String date = jsonObject.getString("fechaPublicacion");
                                String author = jsonObject.getString("autor");
                                String thumbnailUrl = jsonObject.getString("urlImagen");
                                int authorImage = R.drawable.profile;

                                Blog blog = new Blog(id, title, content, date, author, thumbnailUrl, authorImage);
                                blogs.add(blog);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        blogList = blogs;
                        filteredBlogList.clear();
                        filteredBlogList.addAll(blogList);
                        blogAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(requireContext(), "Error al cargar los blogs", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonArrayRequest);
    }

    //Filtro por titulo y autor
    private void filterBlogs(String query) {
        List<Blog> filteredList = new ArrayList<>();
        for (Blog blog : blogList) {
            if (blog.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    blog.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(blog);
            }
        }

        filteredBlogList.clear();

        filteredBlogList.addAll(filteredList);
        blogAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Blog clickedBlog = filteredBlogList.get(position);
        Log.i(null, "Blog clicked: " + clickedBlog.getContent());
        Bundle bundle = new Bundle();
        bundle.putSerializable("blog", clickedBlog);
        BlogDetFragment blogDetFragment = new BlogDetFragment();
        blogDetFragment.setArguments(bundle);
        cambiarFragment(blogDetFragment);
    }

    private void cambiarFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null); //Para poder volver atrás
        fragmentTransaction.commit();
    }
}
