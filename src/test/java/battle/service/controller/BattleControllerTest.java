package battle.service.controller;

import battle.service.entity.Battle;
import battle.service.entity.UnitData;
import battle.service.repository.BattleRepository;
import battle.service.service.BattleService;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static battle.service.TestUtils.fromResource;
import static battle.service.entity.UnitState.*;
import static battle.service.entity.UnitType.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BattleControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    BattleService battleService;
    @Autowired
    BattleRepository battleRepository;

    @ClassRule
    public static WireMockRule wireMockRuleForEnemy = new WireMockRule(8089);

    @After
    public void cleanUp() {
        battleRepository.deleteAll();
    }

    @Test
    public void given_WhenCreateBattle_ThenFilledUnitData() throws Exception {
        stubFor(get("/subdivisions/1/guns")
        .willReturn(okJson(fromResource("controller/battle/get_units_from_subdivision_guns.json"))));

        stubFor(get("/subdivisions/1/enemies")
                .willReturn(okJson(fromResource("controller/battle/get_units_from_subdivision_enemies.json"))));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/battles")
                        .contentType("application/json")
                        .content(fromResource("controller/battle/create_battle.json")))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        int battleId = Integer.parseInt(response.getHeader("location").replace("http://localhost:8080/battles/", ""));

        verify(getRequestedFor(urlPathEqualTo("/subdivisions/1/guns")));
        verify(getRequestedFor(urlPathEqualTo("/subdivisions/1/enemies")));

        assertTrue(battleRepository.findById(battleId).isPresent());
    }

    @Test
    public void given_WhenGivenUnits_ThenMustReturnSetUnits() throws Exception {
        UnitData unitData1 = new UnitData(null, 1, 2, 6, 5, TANK, ACTIVE, 0.0);

        Battle battle = new Battle(null, 1, 1);
        battle.getUnits().add(unitData1);

        int battleId = battleRepository.save(battle).getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/battles/{battleId}/units", battleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].posY", is(6)))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void given_WhenUpdateEnemyPosition_ThenUnitMustDecrementPosY() throws Exception {
        UnitData unitData = new UnitData(null, 1, 2, 6, 5, TANK, ACTIVE, 0.0);

        Battle battle = new Battle(null, 1, 1);
        battle.getUnits().add(unitData);

        int battleId = battleRepository.save(battle).getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/battles/{battleId}/units/position/update", battleId)
                .contentType("application/json")
                .content(fromResource("controller/battle/position_update.json")))
                .andExpect(status().isNoContent());

        UnitData unit = battleRepository.findById(battleId).get().getUnits().iterator().next();
        assertEquals(5, (int) unit.getPosY());
    }

    @Test
    public void given_WhenUpdateStateState_ThenStateMustBeCritical() throws Exception {
        UnitData unitData = new UnitData(null, 1, 2, 6, 5, TANK, ACTIVE, 0.0);

        Battle battle = new Battle(null, 1, 1);
        battle.getUnits().add(unitData);

        int battleId = battleRepository.save(battle).getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/battles/{battleId}/units/state/update", battleId)
                .contentType("application/json")
                .content(fromResource("controller/battle/state_update.json")))
                .andExpect(status().isNoContent());

        UnitData unit = battleRepository.findById(battleId).get().getUnits().iterator().next();
        assertEquals(CRITICAL_DISTANCE_REACHED, unit.getUnitState());
    }

    @Test
    public void given_WhenUnitSetDamage_ThenUnitGetDamageAndGetStateDead() throws Exception {

        stubFor(patch(urlPathEqualTo("/subdivisions/units/state/update"))
                .withRequestBody(equalToJson(fromResource("controller/battle/set_dead_state.json")))
                .willReturn(aResponse()
                .withStatus(204)));

        UnitData unitData = new UnitData(null, 1, 2, 6, 1, TANK, ACTIVE, 0.0);

        Battle battle = new Battle(null, 1, 1);
        battle.getUnits().add(unitData);

        int battleId = battleRepository.save(battle).getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/battles/{battleId}/units/damage", battleId)
                .contentType("application/json")
                .content(fromResource("controller/battle/damage_enemy.json")))
                .andExpect(status().isNoContent());

        verify(patchRequestedFor(urlPathEqualTo("/subdivisions/units/state/update")));

        UnitData unit = battleRepository.findById(battleId).get().getUnits().iterator().next();
        assertEquals(1.5, unit.getTakenDamage(), 0.0);
    }
}