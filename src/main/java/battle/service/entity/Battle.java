package battle.service.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Battle {

    public Battle(){}

    public Battle(Integer id, Integer defenderSubdivisionId, Integer attackSubdivisionId) {
        this.id = id;
        this.defenderSubdivisionId = defenderSubdivisionId;
        this.attackSubdivisionId = attackSubdivisionId;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "battle_id")
    private Integer id;
    private Integer defenderSubdivisionId;
    private Integer attackSubdivisionId;

    @OneToMany(cascade = ALL, fetch = EAGER)
    @JoinColumn(name = "battle_id")
    private Set<UnitData> units = new HashSet<>();

    @OneToMany(cascade = ALL, fetch = EAGER)
    @JoinColumn(name = "battle_id")
    private Set<Shot> shots = new HashSet<>();

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Boolean isOver;
}
