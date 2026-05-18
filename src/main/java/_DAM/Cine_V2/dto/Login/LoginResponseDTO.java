package _DAM.Cine_V2.dto.Login;

public record LoginResponseDTO(
        String email,
        String message,
        String accessToken,
        String refreshToken
) {}