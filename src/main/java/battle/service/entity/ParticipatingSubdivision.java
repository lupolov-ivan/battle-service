package battle.service.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class ParticipatingSubdivision {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer subdivisionId;
    private UnitType subdivisionType;
}
