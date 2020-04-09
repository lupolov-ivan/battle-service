package battle.service.controller;

import battle.service.entity.Battlefield;
import battle.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource doesn't exist or has been deleted")
    public void handleNotFound() { }
}
