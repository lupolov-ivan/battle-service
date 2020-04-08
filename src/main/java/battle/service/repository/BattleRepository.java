package battle.service.repository;

import battle.service.entity.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleRepository extends JpaRepository<Battle, Integer> {
}
