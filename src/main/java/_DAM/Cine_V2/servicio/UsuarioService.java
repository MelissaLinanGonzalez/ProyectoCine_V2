package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.usuario.UsuarioRequestDTO;
import _DAM.Cine_V2.dto.usuario.UsuarioResponseDTO;
import _DAM.Cine_V2.mapper.UsuarioMapper;
import _DAM.Cine_V2.modelo.Rol;
import _DAM.Cine_V2.modelo.Usuario;
import _DAM.Cine_V2.repositorio.RolRepository;
import _DAM.Cine_V2.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO findById(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    public UsuarioResponseDTO save(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);

        // Handle Roles
        if (usuarioRequestDTO.roles() != null && !usuarioRequestDTO.roles().isEmpty()) {
            Set<Rol> roles = new HashSet<>();
            for (String rolNombre : usuarioRequestDTO.roles()) {
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
                roles.add(rol);
            }
            usuario.setRoles(roles);
        }

        // Handle password
        if (usuarioRequestDTO.password() != null && !usuarioRequestDTO.password().isBlank()) {
            usuario.setPassword(usuarioRequestDTO.password()); // In real app, BCrypt here
        }

        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(saved);
    }

    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setEmail(usuarioRequestDTO.email());
        usuario.setEnabled(usuarioRequestDTO.enabled());

        // Handle Roles
        if (usuarioRequestDTO.roles() != null && !usuarioRequestDTO.roles().isEmpty()) {
            Set<Rol> roles = new HashSet<>();
            for (String rolNombre : usuarioRequestDTO.roles()) {
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
                roles.add(rol);
            }
            usuario.setRoles(roles);
        }

        // Handle password only if provided
        if (usuarioRequestDTO.password() != null && !usuarioRequestDTO.password().isBlank()) {
            usuario.setPassword(usuarioRequestDTO.password()); // In real app, BCrypt here
        }

        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(saved);
    }

    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
