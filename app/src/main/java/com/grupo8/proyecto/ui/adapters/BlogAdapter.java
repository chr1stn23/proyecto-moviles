package com.grupo8.proyecto.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Blog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private List<Blog> blogList;
    private OnItemClickListener onItemClickListener;

    public BlogAdapter(List<Blog> blogList, OnItemClickListener onItemClickListener) {
        this.blogList = blogList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false);
        return new BlogViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        holder.bind(blog);
    }

    @Override
    public int getItemCount() {
        return blogList != null ? blogList.size() : 0;
    }

    public void setBlogs(List<Blog> blogs){
        Log.d("BlogAdapter", "Setting new blogs: " + blogs.size());
        this.blogList = blogs;
        notifyDataSetChanged();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView blogThumbnail;
        private TextView blogTitle;
        private TextView authorName;
        private ImageView authorImage;
        private OnItemClickListener onItemClickListener;

        public BlogViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            blogThumbnail = itemView.findViewById(R.id.blogThumbnail);
            blogTitle = itemView.findViewById(R.id.blogTitle);
            authorName = itemView.findViewById(R.id.authorName);
            authorImage = itemView.findViewById(R.id.authorImage);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Blog blog) {
            String imageUrl = "https://drive.google.com/uc?export=view&id="+extractFileId(blog.getThumbnail());
            Picasso.get().load(imageUrl).into(blogThumbnail);
            blogTitle.setText(blog.getTitle());
            authorName.setText(blog.getAuthor());
            authorImage.setImageResource(blog.getAuthorImage());
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //Para extraer el id del link del DRIVE
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
}
