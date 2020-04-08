package battle.service.service;

import battle.service.dto.BattleDto;
import battle.service.entity.Battle;
import battle.service.entity.UnitData;
import battle.service.entity.UnitType;
import battle.service.repository.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;
    private final GunSubdivisionService gunSubdivisionService;


    public Battle createBattle(BattleDto dto) {
        Battle battle = new Battle();
        battle.setDefenderSubdivisionId(dto.getDefenderSubdivisionId());
        battle.setAttackSubdivisionId(dto.getAttackSubdivisionId());


        battle.getUnits().addAll(Arrays.asList(
                new UnitData(null, 2, 48, 10, UnitType.TANK, 0.0, true, null),
                new UnitData(null, 2, 48, 10, UnitType.TANK, 0.0, true, null),
                new UnitData(null, 2, 48, 10, UnitType.TANK, 0.0, true, null),
                new UnitData(null, 2, 48, 10, UnitType.TANK, 0.0, true, null)
        )); // TODO: replace with a real enemy service call.

        battle.getUnits().addAll(gunSubdivisionService.getUnitsDataBySubdivisionId(dto.getDefenderSubdivisionId()));

        return battleRepository.save(battle);
    }

    public void startBattle(Integer id) {
        gunSubdivisionService.startSubdivisionPatrolling(id);
    }
}
