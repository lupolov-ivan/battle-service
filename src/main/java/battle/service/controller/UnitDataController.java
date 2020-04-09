package battle.service.controller;

import battle.service.dto.PositionUpdateDto;
import battle.service.dto.UnitDamageDto;
import battle.service.dto.UnitDto;
import battle.service.exceptions.NotFoundException;
import battle.service.service.UnitDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/units")
@Slf4j
public class UnitDataController {

    private final UnitDataService unitDataService;

    @GetMapping("/x/{posX}/y/{posY}/battle/{battleId}")
    public ResponseEntity<UnitDto> getUnitByCoordinate(@PathVariable Integer posX, @PathVariable Integer posY, @PathVariable Integer battleId) {
        UnitDto unitDto = unitDataService.getUnitByCoordinateAndBattleId(posX, posY, battleId);

        log.info("Get Unit {}", unitDto);

        return ResponseEntity.ok(unitDto);
    }

    @PatchMapping("/damage/battle/{battleId}")
    public ResponseEntity<?> setDamageUnit(@PathVariable Integer battleId, @RequestBody UnitDamageDto unitDamageDto) {
        unitDataService.setDamageUnit(battleId, unitDamageDto);

        log.info("Unit get damage {}", unitDamageDto.getDamage());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/position/update/battle/{battleId}")
    public ResponseEntity<?> updateUnitPosition(@PathVariable Integer battleId, @RequestBody PositionUpdateDto positionUpdateDto) {
        unitDataService.updateUnitPosition(battleId, positionUpdateDto);

        log.info("Unit update position to  ({},{})", positionUpdateDto.getNewPosX(), positionUpdateDto.getNewPosY());

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource doesn't exist or has been deleted")
    public void handleNotFound() { }
}
