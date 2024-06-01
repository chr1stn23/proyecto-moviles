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
public class Notifications {

    @EqualsAndHashCode.Include
    private int id;
    private String message;
    private String sendDate;
    private String event;

}
