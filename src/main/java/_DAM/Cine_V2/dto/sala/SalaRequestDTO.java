package _DAM.Cine_V2.dto.sala;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SalaRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @Min(value = 1, message = "La capacidad debe ser al menos 1") int capacidad) {
}
