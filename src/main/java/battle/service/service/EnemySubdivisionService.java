package battle.service.service;

import battle.service.dto.UnitDto;
import battle.service.entity.UnitData;
import battle.service.repository.EnemySubdivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnemySubdivisionService {

    private final EnemySubdivisionRepository enemySubdivisionRepository;

    public List<UnitData> getUnitsDataBySubdivisionId(Integer id) {
        List<UnitDto> unitDtoList = enemySubdivisionRepository.getUnitDtoListBySubdivisionId(id);
        List<UnitData> unitDataList = new ArrayList<>();

        unitDtoList.forEach(dto -> {
            UnitData data = new UnitData();

            data.setUnitId(dto.getUnitId());
            data.setPosX(dto.getPosX());
            data.setPosY(dto.getPosY());
            data.setProtectionLevel(dto.getProtectionLevel());
            data.setUnitType(dto.getUnitType());
            data.setUnitState(dto.getUnitState());
            data.setTakenDamage(0.0);

            unitDataList.add(data);
        });

        return unitDataList;
    }

    public void startSubdivisionMoving(Integer subdivisionId, Integer battleId) {

        enemySubdivisionRepository.startSubdivisionMoving(subdivisionId, battleId);
    }

    public void setEnemyIsDeadStatus(Integer enemyId) {
        enemySubdivisionRepository.setEnemyDeadStatus(enemyId);
    }
}
