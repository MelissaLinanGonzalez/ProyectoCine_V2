package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.actor.ActorRequestDTO;
import _DAM.Cine_V2.dto.actor.ActorResponseDTO;
import _DAM.Cine_V2.modelo.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActorMapper {

    ActorResponseDTO toResponseDTO(Actor actor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "peliculas", ignore = true)
    Actor toEntity(ActorRequestDTO actorRequestDTO);
}
