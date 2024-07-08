package com.grupo8.proyecto.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Course;
import com.grupo8.proyecto.ui.activities.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MisCursosAdapter extends RecyclerView.Adapter<MisCursosAdapter.CursosViewHolder> {
    private Context context;
    private List<Course> cursosList;

    public MisCursosAdapter(Context context, List<Course> cursosList) {
        this.context = context;
        this.cursosList = cursosList;
    }

    @NonNull
    @Override
    public CursosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mis_cursos, parent, false);
        return new CursosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CursosViewHolder holder, int position) {
        Course curso = cursosList.get(position);

        holder.itemTitle.setText(curso.getTitle());
        // Aquí cargarías la imagen usando Picasso o Glide
        Picasso.get().load(curso.getUrlImage()).into(holder.itemImage);

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

    }

    @Override
    public int getItemCount() {
        return cursosList.size();
    }

    public static class CursosViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;

        public CursosViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
        }
    }
}//FIn clase
