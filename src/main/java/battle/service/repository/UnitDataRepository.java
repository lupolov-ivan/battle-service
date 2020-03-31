package battle.service.repository;

import battle.service.entity.UnitData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitDataRepository extends JpaRepository<UnitData, Integer> {

    Optional<UnitData> getUnitDataByPosXAndPosY(Integer posX, Integer posY);

}
