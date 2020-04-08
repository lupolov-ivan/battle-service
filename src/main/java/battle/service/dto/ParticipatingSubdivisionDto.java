package battle.service.dto;

import battle.service.entity.UnitType;
import lombok.Data;

@Data
public class ParticipatingSubdivisionDto {
    private Integer subdivisionId;
    private UnitType subdivisionType;
}
