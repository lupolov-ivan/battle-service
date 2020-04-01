package battle.service.controller;

import battle.service.entity.Battlefield;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("battlefield")
@RequiredArgsConstructor
public class BattlefieldController {

    private final Battlefield battlefield;

    @GetMapping("/info")
    public ResponseEntity<?> getParametersBattlefield() {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("width", battlefield.getWidth());
        parameters.put("length", battlefield.getLength());

        return ResponseEntity.ok(parameters);
    }

}
