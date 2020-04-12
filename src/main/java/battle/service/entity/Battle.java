package battle.service.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Battle {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer defenderSubdivisionId;
    private Integer attackSubdivisionId;
    @OneToMany(cascade = ALL, fetch = EAGER)
    @JoinColumn(name = "battle_id")
    private List<UnitData> units = new ArrayList<>();
    @OneToMany(cascade = ALL, fetch = EAGER)
    @JoinColumn(name = "battle_id")
    private List<Shot> shots = new ArrayList<>();
}
