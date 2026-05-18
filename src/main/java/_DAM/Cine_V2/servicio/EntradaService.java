package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.entrada.EntradaRequestDTO;
import _DAM.Cine_V2.dto.entrada.EntradaResponseDTO;
import _DAM.Cine_V2.mapper.EntradaMapper;
import _DAM.Cine_V2.modelo.Entrada;
import _DAM.Cine_V2.modelo.EstadoEntrada;
import _DAM.Cine_V2.modelo.Funcion;
import _DAM.Cine_V2.repositorio.EntradaRepository;
import _DAM.Cine_V2.repositorio.FuncionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntradaService {

    private final EntradaRepository entradaRepository;
    private final FuncionRepository funcionRepository;
    private final EntradaMapper entradaMapper;

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> findAll() {
        return entradaRepository.findAll().stream()
                .map(entradaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EntradaResponseDTO findById(Long id) {
        Entrada entrada = entradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con ID: " + id));
        
        // Ownership check
        String currentUserEmail = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && entrada.getVenta() != null && entrada.getVenta().getUsuario() != null && 
            !entrada.getVenta().getUsuario().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Acceso denegado: No tienes permiso para ver esta entrada.");
        }

        return entradaMapper.toResponseDTO(entrada);
    }

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> findMisEntradas(String email) {
        return entradaRepository.findAll().stream()
                .filter(e -> e.getVenta() != null && e.getVenta().getUsuario() != null && e.getVenta().getUsuario().getEmail().equals(email))
                .map(entradaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EntradaResponseDTO save(EntradaRequestDTO entradaRequestDTO) {
        // Validation: Seat availability
        if (isSeatOccupied(entradaRequestDTO.funcionId(), entradaRequestDTO.fila(), entradaRequestDTO.asiento())) {
            throw new RuntimeException(
                    "El asiento " + entradaRequestDTO.fila() + "-" + entradaRequestDTO.asiento() + " ya está ocupado.");
        }

        Entrada entrada = entradaMapper.toEntity(entradaRequestDTO);

        if (entradaRequestDTO.funcionId() != null) {
            Funcion funcion = funcionRepository.findById(entradaRequestDTO.funcionId())
                    .orElseThrow(() -> new RuntimeException(
                            "Funcion no encontrada con ID: " + entradaRequestDTO.funcionId()));
            entrada.setFuncion(funcion);
        }

        // Generate unique code
        entrada.setCodigo(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Default status if not provided
        if (entrada.getEstado() == null) {
            entrada.setEstado(EstadoEntrada.VENDIDA);
        }

        Entrada saved = entradaRepository.save(entrada);
        return entradaMapper.toResponseDTO(saved);
    }

    @Transactional
    public EntradaResponseDTO update(Long id, EntradaRequestDTO entradaRequestDTO) {
        Entrada entrada = entradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con ID: " + id));

        // Check if seat changed and if new seat is available
        if (entrada.getFila() != entradaRequestDTO.fila() || entrada.getAsiento() != entradaRequestDTO.asiento()) {
            if (isSeatOccupied(entradaRequestDTO.funcionId(), entradaRequestDTO.fila(), entradaRequestDTO.asiento())) {
                throw new RuntimeException(
                        "El asiento " + entradaRequestDTO.fila() + "-" + entradaRequestDTO.asiento()
                                + " ya está ocupado.");
            }
        }

        entrada.setFila(entradaRequestDTO.fila());
        entrada.setAsiento(entradaRequestDTO.asiento());
        if (entradaRequestDTO.estado() != null) {
            entrada.setEstado(entradaRequestDTO.estado());
        }

        if (entradaRequestDTO.funcionId() != null) {
            Funcion funcion = funcionRepository.findById(entradaRequestDTO.funcionId())
                    .orElseThrow(() -> new RuntimeException(
                            "Funcion no encontrada con ID: " + entradaRequestDTO.funcionId()));
            entrada.setFuncion(funcion);
        }

        Entrada saved = entradaRepository.save(entrada);
        return entradaMapper.toResponseDTO(saved);
    }

    public boolean isSeatOccupied(Long funcionId, int fila, int asiento) {
        List<Entrada> entradas = entradaRepository.findByFuncionId(funcionId);
        return entradas.stream().anyMatch(
                e -> e.getFila() == fila && e.getAsiento() == asiento && e.getEstado() != EstadoEntrada.CANCELADA);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!entradaRepository.existsById(id)) {
            throw new RuntimeException("Entrada no encontrada con ID: " + id);
        }
        entradaRepository.deleteById(id);
    }
}
