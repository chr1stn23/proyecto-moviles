package com.grupo8.proyecto.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Course;
import com.grupo8.proyecto.data.Taller;
import com.grupo8.proyecto.ui.activities.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CursoTallerAdapter extends RecyclerView.Adapter<CursoTallerAdapter.ViewHolder> implements Filterable {

    private List<Object> itemList;
    private List<Object> itemListFull;
    private Context context;

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
                intent.putExtra("title", curso.getTitle());
                intent.putExtra("imageUrl", curso.getUrlImage());
                intent.putExtra("description", curso.getDescription());
                intent.putExtra("duration", curso.getDuration());
                intent.putExtra("date", curso.getStartDate());
                intent.putExtra("dateEnd", curso.getEndDate());
                intent.putExtra("location", ""); // Los cursos no tienen ubicación
                context.startActivity(intent);
            });
        } else if (itemList.get(position) instanceof Taller) {
            Taller taller = (Taller) itemList.get(position);
            holder.title.setText(taller.getTitle());
            holder.description.setText(taller.getDescription());
            Picasso.get().load(taller.getUrlImage()).into(holder.imageView);

            //Manejar clic en cada item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("title", taller.getTitle());
                intent.putExtra("imageUrl", taller.getUrlImage());
                intent.putExtra("description", taller.getDescription());
                intent.putExtra("duration", taller.getDuration());
                intent.putExtra("date", taller.getDate());
                intent.putExtra("dateEnd", "");//los talleres son de fecha unica
                intent.putExtra("location", taller.getUbication());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            imageView = itemView.findViewById(R.id.item_image);
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
