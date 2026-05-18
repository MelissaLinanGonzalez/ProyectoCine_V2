package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.config.JwtUtil;
import _DAM.Cine_V2.dto.Login.LoginRequestDTO;
import _DAM.Cine_V2.dto.Login.LoginResponseDTO;
import _DAM.Cine_V2.dto.Login.RegisterRequestDTO;
import _DAM.Cine_V2.dto.usuario.UsuarioInputDTO;
import _DAM.Cine_V2.dto.usuario.UsuarioOutputDTO;
import _DAM.Cine_V2.mapper.UsuarioMapper;
import _DAM.Cine_V2.modelo.RefreshToken;
import _DAM.Cine_V2.modelo.Rol;
import _DAM.Cine_V2.modelo.Usuario;
import _DAM.Cine_V2.repositorio.RolRepository;
import _DAM.Cine_V2.repositorio.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public List<UsuarioOutputDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioOutputDTO findById(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrada con ID: " + id));
    }

    @Transactional
    public UsuarioOutputDTO save(UsuarioInputDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

        // Handle Roles
        if (usuarioDTO.roles() != null && !usuarioDTO.roles().isEmpty()) {
            Set<Rol> roles = new HashSet<>();
            for (String rolNombre : usuarioDTO.roles()) {
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
                roles.add(rol);
            }
            usuario.setRoles(roles);
        }

        // Handle password (basic for now)
        if (usuarioDTO.password() != null && !usuarioDTO.password().isBlank()) {
            usuario.setPassword(usuarioDTO.password()); // In real app, B.crypt here
        }

        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(saved);
    }

    @Transactional
    public UsuarioOutputDTO update(Long id, UsuarioInputDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrada con ID: " + id));

        usuarioMapper.update(usuarioDTO, usuario);

        // Handle Roles
        if (usuarioDTO.roles() != null) {
            Set<Rol> roles = new HashSet<>();
            for (String rolNombre : usuarioDTO.roles()) {
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
                roles.add(rol);
            }
            usuario.setRoles(roles);
        }

        if (usuarioDTO.password() != null && !usuarioDTO.password().isBlank()) {
            usuario.setPassword(usuarioDTO.password());
        }

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // 🔹 REGISTRO — devuelve tokens
    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO req) {
        Usuario u = new Usuario();
        u.setEmail(req.email());
        u.setPassword(encoder.encode(req.password()));

        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolUser);
        u.setRoles(roles);
        usuarioRepository.save(u);

        // Generar tokens
        String accessToken = jwtUtil.generateToken(u);
        RefreshToken refreshToken = refreshTokenService.crearRefreshToken(u.getEmail());

        return new LoginResponseDTO(u.getEmail(), "Registro OK", accessToken, refreshToken.getToken());
    }

    // 🔹 LOGIN
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO req) {
        Usuario u = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!encoder.matches(req.password(), u.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String accessToken = jwtUtil.generateToken(u);
        RefreshToken refreshToken = refreshTokenService.crearRefreshToken(u.getEmail());

        return new LoginResponseDTO(u.getEmail(), "Login OK", accessToken, refreshToken.getToken());
    }
}
