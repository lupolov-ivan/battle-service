package battle.service.controller;

import battle.service.dto.ParticipatingSubdivisionDto;
import battle.service.entity.ParticipatingSubdivision;
import battle.service.exceptions.NotFoundException;
import battle.service.service.ParticipatingSubdivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participants")
public class ParticipatingSubdivisionController {

    private final ParticipatingSubdivisionService participatingSubdivisionService;

    @PostMapping
    public ResponseEntity<ParticipatingSubdivision> registerParticipant(@RequestBody ParticipatingSubdivisionDto dto) {
        ParticipatingSubdivision subdivision = participatingSubdivisionService.registerParticipant(dto);
        return ResponseEntity
                .created(URI.create("/participants/" + subdivision.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<ParticipatingSubdivision>> getAllSubdivisions() {
        return ResponseEntity.ok(participatingSubdivisionService.getAllParticipant());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteSubdivisionById(@PathVariable Integer id) {
        participatingSubdivisionService.deleteParticipantById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource doesn't exist or has been deleted")
    public void handleNotFound() { }
}
