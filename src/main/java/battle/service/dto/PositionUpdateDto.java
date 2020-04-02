package battle.service.dto;

import lombok.Data;

@Data
public class PositionUpdateDto {

    private Integer posX;
    private Integer posY;
    private Integer newPosX;
    private Integer newPosY;
}
