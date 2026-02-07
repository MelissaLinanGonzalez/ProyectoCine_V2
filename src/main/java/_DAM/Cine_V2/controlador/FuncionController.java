package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.funcion.FuncionRequestDTO;
import _DAM.Cine_V2.dto.funcion.FuncionResponseDTO;
import _DAM.Cine_V2.servicio.FuncionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funciones")
@RequiredArgsConstructor
public class FuncionController {

    private final FuncionService funcionService;

    @GetMapping
    public ResponseEntity<List<FuncionResponseDTO>> findAll() {
        return ResponseEntity.ok(funcionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(funcionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FuncionResponseDTO> create(@Valid @RequestBody FuncionRequestDTO funcionRequestDTO) {
        return new ResponseEntity<>(funcionService.save(funcionRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody FuncionRequestDTO funcionRequestDTO) {
        return ResponseEntity.ok(funcionService.update(id, funcionRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        funcionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
