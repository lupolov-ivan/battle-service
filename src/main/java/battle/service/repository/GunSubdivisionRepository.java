package battle.service.repository;

import battle.service.dto.UnitDto;
import battle.service.entity.UnitData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GunSubdivisionRepository {

    @Value("${gun-service-server.host}")
    private String host;
    @Value("${gun-service-server.port}")
    private Integer port;

    private String template = "http://"+ host +":"+ port;
    private final RestTemplate restTemplate;

    public List<UnitDto> getUnitDtoListBySubdivisionId(Integer id) {

        String url = template +"/subdivisions/"+ id;

        ResponseEntity<UnitDto[]> response = restTemplate.getForEntity(url, UnitDto[].class);

        return Arrays.asList(response.getBody());
    }

    public void startSubdivisionPatrolling(Integer id) {
        String url = template +"/subdivisions/"+ id +"/patrol";
    }
}
