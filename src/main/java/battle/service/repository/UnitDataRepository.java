package battle.service.repository;

import battle.service.entity.UnitData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitDataRepository extends JpaRepository<UnitData, Integer> {

    Optional<UnitData> findByPosXAndPosY(Integer posX, Integer posY);

    Optional<UnitData> findByPosXAndPosYAndAndBattle_Id(Integer posX, Integer posY, Integer battleId);
}
