package battle.service.controller;

import battle.service.dto.PositionUpdateDto;
import battle.service.dto.UnitDamageDto;
import battle.service.dto.UnitDto;
import battle.service.entity.UnitData;
import battle.service.exceptions.NotFoundException;
import battle.service.exceptions.UnitOutsideBattlefieldException;
import battle.service.service.UnitDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/units")
@Slf4j
public class UnitDataController {

    @Value("${uri-builder.scheme}")
    private String scheme;
    @Value("${uri-builder.host}")
    private String host;
    @Value("${uri-builder.port}")
    private Integer port;

    private final UnitDataService unitDataService;

    @PostMapping
    public ResponseEntity<UnitData> registerUnit(@RequestBody UnitDto unitDto) {
        unitDataService.registerUnit(unitDto);

        log.info("New Unit {} has been registered", unitDto);

        return ResponseEntity
                .created(createUriBuilder().build(unitDto.getPosX(), unitDto.getPosY()))
                .build();
    }

    @GetMapping("/x/{posX}/y/{posY}")
    public ResponseEntity<UnitDto> getUnitByCoordinate(@PathVariable Integer posX, @PathVariable Integer posY) {
        UnitDto unitDto = unitDataService.getUnitByCoordinate(posX, posY);

        log.info("Get Unit {}", unitDto);

        return ResponseEntity.ok(unitDto);
    }

    @PatchMapping("/damage")
    public ResponseEntity<?> setDamageUnit(@RequestBody UnitDamageDto unitDamageDto) {
        unitDataService.setDamageUnit(unitDamageDto);

        log.info("Unit get damage {}", unitDamageDto.getDamage());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/position/update")
    public ResponseEntity<?> updateUnitPosition(@RequestBody PositionUpdateDto positionUpdateDto) {
        unitDataService.updateUnitPosition(positionUpdateDto);

        log.info("Unit update position to  ({},{})", positionUpdateDto.getNewPosX(), positionUpdateDto.getNewPosY());

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource doesn't exist or has been deleted")
    public void handleNotFound() { }

    @ExceptionHandler(UnitOutsideBattlefieldException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Unit is outside the battlefield.")
    public void handleUnitOutsideBattlefieldException() { }

    private UriComponentsBuilder createUriBuilder() {

        String uriTemplate = "/units/x/{posX}/y/{posY}";

        return org.springframework.web.util.UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .port(port)
                .path(uriTemplate);
    }
}
