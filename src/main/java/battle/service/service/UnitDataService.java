package battle.service.service;

import battle.service.dto.PositionUpdateDto;
import battle.service.dto.UnitDamageDto;
import battle.service.entity.Battlefield;
import battle.service.dto.UnitDto;
import battle.service.entity.UnitData;
import battle.service.entity.UnitType;
import battle.service.exceptions.NotFoundException;
import battle.service.exceptions.UnitOutsideBattlefieldException;
import battle.service.repository.UnitDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitDataService {

    private final Battlefield battlefield;
    private final UnitDataRepository unitDataRepository;

    public UnitData registerUnit(UnitDto unitDto) {

        int posX = unitDto.getPosX();
        int posY = unitDto.getPosY();

        int width = battlefield.getWidth();
        int length = battlefield.getLength();

        if(posX < 0 || posX > width -1 || posY < 0 || posY > length -1) {
            throw new UnitOutsideBattlefieldException();
        }

        UnitData unitData = new UnitData();

        unitData.setPosX(unitDto.getPosX());
        unitData.setPosY(unitDto.getPosY());
        unitData.setProtectionLevel(unitData.getProtectionLevel());
        unitData.setUnitType(unitDto.getUnitType());
        unitData.setIsAlive(unitData.getIsAlive());
        unitData.setTakenDamage(0.0);

        return unitDataRepository.save(unitData);
    }

    public UnitDto getUnitByCoordinate(Integer posX, Integer posY) {
        UnitData maybeUnitData = unitDataRepository.findByPosXAndPosY(posX, posY).orElseThrow(NotFoundException::new);
        UnitDto unitDto = new UnitDto();

        unitDto.setPosX(maybeUnitData.getPosX());
        unitDto.setPosY(maybeUnitData.getPosY());
        unitDto.setProtectionLevel(maybeUnitData.getProtectionLevel());
        unitDto.setUnitType(maybeUnitData.getUnitType());
        unitDto.setIsAlive(maybeUnitData.getIsAlive());

        return unitDto;
    }

    public void setDamageUnit(UnitDamageDto unitDamageDto) {

        UnitData maybeUnitData = unitDataRepository
                .findByPosXAndPosY(unitDamageDto.getPosX(), unitDamageDto.getPosY())
                .orElseThrow(NotFoundException::new);

        double currentDamage = maybeUnitData.getTakenDamage();
        maybeUnitData.setTakenDamage(currentDamage + unitDamageDto.getDamage());

        if ((maybeUnitData.getUnitType().equals(UnitType.TANK) || maybeUnitData.getUnitType().equals(UnitType.AFC)) && maybeUnitData.getProtectionLevel() <= maybeUnitData.getTakenDamage()) {
            maybeUnitData.setIsAlive(false);
        }

        if (maybeUnitData.getUnitType().equals(UnitType.INFANTRY) && (maybeUnitData.getProtectionLevel()*0.7) <= maybeUnitData.getTakenDamage()) {
            maybeUnitData.setIsAlive(false);
        }

        unitDataRepository.save(maybeUnitData);
    }

    public void updateUnitPosition(PositionUpdateDto positionUpdateDto) {
        UnitData maybeUnitData = unitDataRepository
                .findByPosXAndPosY(positionUpdateDto.getPosX(), positionUpdateDto.getPosY())
                .orElseThrow(NotFoundException::new);

        maybeUnitData.setPosX(positionUpdateDto.getNewPosX());
        maybeUnitData.setPosY(positionUpdateDto.getNewPosY());

        unitDataRepository.save(maybeUnitData);
    }
}
