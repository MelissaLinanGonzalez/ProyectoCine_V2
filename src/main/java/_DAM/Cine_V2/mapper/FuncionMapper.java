package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.funcion.FuncionRequestDTO;
import _DAM.Cine_V2.dto.funcion.FuncionResponseDTO;
import _DAM.Cine_V2.modelo.Funcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FuncionMapper {

    @Mapping(target = "peliculaId", source = "pelicula.id")
    @Mapping(target = "salaId", source = "sala.id")
    FuncionResponseDTO toResponseDTO(Funcion funcion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pelicula", ignore = true)
    @Mapping(target = "sala", ignore = true)
    @Mapping(target = "entradas", ignore = true)
    Funcion toEntity(FuncionRequestDTO funcionRequestDTO);
}
