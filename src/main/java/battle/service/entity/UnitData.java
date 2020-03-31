package battle.service.entity;

import lombok.Data;

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
    private Integer posX;
    private Integer posY;
    private Integer protectionLevel;
    private Double takenDamage;
}
