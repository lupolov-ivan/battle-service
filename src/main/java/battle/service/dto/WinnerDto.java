package battle.service.dto;

import battle.service.entity.UnitType;
import lombok.Data;

@Data
public class WinnerDto {
    private UnitType winner;
}
