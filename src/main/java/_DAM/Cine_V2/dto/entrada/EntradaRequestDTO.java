package _DAM.Cine_V2.dto.entrada;

import _DAM.Cine_V2.modelo.EstadoEntrada;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EntradaRequestDTO(
        @Min(value = 1, message = "La fila debe ser al menos 1") int fila,
        @Min(value = 1, message = "El asiento debe ser al menos 1") int asiento,
        EstadoEntrada estado,
        @NotNull(message = "La función es obligatoria") Long funcionId) {
}
