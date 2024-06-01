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
public class Course {

    @EqualsAndHashCode.Include
    private int id;
    private String title;
    private int duration;
    private String startDate;
    private String endDate;
    private String description;

}
