package battle.service.entity;


import lombok.Data;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Data
@Entity
public class Shot {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer targetId;
    private UnitType targetType;
    private Double damage;
    @Enumerated(value = STRING)
    private ShotResult shotResult;
}
