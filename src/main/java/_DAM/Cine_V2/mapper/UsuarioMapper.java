package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.usuario.UsuarioRequestDTO;
import _DAM.Cine_V2.dto.usuario.UsuarioResponseDTO;
import _DAM.Cine_V2.modelo.Rol;
import _DAM.Cine_V2.modelo.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToStrings")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "ventas", ignore = true)
    Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO);

    @Named("mapRolesToStrings")
    default Set<String> mapRolesToStrings(Set<Rol> roles) {
        if (roles == null)
            return Collections.emptySet();
        return roles.stream().map(Rol::getNombre).collect(Collectors.toSet());
    }
}
