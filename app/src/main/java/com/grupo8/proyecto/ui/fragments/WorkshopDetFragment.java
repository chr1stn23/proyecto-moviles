package com.grupo8.proyecto.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupo8.proyecto.R;
import com.grupo8.proyecto.data.Workshop;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkshopDetFragment extends Fragment {

    private TextView title;
    private ImageView urlImage;
    private TextView duration;
    private TextView type;
    private TextView date;
    private TextView description;
    private TextView ubication;

    private Workshop workshop;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_workshop_det, container, false);
        title = rootView.findViewById(R.id.workshopTitle);
        urlImage = rootView.findViewById(R.id.workshopImage);
        duration = rootView.findViewById(R.id.workshopDuration);
        type = rootView.findViewById(R.id.workshopType);
        date = rootView.findViewById(R.id.workshopDate);
        description = rootView.findViewById(R.id.workshopContent);
        ubication = rootView.findViewById(R.id.workshopUbication);

        Bundle args = getArguments();
        if (args != null) {
            workshop = (Workshop) args.getSerializable("workshop");
            title.setText(workshop.getTitle());
            duration.setText(workshop.getDuration());
            type.setText(workshop.getType());
            date.setText(formateoFecha(workshop.getDate()));
            description.setText(workshop.getDescription());
            ubication.setText(workshop.getUbication());
        }

        return rootView;
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