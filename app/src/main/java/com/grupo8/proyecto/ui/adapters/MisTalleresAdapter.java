package com.grupo8.proyecto.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Taller;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MisTalleresAdapter extends RecyclerView.Adapter<MisTalleresAdapter.TalleresViewHolder> {
    private Context context;
    private List<Taller> talleresList;

    public MisTalleresAdapter(Context context, List<Taller> talleresList) {
        this.context = context;
        this.talleresList = talleresList;
    }

    @NonNull
    @Override
    public TalleresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mis_talleres, parent, false);
        return new TalleresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TalleresViewHolder holder, int position) {
        Taller taller = talleresList.get(position);

        holder.itemTitle.setText(taller.getTitle());
        // Aquí cargarías la imagen usando Picasso o Glide
        Picasso.get().load(taller.getUrlImage()).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return talleresList.size();
    }

    public static class TalleresViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;

        public TalleresViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
        }
    }
}//Fin clase
