package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.config.JwtUtil;
import _DAM.Cine_V2.dto.Login.LoginRequestDTO;
import _DAM.Cine_V2.dto.Login.LoginResponseDTO;
import _DAM.Cine_V2.dto.Login.RefreshTokenRequestDTO;
import _DAM.Cine_V2.dto.Login.RegisterRequestDTO;
import _DAM.Cine_V2.modelo.RefreshToken;
import _DAM.Cine_V2.modelo.Usuario;
import _DAM.Cine_V2.repositorio.UsuarioRepository;
import _DAM.Cine_V2.servicio.RefreshTokenService;
import _DAM.Cine_V2.servicio.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegisterRequestDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO req) {
        return ResponseEntity.ok(usuarioService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO req) {
        RefreshToken refreshToken = refreshTokenService.findByToken(req.refreshToken());
        refreshTokenService.verificarExpiracion(refreshToken);

        Usuario usuario = refreshToken.getUsuario();
        String newAccessToken = jwtUtil.generateToken(usuario);

        return ResponseEntity.ok(new LoginResponseDTO(
                usuario.getEmail(),
                "Token renovado",
                newAccessToken,
                refreshToken.getToken()
        ));
    }
}