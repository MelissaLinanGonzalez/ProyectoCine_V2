package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.rol.RolRequestDTO;
import _DAM.Cine_V2.dto.rol.RolResponseDTO;
import _DAM.Cine_V2.modelo.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RolMapper {

    RolResponseDTO toResponseDTO(Rol rol);

    @Mapping(target = "id", ignore = true)
    Rol toEntity(RolRequestDTO rolRequestDTO);
}
