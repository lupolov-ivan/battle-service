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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static battle.service.entity.ShotResult.HIT;
import static battle.service.entity.ShotResult.MISS;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;
    private final GunSubdivisionService gunSubdivisionService;
    private final EnemySubdivisionService enemySubdivisionService;

    public Battle createBattle(BattleDto dto) {
        Battle battle = new Battle();
        battle.setDefenderSubdivisionId(dto.getDefenderSubdivisionId());
        battle.setAttackSubdivisionId(dto.getAttackSubdivisionId());

        battle.getUnits().addAll(enemySubdivisionService.getUnitsDataBySubdivisionId(dto.getAttackSubdivisionId()));
        battle.getUnits().addAll(gunSubdivisionService.getUnitsDataBySubdivisionId(dto.getDefenderSubdivisionId()));

        return battleRepository.save(battle);
    }

    public void startBattle(Integer battleId) {
        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);
        gunSubdivisionService.startSubdivisionPatrolling(maybeBattle.getDefenderSubdivisionId(), battleId);
        enemySubdivisionService.startSubdivisionMoving(maybeBattle.getAttackSubdivisionId(), battleId);
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

            if (unit.getUnitType().equals(UnitType.TANK)
                    && unit.getProtectionLevel() <= unit.getTakenDamage()) {
                unit.setIsAlive(false);
                enemySubdivisionService.setEnemyIsDeadStatus(unit.getUnitId());
            }

            if (unit.getUnitType().equals(UnitType.AFC)
                    && unit.getProtectionLevel() <= unit.getTakenDamage()) {
                unit.setIsAlive(false);
            }

            if (unit.getUnitType().equals(UnitType.INFANTRY) && (unit.getProtectionLevel() * 0.7) <= unit.getTakenDamage()) {
                unit.setIsAlive(false);
                enemySubdivisionService.setEnemyIsDeadStatus(unit.getUnitId());
            }

            shot.setTargetId(unit.getId());
            shot.setTargetType(unit.getUnitType());
            shot.setDamage(dto.getDamage());
            shot.setShotResult(HIT);

            log.info("HIT");
        } else {
            shot.setShotResult(MISS);
            log.info("MISS");
        }

        maybeBattle.getShots().add(shot);

        battleRepository.save(maybeBattle);
    }

    public void updateUnitPosition(Integer battleId, PositionUpdateDto dto) {
        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);
        Set<UnitData> units = maybeBattle.getUnits();

        UnitData maybeUnitData = findUnitByUnitIdAndType(units, dto.getUnitId(), dto.getUnitType()).orElseThrow(NotFoundException::new);

        maybeUnitData.setPosX(dto.getNewPosX());
        maybeUnitData.setPosY(dto.getNewPosY());

        battleRepository.save(maybeBattle);
    }

    public Set<UnitDto> getUnitsByBattleId(Integer battleId) {

        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);
        Set<UnitDto> unitDtos = new HashSet<>();
        maybeBattle.getUnits().forEach(unitData -> {
            UnitDto unitDto = new UnitDto();
            unitDto.setPosX(unitData.getPosX());
            unitDto.setPosY(unitData.getPosY());
            unitDto.setUnitType(unitData.getUnitType());
            unitDto.setProtectionLevel(unitData.getProtectionLevel());
            unitDto.setIsAlive(unitData.getIsAlive());
            unitDto.setUnitId(unitData.getUnitId());

            unitDtos.add(unitDto);
        });

        return unitDtos;
    }

    private Optional<UnitData> findUnitByCoordinate(Set<UnitData> units, Integer posX, Integer posY) {
        return units.stream()
                .filter(data -> data.getPosX().equals(posX))
                .filter(data -> data.getPosY().equals(posY))
                .findFirst();
    }

    private Optional<UnitData> findUnitByUnitIdAndType(Set<UnitData> units, Integer unitId, UnitType unitType) {
        return units.stream()
                .filter(data -> data.getUnitId().equals(unitId))
                .filter(data -> data.getUnitType().equals(unitType))
                .findFirst();
    }
}
