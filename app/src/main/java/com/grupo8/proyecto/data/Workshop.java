package com.grupo8.proyecto.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Workshop implements Serializable {

    @EqualsAndHashCode.Include
    private int id;
    private String title;
    private String urlImage;
    private int duration;
    private int type;
    private String date;
    private String description;
    private String ubication;

    public Workshop(int id, String title, String urlImage, int duration, int type, String date, String description, String ubication) {
        this.id = id;
        this.title = title;
        this.urlImage = urlImage;
        this.duration = duration;
        this.type = type;
        this.date = date;
        this.description = description;
        this.ubication = ubication;
    }

    public String getUbication() {
        return ubication;
    }

    public void setUbication(String ubication) {
        this.ubication = ubication;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
