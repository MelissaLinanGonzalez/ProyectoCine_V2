package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.entrada.EntradaResponseDTO;
import _DAM.Cine_V2.dto.venta.VentaRequestDTO;
import _DAM.Cine_V2.dto.venta.VentaResponseDTO;
import _DAM.Cine_V2.modelo.Entrada;
import _DAM.Cine_V2.modelo.Venta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { EntradaMapper.class })
public interface VentaMapper {

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "entradas", source = "entradas", qualifiedByName = "mapEntradasToResponseDTO")
    VentaResponseDTO toResponseDTO(Venta venta);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "importeTotal", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "entradas", ignore = true)
    Venta toEntity(VentaRequestDTO ventaRequestDTO);

    @Named("mapEntradasToResponseDTO")
    default Set<EntradaResponseDTO> mapEntradasToResponseDTO(Set<Entrada> entradas) {
        if (entradas == null)
            return Collections.emptySet();
        return entradas.stream()
                .map(entrada -> new EntradaResponseDTO(
                        entrada.getId(),
                        entrada.getCodigo(),
                        entrada.getFila(),
                        entrada.getAsiento(),
                        entrada.getEstado(),
                        entrada.getFuncion() != null ? entrada.getFuncion().getId() : null,
                        entrada.getVenta() != null ? entrada.getVenta().getId() : null))
                .collect(Collectors.toSet());
    }
}
