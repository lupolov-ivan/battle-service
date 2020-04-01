package battle.service.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("battlefield")
public class Battlefield {

    private Integer width;
    private Integer length;
}
