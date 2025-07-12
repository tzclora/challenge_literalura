package com.bubches.literalura.DTO;


import java.util.List;

public record ApiResponseDTO(
        Integer count,
        String next,
        String previous,
        List<LibroDTO> results
) {}
