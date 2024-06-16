package com.grupo8.proyecto.ui.fragments;

import static com.grupo8.proyecto.data.Contants.BASE_URL;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Blog;
import com.grupo8.proyecto.ui.adapters.BlogAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment implements BlogAdapter.OnItemClickListener {

    private List<Blog> blogList;
    private RecyclerView inicioRecyclerView;
    private BlogAdapter blogAdapter;

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);

        inicioRecyclerView = rootView.findViewById(R.id.homeRecyclerView);
        inicioRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        blogAdapter = new BlogAdapter(new ArrayList<>(), this);
        inicioRecyclerView.setAdapter(blogAdapter);
        loadBlogsFromApi();
        return rootView;
    }

    private void loadBlogsFromApi() {
        String url = BASE_URL + "GetBlogs";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Blog> blogs = new ArrayList<>();
                        try {
                            // Simulación de datos si no tienes un JSON real para probar
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Aquí construyes manualmente cada objeto Blog
                                int id = jsonObject.getInt("id");
                                String title = jsonObject.getString("titulo");
                                String content = jsonObject.getString("contenido");
                                String date = jsonObject.getString("fechaPublicacion");
                                String author = jsonObject.getString("autor");
                                String thumbnailUrl = jsonObject.getString("urlImagen");
                                int authorImage = R.drawable.profile;

                                // Crear instancia de Blog y agregarla a la lista
                                Blog blog = new Blog(id, title, content, date, author, thumbnailUrl, authorImage);
                                blogs.add(blog);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        blogList = blogs;
                        blogAdapter.setBlogs(blogList);
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

    private List<Blog> generateBlogItems() {
        List<Blog> blogItems = new ArrayList<>();
        blogItems.add(new Blog(1,"Introducción a Entity Framework","Entity Framework es un ORM para .NET...","2024-05-01T10:00:00","John Doe","https://edteam-media.s3.amazonaws.com/blogs/original/880ef35a-49cb-49ae-8469-b9809750718e.jpg",R.drawable.profile));
        blogItems.add(new Blog(2,"ASP.NET Core 101","ASP.NET Core es un framework web de código abierto...","2024-05-15T14:30:00","Jane Smith","https://miro.medium.com/v2/resize:fit:828/format:webp/1*Qy9HeVwrfnOTS3sptJ--hw.png",R.drawable.profile));
        return blogItems;
    }

    @Override
    public void onItemClick(int position) {
        Blog clickedBlog = blogList.get(position);
        Log.i(null, "Blog clicked: " + clickedBlog.getId());
    }
}