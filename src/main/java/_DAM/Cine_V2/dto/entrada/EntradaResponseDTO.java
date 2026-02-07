package _DAM.Cine_V2.dto.entrada;

import _DAM.Cine_V2.modelo.EstadoEntrada;

public record EntradaResponseDTO(
        Long id,
        String codigo,
        int fila,
        int asiento,
        EstadoEntrada estado,
        Long funcionId,
        Long ventaId) {
}
