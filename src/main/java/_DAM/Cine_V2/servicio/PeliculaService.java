package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.pelicula.PeliculaRequestDTO;
import _DAM.Cine_V2.dto.pelicula.PeliculaResponseDTO;
import _DAM.Cine_V2.mapper.PeliculaMapper;
import _DAM.Cine_V2.modelo.Actor;
import _DAM.Cine_V2.modelo.Director;
import _DAM.Cine_V2.modelo.Pelicula;
import _DAM.Cine_V2.repositorio.ActorRepository;
import _DAM.Cine_V2.repositorio.DirectorRepository;
import _DAM.Cine_V2.repositorio.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeliculaService {

    private final PeliculaRepository peliculaRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;
    private final PeliculaMapper peliculaMapper;

    @Transactional(readOnly = true)
    public List<PeliculaResponseDTO> findAll() {
        return peliculaRepository.findAll().stream()
                .map(peliculaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PeliculaResponseDTO findById(Long id) {
        return peliculaRepository.findById(id)
                .map(peliculaMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Pelicula no encontrada con ID: " + id));
    }

    @Transactional
    public PeliculaResponseDTO save(PeliculaRequestDTO peliculaRequestDTO) {
        Pelicula pelicula = peliculaMapper.toEntity(peliculaRequestDTO);

        if (peliculaRequestDTO.directorId() != null) {
            Director director = directorRepository.findById(peliculaRequestDTO.directorId())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Director no encontrado con ID: " + peliculaRequestDTO.directorId()));
            pelicula.setDirector(director);
        }

        if (peliculaRequestDTO.actorIds() != null && !peliculaRequestDTO.actorIds().isEmpty()) {
            List<Actor> actores = actorRepository.findAllById(peliculaRequestDTO.actorIds());
            if (actores.size() != peliculaRequestDTO.actorIds().size()) {
                throw new RuntimeException("Algunos actores no fueron encontrados");
            }
            pelicula.setActores(new HashSet<>(actores));
        }

        Pelicula saved = peliculaRepository.save(pelicula);
        return peliculaMapper.toResponseDTO(saved);
    }

    @Transactional
    public PeliculaResponseDTO update(Long id, PeliculaRequestDTO peliculaRequestDTO) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pelicula no encontrada con ID: " + id));

        pelicula.setTitulo(peliculaRequestDTO.titulo());
        pelicula.setDuracion(peliculaRequestDTO.duracion());
        pelicula.setEdadMinima(peliculaRequestDTO.edadMinima());

        if (peliculaRequestDTO.directorId() != null) {
            Director director = directorRepository.findById(peliculaRequestDTO.directorId())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Director no encontrado con ID: " + peliculaRequestDTO.directorId()));
            pelicula.setDirector(director);
        }

        if (peliculaRequestDTO.actorIds() != null) {
            List<Actor> actores = actorRepository.findAllById(peliculaRequestDTO.actorIds());
            if (actores.size() != peliculaRequestDTO.actorIds().size()) {
                throw new RuntimeException("Algunos actores no fueron encontrados");
            }
            pelicula.setActores(new HashSet<>(actores));
        }

        Pelicula saved = peliculaRepository.save(pelicula);
        return peliculaMapper.toResponseDTO(saved);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!peliculaRepository.existsById(id)) {
            throw new RuntimeException("Pelicula no encontrada con ID: " + id);
        }
        peliculaRepository.deleteById(id);
    }
}
