package battle.service.service;

import battle.service.dto.UnitDto;
import battle.service.entity.UnitData;
import battle.service.repository.GunSubdivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GunSubdivisionService {

    private final GunSubdivisionRepository gunSubdivisionRepository;

    public List<UnitData> getUnitsDataBySubdivisionId(Integer id) {
        List<UnitDto> unitDtoList = gunSubdivisionRepository.getUnitDtoListBySubdivisionId(id);
        List<UnitData> unitDataList = new ArrayList<>();

        unitDtoList.forEach(dto -> {
            UnitData data = new UnitData();

            data.setUnitId(dto.getUnitId());
            data.setPosX(dto.getPosX());
            data.setPosY(dto.getPosY());
            data.setProtectionLevel(dto.getProtectionLevel());
            data.setUnitType(dto.getUnitType());
            data.setIsAlive(dto.getIsAlive());
            data.setTakenDamage(0.0);

            unitDataList.add(data);
        });

        return unitDataList;
    }

    public void startSubdivisionPatrolling(Integer subdivisionId, Integer battleId) {

        gunSubdivisionRepository.startSubdivisionPatrolling(subdivisionId, battleId);
    }
}
