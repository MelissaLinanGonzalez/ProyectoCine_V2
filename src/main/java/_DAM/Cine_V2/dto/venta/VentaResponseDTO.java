package _DAM.Cine_V2.dto.venta;

import _DAM.Cine_V2.dto.entrada.EntradaResponseDTO;

import java.time.LocalDateTime;
import java.util.Set;

public record VentaResponseDTO(
        Long id,
        LocalDateTime fecha,
        double importeTotal,
        String metodoPago,
        String estado,
        Long usuarioId,
        Set<EntradaResponseDTO> entradas) {
}
