package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.sala.SalaRequestDTO;
import _DAM.Cine_V2.dto.sala.SalaResponseDTO;
import _DAM.Cine_V2.servicio.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // CRUD total solo ADMIN
public class SalaController {

    private final SalaService salaService;

    @GetMapping
    public ResponseEntity<List<SalaResponseDTO>> findAll() {
        return ResponseEntity.ok(salaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SalaResponseDTO> create(@Valid @RequestBody SalaRequestDTO salaRequestDTO) {
        return new ResponseEntity<>(salaService.save(salaRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody SalaRequestDTO salaRequestDTO) {
        return ResponseEntity.ok(salaService.update(id, salaRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
