package battle.service.entity;

import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class UnitData {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Embedded
    private Unit unit;
    private Double takenDamage;
    private Boolean isAlive;
}
