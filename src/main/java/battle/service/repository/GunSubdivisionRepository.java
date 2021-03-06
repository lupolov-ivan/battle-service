package battle.service.repository;

import battle.service.dto.UnitDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
public class GunSubdivisionRepository {

    private final String template;
    private final RestTemplate restTemplate;

    public GunSubdivisionRepository(@Value("${gun-service-server.host}") String host,
                                    @Value("${gun-service-server.port}") Integer port,
                                    RestTemplate restTemplate) {
        this.template = "http://"+ host +":"+ port;
        this.restTemplate = restTemplate;
    }

    public List<UnitDto> getUnitDtoListBySubdivisionId(Integer id) {

        String url = template +"/subdivisions/"+ id + "/guns";

        ResponseEntity<UnitDto[]> response = restTemplate.getForEntity(url, UnitDto[].class);

        return Arrays.asList(response.getBody());
    }

    public void startSubdivisionPatrolling(Integer subdivisionId, Integer battleId) {
        String url = template +"/subdivisions/"+ subdivisionId +"/battle/"+ battleId +"/start";

        restTemplate.postForObject(url, HttpEntity.EMPTY, Void.class);
    }

    public void setGunsDeadStatus(Integer subdivisionId) {
        String url = template + "/subdivisions/"+ subdivisionId +"/units/state/dead";

        try {
            restTemplate.patchForObject(url, HttpEntity.EMPTY, Void.class);
        } catch (HttpClientErrorException ignored) {
        }
    }
}
