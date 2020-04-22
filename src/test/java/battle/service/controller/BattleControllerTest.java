package battle.service.controller;

import battle.service.TestUtils;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static battle.service.TestUtils.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BattleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRuleForEnemy = new WireMockRule(8089);

    @Test
    public void given_WhenCreateBattle_ThenFilledUnitData() throws Exception {
        stubFor(get("/subdivisions/1/guns")
        .willReturn(okJson(fromResource("battlecontroller/get_units_from_subdivision_guns.json"))));

        stubFor(get("/subdivisions/1/enemies")
                .willReturn(okJson(fromResource("battlecontroller/get_units_from_subdivision_enemies.json"))));

        mockMvc.perform(MockMvcRequestBuilders.post("/battles")
                        .contentType("application/json")
                        .content(fromResource("battlecontroller/create_battle.json")))
                .andExpect(status().isCreated());

        verify(getRequestedFor(urlPathEqualTo("/subdivisions/1/guns")));
        verify(getRequestedFor(urlPathEqualTo("/subdivisions/1/enemies")));
    }
}