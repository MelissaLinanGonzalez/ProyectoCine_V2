package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.usuario.UsuarioInputDTO;
import _DAM.Cine_V2.dto.usuario.UsuarioOutputDTO;
import _DAM.Cine_V2.modelo.Rol;
import _DAM.Cine_V2.modelo.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToStrings")
    UsuarioOutputDTO toDTO(Usuario usuario);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "ventas", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true") // Default enabled on creation
    Usuario toEntity(UsuarioInputDTO usuarioInputDTO);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "ventas", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Don't wipe password if null, service handles it
    void update(UsuarioInputDTO usuarioInputDTO, @MappingTarget Usuario usuario);

    @Named("mapRolesToStrings")
    default Set<String> mapRolesToStrings(Set<Rol> roles) {
        if (roles == null)
            return Collections.emptySet();
        return roles.stream().map(Rol::getNombre).collect(Collectors.toSet());
    }
}
