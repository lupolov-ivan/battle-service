package battle.service.controller;

import battle.service.dto.*;
import battle.service.entity.Battle;
import battle.service.exceptions.NotFoundException;
import battle.service.service.BattleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/battles")
@Slf4j
public class BattleController {

    @Value("${uri-builder.scheme}")
    private String scheme;
    @Value("${uri-builder.host}")
    private String host;
    @Value("${uri-builder.port}")
    private Integer port;

    private final BattleService battleService;

    @PostMapping
    public ResponseEntity<Battle> createBattle(@RequestBody BattleDto dto) {
        Battle battle = battleService.createBattle(dto);
        battleService.startBattle(battle.getId());
        return ResponseEntity
                .created(createUriBuilder("/battles/{id}").build(battle.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Battle>> getAllBattles() {
        return ResponseEntity.ok(battleService.getAllBattles());
    }

    @GetMapping("/{battleId}")
    public ResponseEntity<Battle> getBattleById(@PathVariable Integer battleId) {
        return ResponseEntity.ok(battleService.getBattleById(battleId));
    }

    @PostMapping("/{battleId}/start")
    public ResponseEntity<?> startBattle(@PathVariable Integer battleId) {
        battleService.startBattle(battleId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{battleId}/stop")
    public ResponseEntity<?> stopBattle(@PathVariable Integer battleId, @RequestBody WinnerDto winner) {
        battleService.stopBattle(battleId, winner);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{battleId}/units")
    public ResponseEntity<Set<UnitDto>> getUnitsOnBattlefield(@PathVariable Integer battleId) {
        Set<UnitDto> units = battleService.getUnitsByBattleId( battleId);

        log.info("Get Units {}", units);

        return ResponseEntity.ok(units);
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

    @PatchMapping("/{battleId}/units/state/update")
    public ResponseEntity<?> updateUnitState(@PathVariable Integer battleId, @RequestBody UnitStateDto dto) {
        battleService.updateUnitState(battleId, dto);

        log.info("Unit '{}' changed state to {}", dto.getUnitId(), dto.getUnitState());

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource doesn't exist or has been deleted")
    public void handleNotFound() { }

    private UriComponentsBuilder createUriBuilder(String uriTemplate) {
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .port(port)
                .path(uriTemplate);
    }
}
