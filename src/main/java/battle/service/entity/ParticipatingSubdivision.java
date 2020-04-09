package battle.service.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class ParticipatingSubdivision {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer subdivisionId;
    @Enumerated(EnumType.STRING)
    private UnitType subdivisionType;
}
