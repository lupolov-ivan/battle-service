package battle.service.controller;

import battle.service.dto.BattleDto;
import battle.service.entity.Battle;
import battle.service.service.BattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/battles")
public class BattleController {

    private final BattleService battleService;

    @PostMapping
    public ResponseEntity<Battle> createBattle(@RequestBody BattleDto dto) {
        Battle battle = battleService.createBattle(dto);
        return ResponseEntity
                .created(URI.create("/battles/"+ battle.getId()))
                .build();
    }

    @PostMapping("{battleId}/start")
    public ResponseEntity<?> startBattle(@PathVariable Integer battleId) {
        battleService.startBattle(battleId);
        return ResponseEntity.ok().build();
    }

}
