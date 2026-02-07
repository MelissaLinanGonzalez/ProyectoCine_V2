package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.rol.RolRequestDTO;
import _DAM.Cine_V2.dto.rol.RolResponseDTO;
import _DAM.Cine_V2.servicio.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<RolResponseDTO>> findAll() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RolResponseDTO> create(@Valid @RequestBody RolRequestDTO rolRequestDTO) {
        return new ResponseEntity<>(rolService.save(rolRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody RolRequestDTO rolRequestDTO) {
        return ResponseEntity.ok(rolService.update(id, rolRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
