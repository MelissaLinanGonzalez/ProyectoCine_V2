package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.rol.RolRequestDTO;
import _DAM.Cine_V2.dto.rol.RolResponseDTO;
import _DAM.Cine_V2.mapper.RolMapper;
import _DAM.Cine_V2.modelo.Rol;
import _DAM.Cine_V2.repositorio.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    public List<RolResponseDTO> findAll() {
        return rolRepository.findAll().stream()
                .map(rolMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RolResponseDTO findById(Long id) {
        return rolRepository.findById(id)
                .map(rolMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    public RolResponseDTO save(RolRequestDTO rolRequestDTO) {
        Rol rol = rolMapper.toEntity(rolRequestDTO);
        Rol saved = rolRepository.save(rol);
        return rolMapper.toResponseDTO(saved);
    }

    public RolResponseDTO update(Long id, RolRequestDTO rolRequestDTO) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
        rol.setNombre(rolRequestDTO.nombre());
        Rol saved = rolRepository.save(rol);
        return rolMapper.toResponseDTO(saved);
    }

    public void deleteById(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }
        rolRepository.deleteById(id);
    }
}
