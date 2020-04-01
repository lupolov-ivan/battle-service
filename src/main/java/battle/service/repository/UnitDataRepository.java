package battle.service.repository;

import battle.service.entity.UnitData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitDataRepository extends JpaRepository<UnitData, Integer> {

    Optional<UnitData> findByUnit_PosXAndUnit_PosY(Integer posX, Integer posY);

}
