package battle.service.controller;

import battle.service.dto.BattleDto;
import battle.service.dto.PositionUpdateDto;
import battle.service.dto.UnitDamageDto;
import battle.service.dto.UnitDto;
import battle.service.entity.Battle;
import battle.service.exceptions.NotFoundException;
import battle.service.service.BattleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/battles")
@Slf4j
public class BattleController {

    private final BattleService battleService;

    @PostMapping
    public ResponseEntity<Battle> createBattle(@RequestBody BattleDto dto) {
        Battle battle = battleService.createBattle(dto);
        return ResponseEntity
                .created(URI.create("/battles/"+ battle.getId()))
                .build();
    }

    @PostMapping("/{battleId}/start")
    public ResponseEntity<?> startBattle(@PathVariable Integer battleId) {
        battleService.startBattle(battleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{battleId}/units/x/{posX}/y/{posY}")
    public ResponseEntity<UnitDto> getUnitByCoordinate(@PathVariable Integer posX, @PathVariable Integer posY, @PathVariable Integer battleId) {
        UnitDto unitDto = battleService.getUnitByCoordinate(posX, posY, battleId);

        log.info("Get Unit {}", unitDto);

        return ResponseEntity.ok(unitDto);
    }

    @PatchMapping("/{battleId}/units/damage")
    public ResponseEntity<?> setDamageUnit(@PathVariable Integer battleId, @RequestBody UnitDamageDto unitDamageDto) {
        battleService.setDamageUnit(battleId, unitDamageDto);

        log.info("Unit get damage {}", unitDamageDto.getDamage());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{battleId}/units/position/update")
    public ResponseEntity<?> updateUnitPosition(@PathVariable Integer battleId, @RequestBody PositionUpdateDto positionUpdateDto) {
        battleService.updateUnitPosition(battleId, positionUpdateDto);

        log.info("Unit update position to  ({},{})", positionUpdateDto.getNewPosX(), positionUpdateDto.getNewPosY());

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource doesn't exist or has been deleted")
    public void handleNotFound() { }
}
