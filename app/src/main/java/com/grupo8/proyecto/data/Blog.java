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
public class Blog implements Serializable {

    @EqualsAndHashCode.Include
    private int id;
    private String title;
    private String content;
    private String publicationDate;
    private String author;
    private String thumbnail;
    private int authorImage;

    public Blog(int id, String title, String content, String publicationDate, String author, String thumbnail, int authorImage) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publicationDate = publicationDate;
        this.author = author;
        this.thumbnail = thumbnail;
        this.authorImage = authorImage;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(int authorImage) {
        this.authorImage = authorImage;
    }
}
