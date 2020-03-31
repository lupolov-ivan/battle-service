package battle.service.controller;

import battle.service.entity.UnitData;
import battle.service.exceptions.NotFoundException;
import battle.service.service.UnitDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UnitDataController {

    private final UnitDataService unitDataService;

    @PostMapping("/units")
    public ResponseEntity<UnitData> addUnitData(@RequestBody UnitData unitData) {
        unitDataService.addUnitData(unitData);
        return ResponseEntity
                .created(URI.create("units/"+ unitData.getId()))
                .build();
    }

    @GetMapping("units/x/{posX}/y/{posY}")
    public ResponseEntity<UnitData> getUnitByCoordinate(@PathVariable Integer posX, @PathVariable Integer posY) {
        UnitData data = unitDataService.getUnitByCoordinate(posX, posY);
        return ResponseEntity.ok(data);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource doesn't exist or has been deleted")
    public void handleNotFound(NotFoundException e) {}
}
