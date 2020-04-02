package battle.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Unit {

     private Integer posX;
     private Integer posY;
     private Integer protectionLevel;

     @Enumerated(EnumType.STRING)
     private UnitType unitType;
}
