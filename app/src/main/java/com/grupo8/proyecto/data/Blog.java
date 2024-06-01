package com.grupo8.proyecto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Blog {

    @EqualsAndHashCode.Include
    private int id;
    private String title;
    private String content;
    private String publicationDate;
    private String author;

}
