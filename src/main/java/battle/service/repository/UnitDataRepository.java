package battle.service.repository;

import battle.service.entity.UnitData;
import battle.service.entity.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UnitDataRepository extends JpaRepository<UnitData, Integer> {

    @Query(value = "SELECT COUNT(*) FROM unit_data WHERE battle_id = ?1 AND unit_type = ?2 AND is_alive = FALSE", nativeQuery = true)
    Integer countAllByBattleIdAndUnitTypeAndIsAliveFalse(Integer battleId, UnitType unitType);
}
