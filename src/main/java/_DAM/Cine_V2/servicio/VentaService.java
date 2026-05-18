package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.entrada.EntradaRequestDTO;
import _DAM.Cine_V2.dto.venta.VentaRequestDTO;
import _DAM.Cine_V2.dto.venta.VentaResponseDTO;
import _DAM.Cine_V2.mapper.EntradaMapper;
import _DAM.Cine_V2.mapper.VentaMapper;
import _DAM.Cine_V2.modelo.*;
import _DAM.Cine_V2.repositorio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FuncionRepository funcionRepository;
    private final EntradaRepository entradaRepository;
    private final VentaMapper ventaMapper;
    private final EntradaMapper entradaMapper;

    public List<VentaResponseDTO> findAll() {
        return ventaRepository.findAll().stream()
                .map(ventaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VentaResponseDTO findById(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
        
        // Ownership check
        String currentUserEmail = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && venta.getUsuario() != null && !venta.getUsuario().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Acceso denegado: No tienes permiso para ver esta venta.");
        }

        return ventaMapper.toResponseDTO(venta);
    }

    @Transactional
    public VentaResponseDTO save(VentaRequestDTO ventaRequestDTO) {
        Venta venta = ventaMapper.toEntity(ventaRequestDTO);

        // Set sale date
        venta.setFecha(LocalDateTime.now());
        venta.setEstado("COMPLETADA");

        if (ventaRequestDTO.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(ventaRequestDTO.usuarioId())
                    .orElseThrow(
                            () -> new RuntimeException("Usuario no encontrado con ID: " + ventaRequestDTO.usuarioId()));
            venta.setUsuario(usuario);
        }

        // Set payment method
        venta.setMetodoPago(ventaRequestDTO.metodoPago());

        // Process tickets
        double importeTotal = 0.0;
        if (ventaRequestDTO.entradas() != null && !ventaRequestDTO.entradas().isEmpty()) {
            Set<Entrada> entradasEntities = new HashSet<>();
            for (EntradaRequestDTO eDTO : ventaRequestDTO.entradas()) {
                if (eDTO.funcionId() == null)
                    throw new RuntimeException("Entrada sin funcion ID");

                Funcion funcion = funcionRepository.findById(eDTO.funcionId())
                        .orElseThrow(() -> new RuntimeException("Funcion no encontrada " + eDTO.funcionId()));

                // Check availability
                boolean occupied = entradaRepository.findByFuncionId(funcion.getId()).stream()
                        .anyMatch(e -> e.getFila() == eDTO.fila() && e.getAsiento() == eDTO.asiento()
                                && e.getEstado() != EstadoEntrada.CANCELADA);

                if (occupied) {
                    throw new RuntimeException("Asiento ocupado: " + eDTO.fila() + "-" + eDTO.asiento());
                }

                Entrada entrada = entradaMapper.toEntity(eDTO);
                entrada.setFuncion(funcion);
                entrada.setVenta(venta);
                entrada.setCodigo(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                if (entrada.getEstado() == null)
                    entrada.setEstado(EstadoEntrada.VENDIDA);

                entradasEntities.add(entrada);
                importeTotal += funcion.getPrecio();
            }
            venta.setEntradas(entradasEntities);
        }

        venta.setImporteTotal(importeTotal);
        Venta saved = ventaRepository.save(venta);
        return ventaMapper.toResponseDTO(saved);
    }

    @Transactional
    public VentaResponseDTO update(Long id, VentaRequestDTO ventaRequestDTO) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        venta.setMetodoPago(ventaRequestDTO.metodoPago());

        if (ventaRequestDTO.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(ventaRequestDTO.usuarioId())
                    .orElseThrow(
                            () -> new RuntimeException("Usuario no encontrado con ID: " + ventaRequestDTO.usuarioId()));
            venta.setUsuario(usuario);
        }

        Venta saved = ventaRepository.save(venta);
        return ventaMapper.toResponseDTO(saved);
    }

    public void deleteById(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        ventaRepository.deleteById(id);
    }
}
