package battle.service.service;

import battle.service.dto.*;
import battle.service.entity.Battle;
import battle.service.entity.Shot;
import battle.service.entity.UnitData;
import battle.service.entity.UnitType;
import battle.service.exceptions.NotFoundException;
import battle.service.repository.BattleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static battle.service.entity.ShotResult.*;
import static battle.service.entity.UnitState.*;
import static battle.service.entity.UnitType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;
    private final GunSubdivisionService gunSubdivisionService;
    private final EnemySubdivisionService enemySubdivisionService;
    private final Clock clock;

    public Battle createBattle(BattleDto dto) {
        Battle battle = new Battle();

        battle.setDefenderSubdivisionId(dto.getDefenderSubdivisionId());
        battle.setAttackSubdivisionId(dto.getAttackSubdivisionId());

        battle.getUnits().addAll(enemySubdivisionService.getUnitsDataBySubdivisionId(dto.getAttackSubdivisionId()));
        battle.getUnits().addAll(gunSubdivisionService.getUnitsDataBySubdivisionId(dto.getDefenderSubdivisionId()));

        battle.setIsOver(false);

        return battleRepository.save(battle);
    }

    public void startBattle(Integer battleId) {
        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);

        maybeBattle.setStartAt(LocalDateTime.now(clock));

        gunSubdivisionService.startSubdivisionPatrolling(maybeBattle.getDefenderSubdivisionId(), battleId);
        enemySubdivisionService.startSubdivisionMoving(maybeBattle.getAttackSubdivisionId(), battleId);

        battleRepository.save(maybeBattle);
    }

    public void stopBattle(Integer battleId, WinnerDto winner) {

        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);

        if (!winner.getWinner().equals(AFC)) {
            gunSubdivisionService.setGunsDeadStatus(maybeBattle.getDefenderSubdivisionId());
        }

        maybeBattle.setIsOver(true);
        maybeBattle.setEndAt(LocalDateTime.now(clock));
        battleRepository.save(maybeBattle);

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

            if (unit.getUnitType().equals(TANK) && unit.getProtectionLevel() <= unit.getTakenDamage()) {
                unit.setUnitState(DEAD);

                SetUnitStateDto unitStateDto = new SetUnitStateDto();
                unitStateDto.setUnitId(unit.getUnitId());
                unitStateDto.setUnitType(unit.getUnitType());
                unitStateDto.setUnitState(unit.getUnitState());

                enemySubdivisionService.setEnemyIsDeadStatus(unitStateDto);
            }

            if (unit.getUnitType().equals(UnitType.AFC) && unit.getProtectionLevel() <= unit.getTakenDamage()) {
                unit.setUnitState(DEAD);
            }

            if (unit.getUnitType().equals(UnitType.INFANTRY) && (unit.getProtectionLevel() * 0.7) <= unit.getTakenDamage()) {
                unit.setUnitState(DEAD);

                SetUnitStateDto unitStateDto = new SetUnitStateDto();
                unitStateDto.setUnitId(unit.getUnitId());
                unitStateDto.setUnitType(unit.getUnitType());
                unitStateDto.setUnitState(unit.getUnitState());

                enemySubdivisionService.setEnemyIsDeadStatus(unitStateDto);
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

    public void updateUnitState(Integer battleId, SetUnitStateDto dto) {
        Battle maybeBattle = battleRepository.findById(battleId).orElseThrow(NotFoundException::new);
        Set<UnitData> units = maybeBattle.getUnits();

        UnitData maybeUnitData = findUnitByUnitIdAndType(units, dto.getUnitId(), dto.getUnitType()).orElseThrow(NotFoundException::new);

        maybeUnitData.setUnitState(dto.getUnitState());

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
            unitDto.setUnitState(unitData.getUnitState());
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
