package battle.service.repository;

import battle.service.entity.UnitData;
import battle.service.entity.UnitState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UnitDataRepository extends JpaRepository<UnitData, Integer> {

    @Query(value = "SELECT COUNT(*) FROM unit_data " +
            "WHERE battle_id = ?1 " +
            "AND (unit_type = 'TANK' OR unit_type = 'INFANTRY')",
            nativeQuery = true)
    Integer countAllEnemiesByBattleId(Integer battleId);

    @Query(value = "SELECT COUNT(*) FROM unit_data " +
            "WHERE battle_id = ?1 " +
            "AND unit_type = 'AFC'",
            nativeQuery = true)
    Integer countAllGunsByBattleId(Integer battleId);

    @Query(value = "SELECT COUNT(*) FROM unit_data " +
                    "WHERE battle_id = ?1 " +
                    "AND unit_state = ?2 " +
                    "AND (unit_type = 'TANK' OR unit_type = 'INFANTRY')",
            nativeQuery = true)
    Integer countAllEnemiesByBattleIdAndUnitState(Integer battleId, UnitState state);

    @Query(value = "SELECT COUNT(*) FROM unit_data " +
            "WHERE battle_id = ?1 " +
            "AND unit_state = ?2 " +
            "AND unit_type = 'AFC'",
            nativeQuery = true)
    Integer countAllGunsByBattleIdAndUnitState(Integer battleId, UnitState state);
}
