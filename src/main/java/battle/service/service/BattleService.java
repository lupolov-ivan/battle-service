package battle.service.service;

import battle.service.dto.BattleDto;
import battle.service.dto.PositionUpdateDto;
import battle.service.dto.UnitDamageDto;
import battle.service.dto.UnitDto;
import battle.service.entity.Battle;
import battle.service.entity.Shot;
import battle.service.entity.UnitData;
import battle.service.entity.UnitType;
import battle.service.exceptions.NotFoundException;
import battle.service.repository.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static battle.service.entity.ShotResult.HIT;
import static battle.service.entity.ShotResult.MISS;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;
    private final GunSubdivisionService gunSubdivisionService;

    public Battle createBattle(BattleDto dto) {
        Battle battle = new Battle();
        battle.setDefenderSubdivisionId(dto.getDefenderSubdivisionId());
        battle.setAttackSubdivisionId(dto.getAttackSubdivisionId());

        battle.getUnits().addAll(Arrays.asList(
                new UnitData(null, 2, 48, 10, UnitType.TANK, 0.0, true),
                new UnitData(null, 4, 48, 10, UnitType.TANK, 0.0, true),
                new UnitData(null, 6, 48, 10, UnitType.TANK, 0.0, true),
                new UnitData(null, 8, 48, 10, UnitType.TANK, 0.0, true)
        )); // TODO: replace with a real battle.getUnits().addAll(enemySubdivisionService.getUnitsDataBySubdivisionId(dto.getAttackSubdivisionId())).

        battle.getUnits().addAll(gunSubdivisionService.getUnitsDataBySubdivisionId(dto.getDefenderSubdivisionId()));

        return battleRepository.save(battle);
    }

    public void startBattle(Integer battleId) {
        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);
        gunSubdivisionService.startSubdivisionPatrolling(maybeBattle.getDefenderSubdivisionId(), battleId);
        // TODO: put call enemyDivisionService(maybeBattle.getAttackSubdivisionId(), battleId);
    }

    public UnitDto getUnitByCoordinate(Integer posX, Integer posY, Integer battleId) {

        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);
        Set<UnitData> units = maybeBattle.getUnits();

        UnitData maybeUnitData = findUnitByCoordinate(units, posX, posY).orElseThrow(NotFoundException::new);

        UnitDto unitDto = new UnitDto();

        unitDto.setPosX(maybeUnitData.getPosX());
        unitDto.setPosY(maybeUnitData.getPosX());
        unitDto.setProtectionLevel(maybeUnitData.getProtectionLevel());
        unitDto.setUnitType(maybeUnitData.getUnitType());
        unitDto.setIsAlive(maybeUnitData.getIsAlive());

        return unitDto;
    }

    public void setDamageUnit(Integer battleId, UnitDamageDto dto) {
        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);
        Set<UnitData> units = maybeBattle.getUnits();

        Optional<UnitData> maybeUnitData = findUnitByCoordinate(units, dto.getPosX(), dto.getPosY());

        Shot shot = new Shot();

        if (maybeUnitData.isPresent()) {

            UnitData unit = maybeUnitData.get();

            double currentDamage = unit.getTakenDamage();
            unit.setTakenDamage(currentDamage + dto.getDamage());

            if ((unit.getUnitType().equals(UnitType.TANK) || unit.getUnitType().equals(UnitType.AFC)) && unit.getProtectionLevel() <= unit.getTakenDamage()) {
                unit.setIsAlive(false);
            }

            if (unit.getUnitType().equals(UnitType.INFANTRY) && (unit.getProtectionLevel() * 0.7) <= unit.getTakenDamage()) {
                unit.setIsAlive(false);
            }

            shot.setTargetId(unit.getId());
            shot.setTargetType(unit.getUnitType());
            shot.setDamage(dto.getDamage());
            shot.setShotResult(HIT);

        } else {
            shot.setShotResult(MISS);
        }

        maybeBattle.getShots().add(shot);

        battleRepository.save(maybeBattle);
    }

    public void updateUnitPosition(Integer battleId, PositionUpdateDto dto) {
        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);
        Set<UnitData> units = maybeBattle.getUnits();

        UnitData maybeUnitData = findUnitByCoordinate(units, dto.getPosX(), dto.getPosY()).orElseThrow(NotFoundException::new);

        maybeUnitData.setPosX(dto.getNewPosX());
        maybeUnitData.setPosY(dto.getNewPosY());

        battleRepository.save(maybeBattle);
    }

    private Optional<UnitData> findUnitByCoordinate(Set<UnitData> units, Integer posX, Integer posY) {
        return units.stream()
                .filter(data -> data.getPosX().equals(posX))
                .filter(data -> data.getPosY().equals(posY))
                .findFirst();
    }
}
