package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.director.DirectorRequestDTO;
import _DAM.Cine_V2.dto.director.DirectorResponseDTO;
import _DAM.Cine_V2.modelo.Director;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DirectorMapper {

    DirectorResponseDTO toResponseDTO(Director director);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "peliculas", ignore = true)
    Director toEntity(DirectorRequestDTO directorRequestDTO);
}
