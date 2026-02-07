package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.director.DirectorRequestDTO;
import _DAM.Cine_V2.dto.director.DirectorResponseDTO;
import _DAM.Cine_V2.mapper.DirectorMapper;
import _DAM.Cine_V2.modelo.Director;
import _DAM.Cine_V2.repositorio.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;

    public List<DirectorResponseDTO> findAll() {
        return directorRepository.findAll().stream()
                .map(directorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DirectorResponseDTO findById(Long id) {
        return directorRepository.findById(id)
                .map(directorMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Director no encontrado con ID: " + id));
    }

    public DirectorResponseDTO save(DirectorRequestDTO directorRequestDTO) {
        Director director = directorMapper.toEntity(directorRequestDTO);
        Director saved = directorRepository.save(director);
        return directorMapper.toResponseDTO(saved);
    }

    public DirectorResponseDTO update(Long id, DirectorRequestDTO directorRequestDTO) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director no encontrado con ID: " + id));
        director.setNombre(directorRequestDTO.nombre());
        Director saved = directorRepository.save(director);
        return directorMapper.toResponseDTO(saved);
    }

    public void deleteById(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new RuntimeException("Director no encontrado con ID: " + id);
        }
        directorRepository.deleteById(id);
    }
}
