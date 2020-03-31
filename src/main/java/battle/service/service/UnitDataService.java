package battle.service.service;

import battle.service.entity.UnitData;
import battle.service.exceptions.NotFoundException;
import battle.service.repository.UnitDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitDataService {

    private final UnitDataRepository unitDataRepository;

    public UnitData addUnitData(UnitData unitData) {
        return unitDataRepository.save(unitData);
    }

    public UnitData getUnitByCoordinate(Integer posX, Integer posY) {
        return unitDataRepository.getUnitDataByPosXAndPosY(posX, posY).orElseThrow(NotFoundException::new);
    }
}
