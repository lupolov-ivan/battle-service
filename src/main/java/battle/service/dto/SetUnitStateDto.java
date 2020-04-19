package battle.service.dto;

import battle.service.entity.UnitState;
import battle.service.entity.UnitType;
import lombok.Data;

@Data
public class SetUnitStateDto {

    private Integer unitId;
    private UnitType unitType;
    private UnitState unitState;
}
