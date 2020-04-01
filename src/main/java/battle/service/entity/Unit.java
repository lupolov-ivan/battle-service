package battle.service.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Embeddable
public class Unit {

     private Integer posX;
     private Integer posY;
     private Integer protectionLevel;

     @Enumerated(EnumType.STRING)
     private UnitType unitType;
}
