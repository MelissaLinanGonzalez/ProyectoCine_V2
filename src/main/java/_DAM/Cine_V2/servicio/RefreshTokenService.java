package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.modelo.RefreshToken;
import _DAM.Cine_V2.modelo.Usuario;
import _DAM.Cine_V2.repositorio.RefreshTokenRepository;
import _DAM.Cine_V2.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Transactional
    public RefreshToken crearRefreshToken(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. Borramos el token viejo
        refreshTokenRepository.deleteByUsuario(usuario);

        // 2. ¡CRÍTICO! Obligamos a la BD a ejecutar el borrado AHORA MISMO
        refreshTokenRepository.flush();

        // 3. Creamos e insertamos el nuevo
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setFechaExpiracion(Instant.now().plusMillis(604800000));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verificarExpiracion(RefreshToken token) {
        if (token.getFechaExpiracion().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado. Inicie sesión de nuevo.");
        }
        return token;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token no encontrado"));
    }
}
