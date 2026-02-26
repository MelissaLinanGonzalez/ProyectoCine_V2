package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.usuario.UsuarioInputDTO;
import _DAM.Cine_V2.dto.usuario.UsuarioOutputDTO;
import _DAM.Cine_V2.servicio.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioOutputDTO>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioOutputDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioOutputDTO> create(@Valid @RequestBody UsuarioInputDTO usuarioRequestDTO) {
        return new ResponseEntity<>(usuarioService.save(usuarioRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioOutputDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody UsuarioInputDTO usuarioRequestDTO) {
        return ResponseEntity.ok(usuarioService.update(id, usuarioRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
