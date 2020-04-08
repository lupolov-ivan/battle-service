package battle.service.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Battle {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer defenderSubdivisionId;
    private Integer attackSubdivisionId;
    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UnitData> units = new ArrayList<>();
}
