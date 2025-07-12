package com.bubches.literalura.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroDTO(
        Integer id,
        String title,
        List<String> languages,
        Integer download_count,
        List<AutorDTO> authors
) {

}