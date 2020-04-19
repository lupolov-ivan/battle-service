package battle.service.service;

import battle.service.entity.UnitType;
import battle.service.repository.UnitDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitDataService {

    private final UnitDataRepository unitDataRepository;

    public Integer countAllByBattleIdAndUnitTypeAndIsAliveFalse(Integer battleId, UnitType unitType) {
        return unitDataRepository.countAllByBattleIdAndUnitTypeAndIsAliveFalse(battleId, unitType);
    }
}
