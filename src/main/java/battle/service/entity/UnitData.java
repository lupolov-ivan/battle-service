package battle.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UnitData {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer unitId;
    private Integer posX;
    private Integer posY;
    private Integer protectionLevel;
    @Enumerated(EnumType.STRING)
    private UnitType unitType;
    private Double takenDamage;
    private Boolean isAlive;
}
