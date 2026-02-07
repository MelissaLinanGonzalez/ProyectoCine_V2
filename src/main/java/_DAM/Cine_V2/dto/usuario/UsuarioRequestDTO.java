package _DAM.Cine_V2.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UsuarioRequestDTO(
        @Email(message = "El formato del email no es válido") @NotBlank(message = "El email no puede estar vacío") String email,
        @NotBlank(message = "La contraseña es obligatoria") String password,
        boolean enabled,
        Set<String> roles) {
}
