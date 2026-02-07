package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.director.DirectorRequestDTO;
import _DAM.Cine_V2.dto.director.DirectorResponseDTO;
import _DAM.Cine_V2.servicio.DirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/directores")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    public ResponseEntity<List<DirectorResponseDTO>> findAll() {
        return ResponseEntity.ok(directorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DirectorResponseDTO> create(@Valid @RequestBody DirectorRequestDTO directorRequestDTO) {
        return new ResponseEntity<>(directorService.save(directorRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DirectorResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody DirectorRequestDTO directorRequestDTO) {
        return ResponseEntity.ok(directorService.update(id, directorRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        directorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
