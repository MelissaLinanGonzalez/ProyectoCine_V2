package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.venta.VentaRequestDTO;
import _DAM.Cine_V2.dto.venta.VentaResponseDTO;
import _DAM.Cine_V2.servicio.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    // ADMIN puede ver todas
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> findAll() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    // USER puede comprar (crear venta)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<VentaResponseDTO> create(@Valid @RequestBody VentaRequestDTO ventaRequestDTO) {
        return new ResponseEntity<>(ventaService.save(ventaRequestDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody VentaRequestDTO ventaRequestDTO) {
        return ResponseEntity.ok(ventaService.update(id, ventaRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ventaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
