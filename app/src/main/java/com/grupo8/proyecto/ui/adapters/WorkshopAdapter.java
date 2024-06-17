package com.grupo8.proyecto.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Workshop;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.WorkshopViewHolder> {

    private List<Workshop> workshopList;
    private OnItemClickListener onItemClickListener;

    public WorkshopAdapter(List<Workshop> workshopList, OnItemClickListener onItemClickListener) {
        this.workshopList = workshopList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public WorkshopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workshop, parent, false);
        return new WorkshopAdapter.WorkshopViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopViewHolder holder, int position) {
        Workshop workshop = workshopList.get(position);
        holder.bind(workshop);
    }

    @Override
    public int getItemCount() {
        return workshopList != null ? workshopList.size() : 0;
    }

    public void setWorkshopList(List<Workshop> workshops) {

    }

    public static class WorkshopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView workshopThumbnail;
        private TextView workshopTitle;
        private TextView ubication;

        private OnItemClickListener onItemClickListener;

        public WorkshopViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            // workshopThumbnail = itemView.findViewById(R.id.workshopThumbnail);
            workshopTitle = itemView.findViewById(R.id.workshopTitle);
            ubication = itemView.findViewById(R.id.workshopUbication);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Workshop workshop) {
            // String imageUrl = "https://drive.google.com/uc?export=view&id="+extractFileId(blog.getThumbnail());
            // Picasso.get().load(imageUrl).into(blogThumbnail);
            workshopTitle.setText(workshop.getTitle());
            ubication.setText(workshop.getUbication());
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

}
