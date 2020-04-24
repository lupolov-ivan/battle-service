package battle.service.repository;


import battle.service.dto.UnitStateDto;
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

        String url = template +"/subdivisions/"+ id + "/enemies";

        ResponseEntity<UnitDto[]> response = restTemplate.getForEntity(url, UnitDto[].class);

        return Arrays.asList(response.getBody());
    }

    public void startSubdivisionMoving(Integer subdivisionId, Integer battleId) {
        String url = template +"/subdivisions/"+ subdivisionId +"/battle/"+ battleId +"/start";

        restTemplate.postForObject(url, HttpEntity.EMPTY, Void.class);
    }

    public void setEnemyDeadStatus(UnitStateDto dto) {
        String url = template + "/subdivisions/units/state/update";

        HttpEntity<UnitStateDto> request = new HttpEntity<>(dto);

        try {
            restTemplate.patchForObject(url, request, Void.class);
        } catch (HttpClientErrorException ignored) { }
    }
}
