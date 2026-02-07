package _DAM.Cine_V2.dto.pelicula;

import java.util.Set;

public record PeliculaResponseDTO(
        Long id,
        String titulo,
        int duracion,
        int edadMinima,
        Long directorId,
        Set<Long> actorIds) {
}
