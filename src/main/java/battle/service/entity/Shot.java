package battle.service.entity;


import lombok.Data;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Shot {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer targetId;
    @Enumerated(STRING)
    private UnitType targetType;
    private Double damage;
    @Enumerated(STRING)
    private ShotResult shotResult;
}
