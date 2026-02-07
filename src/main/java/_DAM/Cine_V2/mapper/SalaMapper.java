package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.sala.SalaRequestDTO;
import _DAM.Cine_V2.dto.sala.SalaResponseDTO;
import _DAM.Cine_V2.modelo.Sala;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SalaMapper {

    SalaResponseDTO toResponseDTO(Sala sala);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "funciones", ignore = true)
    Sala toEntity(SalaRequestDTO salaRequestDTO);
}
