package battle.service.dto;

import battle.service.entity.UnitType;
import lombok.Data;

@Data
public class PositionUpdateDto {

    private Integer unitId;
    private UnitType unitType;
    private Integer newPosX;
    private Integer newPosY;
}
