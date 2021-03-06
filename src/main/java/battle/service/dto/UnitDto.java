package battle.service.dto;

import battle.service.entity.UnitState;
import battle.service.entity.UnitType;
import lombok.Data;

@Data
public class UnitDto {

     private Integer unitId;
     private Integer posX;
     private Integer posY;
     private Integer protectionLevel;
     private UnitType unitType;
     private UnitState unitState;
}
