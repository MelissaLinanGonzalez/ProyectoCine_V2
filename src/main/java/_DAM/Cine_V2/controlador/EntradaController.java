package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.entrada.EntradaRequestDTO;
import _DAM.Cine_V2.dto.entrada.EntradaResponseDTO;
import _DAM.Cine_V2.servicio.EntradaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entradas")
@RequiredArgsConstructor
public class EntradaController {

    private final EntradaService entradaService;

    @GetMapping
    public ResponseEntity<List<EntradaResponseDTO>> findAll() {
        return ResponseEntity.ok(entradaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(entradaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EntradaResponseDTO> create(@Valid @RequestBody EntradaRequestDTO entradaRequestDTO) {
        return new ResponseEntity<>(entradaService.save(entradaRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntradaResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody EntradaRequestDTO entradaRequestDTO) {
        return ResponseEntity.ok(entradaService.update(id, entradaRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entradaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
