package _DAM.Cine_V2.dto.venta;

import _DAM.Cine_V2.dto.entrada.EntradaRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record VentaRequestDTO(
        @NotBlank(message = "El método de pago es obligatorio") String metodoPago,
        @NotNull(message = "El usuario es obligatorio") Long usuarioId,
        @NotEmpty(message = "Debe incluir al menos una entrada") @Valid Set<EntradaRequestDTO> entradas) {
}
