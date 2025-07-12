package com.bubches.literalura.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorDTO(
        String name,
        Integer birth_year,
        Integer death_year
) {}