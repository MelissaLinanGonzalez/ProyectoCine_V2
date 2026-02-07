package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.pelicula.PeliculaRequestDTO;
import _DAM.Cine_V2.dto.pelicula.PeliculaResponseDTO;
import _DAM.Cine_V2.servicio.PeliculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/peliculas")
@RequiredArgsConstructor
public class PeliculaController {

    private final PeliculaService peliculaService;

    @GetMapping
    public ResponseEntity<List<PeliculaResponseDTO>> findAll() {
        return ResponseEntity.ok(peliculaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeliculaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(peliculaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PeliculaResponseDTO> create(@Valid @RequestBody PeliculaRequestDTO peliculaRequestDTO) {
        return new ResponseEntity<>(peliculaService.save(peliculaRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeliculaResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody PeliculaRequestDTO peliculaRequestDTO) {
        return ResponseEntity.ok(peliculaService.update(id, peliculaRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        peliculaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
