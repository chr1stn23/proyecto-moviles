package com.grupo8.proyecto.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Blog;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BlogDetFragment extends Fragment {

    private TextView blogTitle;
    private TextView blogAuthor;
    private TextView blogDate;
    private TextView blogContent;
    private ImageView blogImage;
    private Blog blog;
    public BlogDetFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blog_det, container, false);
        blogTitle = rootView.findViewById(R.id.blogTitle);
        blogAuthor = rootView.findViewById(R.id.blogAuthor);
        blogDate = rootView.findViewById(R.id.blogDate);
        blogContent = rootView.findViewById(R.id.blogContent);
        blogImage = rootView.findViewById(R.id.blogImage);

        Bundle args = getArguments();
        if (args != null) {
            blog = (Blog) args.getSerializable("blog");
            blogTitle.setText(blog.getTitle());
            blogAuthor.setText("Por: " + blog.getAuthor());
            blogDate.setText("Publicado: "+ formateoFecha(blog.getPublicationDate()));
            blogContent.setText(blog.getContent());
            String imageUrl = "https://drive.google.com/uc?export=view&id="+extractFileId(blog.getThumbnail());
            Picasso.get().load(imageUrl).into(blogImage);
        }

        return rootView;
    }

    private static String extractFileId(String originalLink) {
        String fileId = null;
        // Verificar que el enlace sea válido
        if (originalLink != null && !originalLink.isEmpty()) {
            // Buscar el patrón "/file/d/" en el enlace
            int startIndex = originalLink.indexOf("/file/d/");
            if (startIndex != -1) {
                // Obtener el ID del archivo
                startIndex += "/file/d/".length();
                int endIndex = originalLink.indexOf("/view", startIndex);
                if (endIndex != -1) {
                    fileId = originalLink.substring(startIndex, endIndex);
                }
            }
        }
        return fileId;
    }

    private String formateoFecha(String fecha) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        try {
            Date date = inputFormat.parse(fecha);
            return outputFormat.format(date);
        } catch (Exception e){
            return fecha;
        }
    }
}