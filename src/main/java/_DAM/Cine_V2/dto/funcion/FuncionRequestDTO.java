package _DAM.Cine_V2.dto.funcion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FuncionRequestDTO(
        @NotNull(message = "La fecha y hora son obligatorias") LocalDateTime fechaHora,
        @Min(value = 0, message = "El precio no puede ser negativo") double precio,
        @NotNull(message = "Debe haber una película asignada") Long peliculaId,
        @NotNull(message = "Debe haber una sala asignada") Long salaId) {
}
