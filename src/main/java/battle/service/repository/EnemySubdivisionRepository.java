package battle.service.repository;


import battle.service.dto.UnitDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
@Slf4j
public class EnemySubdivisionRepository {

    private final String template;
    private final RestTemplate restTemplate;

    public EnemySubdivisionRepository(@Value("${enemy-service-server.host}") String host,
                                    @Value("${enemy-service-server.port}") Integer port,
                                    RestTemplate restTemplate) {
        this.template = "http://"+ host +":"+ port;
        this.restTemplate = restTemplate;
    }

    public List<UnitDto> getUnitDtoListBySubdivisionId(Integer id) {

        String url = template +"/subdivisions/"+ id;

        ResponseEntity<UnitDto[]> response = restTemplate.getForEntity(url, UnitDto[].class);

        return Arrays.asList(response.getBody());
    }

    public void startSubdivisionMoving(Integer subdivisionId, Integer battleId) {
        String url = template +"/subdivisions/"+ subdivisionId +"/battle/"+ battleId +"/start";

        restTemplate.postForObject(url, HttpEntity.EMPTY, Void.class);
    }

    public void setEnemyDeadStatus(Integer enemyId) {
        String url = template + "/subdivisions/units/" + enemyId + "/dead";

        try {
            restTemplate.patchForObject(url, HttpEntity.EMPTY, Void.class);
        } catch (HttpClientErrorException ignored) {
            log.error("Unit with id '{}' to killing is not exist", enemyId);
        }
    }
}
