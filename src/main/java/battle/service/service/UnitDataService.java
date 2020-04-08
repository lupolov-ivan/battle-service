package battle.service.service;

import battle.service.dto.PositionUpdateDto;
import battle.service.dto.UnitDamageDto;
import battle.service.dto.UnitDto;
import battle.service.entity.UnitData;
import battle.service.entity.UnitType;
import battle.service.exceptions.NotFoundException;
import battle.service.repository.UnitDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitDataService {

    private final UnitDataRepository unitDataRepository;

    public UnitDto getUnitByCoordinateAndBattleId(Integer posX, Integer posY, Integer battleId) {
        UnitData maybeUnitData = unitDataRepository.findByPosXAndPosYAndAndBattle_Id(posX, posY, battleId).orElseThrow(NotFoundException::new);

        UnitDto unitDto = new UnitDto();

        unitDto.setPosX(maybeUnitData.getPosX());
        unitDto.setPosY(maybeUnitData.getPosY());
        unitDto.setProtectionLevel(maybeUnitData.getProtectionLevel());
        unitDto.setUnitType(maybeUnitData.getUnitType());
        unitDto.setIsAlive(maybeUnitData.getIsAlive());

        return unitDto;
    }

    public void setDamageUnit(Integer battleId, UnitDamageDto dto) {

        UnitData maybeUnitData = unitDataRepository
                .findByPosXAndPosYAndAndBattle_Id(dto.getPosX(), dto.getPosY(), battleId)
                .orElseThrow(NotFoundException::new);

        double currentDamage = maybeUnitData.getTakenDamage();
        maybeUnitData.setTakenDamage(currentDamage + dto.getDamage());

        if ((maybeUnitData.getUnitType().equals(UnitType.TANK) || maybeUnitData.getUnitType().equals(UnitType.AFC)) && maybeUnitData.getProtectionLevel() <= maybeUnitData.getTakenDamage()) {
            maybeUnitData.setIsAlive(false);
        }

        if (maybeUnitData.getUnitType().equals(UnitType.INFANTRY) && (maybeUnitData.getProtectionLevel()*0.7) <= maybeUnitData.getTakenDamage()) {
            maybeUnitData.setIsAlive(false);
        }

        unitDataRepository.save(maybeUnitData);
    }

    public void updateUnitPosition(Integer battleId, PositionUpdateDto dto) {
        UnitData maybeUnitData = unitDataRepository
                .findByPosXAndPosYAndAndBattle_Id(dto.getPosX(), dto.getPosY(), battleId)
                .orElseThrow(NotFoundException::new);

        maybeUnitData.setPosX(dto.getNewPosX());
        maybeUnitData.setPosY(dto.getNewPosY());

        unitDataRepository.save(maybeUnitData);
    }
}
