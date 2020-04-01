package battle.service.service;

import battle.service.entity.Battlefield;
import battle.service.entity.Unit;
import battle.service.entity.UnitData;
import battle.service.entity.UnitType;
import battle.service.exceptions.NotFoundException;
import battle.service.exceptions.UnitOutsideBattlefieldException;
import battle.service.repository.UnitDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnitDataService {

    private final Battlefield battlefield;
    private final UnitDataRepository unitDataRepository;

    public UnitData registerUnit(Unit unit) {

        int posX = unit.getPosX();
        int posY = unit.getPosY();

        int width = battlefield.getWidth();
        int length = battlefield.getLength();

        if((posX < 0 && posX > width -1) || (posY < 0 && posY > length -1)) {
            throw new UnitOutsideBattlefieldException();
        }

        UnitData unitData = new UnitData();

        unitData.setUnit(unit);
        unitData.setTakenDamage(0.0);
        unitData.setIsAlive(true);

        return unitDataRepository.save(unitData);
    }

    public UnitData getUnitByCoordinate(Integer posX, Integer posY) {
        return unitDataRepository.findByUnit_PosXAndUnit_PosY(posX, posY).orElseThrow(NotFoundException::new);
    }

    public void setDamageUnit(Integer posX, Integer posY, Map<String, Object> updates) {

        UnitData maybeUnitData = unitDataRepository
                .findByUnit_PosXAndUnit_PosY(posX, posY)
                .orElseThrow(NotFoundException::new);

        Unit unit = maybeUnitData.getUnit();

        double currentDamage = maybeUnitData.getTakenDamage();
        maybeUnitData.setTakenDamage(currentDamage + (double) updates.get("damage"));

        if (unit.getUnitType().equals(UnitType.INFANTRY) && (unit.getProtectionLevel()*0.9) <= maybeUnitData.getTakenDamage()) {
            maybeUnitData.setIsAlive(false);
        }

        if (unit.getUnitType().equals(UnitType.TANK) && unit.getProtectionLevel() <= maybeUnitData.getTakenDamage()) {
            maybeUnitData.setIsAlive(false);
        }

        if (unit.getUnitType().equals(UnitType.INFANTRY) && (unit.getProtectionLevel()*0.7) <= maybeUnitData.getTakenDamage()) {
            maybeUnitData.setIsAlive(false);
        }

        unitDataRepository.save(maybeUnitData);
    }

    public void updateUnitPosition(Integer posX, Integer posY, Map<String, Object> updates) {
        UnitData maybeUnitData = unitDataRepository
                .findByUnit_PosXAndUnit_PosY(posX, posY)
                .orElseThrow(NotFoundException::new);

        Unit unit = maybeUnitData.getUnit();

        unit.setPosX((int) updates.get("newX"));
        unit.setPosY((int) updates.get("newY"));

        unitDataRepository.save(maybeUnitData);
    }
}
