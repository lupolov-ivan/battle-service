package battle.service.service;

import battle.service.entity.UnitState;
import battle.service.repository.UnitDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitDataService {

    private final UnitDataRepository unitDataRepository;

    public Integer countAllEnemiesByBattleId(Integer battleId) {
        return unitDataRepository.countAllEnemiesByBattleId(battleId);
    }
    public Integer countAllGunsByBattleId(Integer battleId) {
        return unitDataRepository.countAllGunsByBattleId(battleId);
    }

    public Integer countAllEnemiesByBattleIdAndUnitState(Integer battleId, UnitState state) {
        return unitDataRepository.countAllEnemiesByBattleIdAndUnitState(battleId, state.name());
    }
    public Integer countAllGunsByBattleIdAndUnitState(Integer battleId, UnitState state) {
        return unitDataRepository.countAllGunsByBattleIdAndUnitState(battleId, state.name());
    }
}
