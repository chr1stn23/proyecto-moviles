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
import com.grupo8.proyecto.data.Taller;
import com.grupo8.proyecto.ui.activities.DetailActivity;
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
        Picasso.get().load(taller.getUrlImage()).into(holder.itemImage);

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
