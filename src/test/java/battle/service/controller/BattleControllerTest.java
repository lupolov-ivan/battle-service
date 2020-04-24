package battle.service.controller;

import battle.service.TestUtils;
import battle.service.entity.Battle;
import battle.service.entity.UnitData;
import battle.service.entity.UnitState;
import battle.service.entity.UnitType;
import battle.service.exceptions.NotFoundException;
import battle.service.repository.BattleRepository;
import battle.service.service.BattleService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.After;
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


import static battle.service.TestUtils.fromResource;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;

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

    @Rule
    public WireMockRule wireMockRuleForEnemy = new WireMockRule(8089);

    @After
    public void cleanUp() {
        battleRepository.deleteAll();
    }

    @Test
    public void given_WhenCreateBattle_ThenFilledUnitData() throws Exception {
        WireMock.stubFor(WireMock.get("/subdivisions/1/guns")
        .willReturn(WireMock.okJson(fromResource("battlecontroller/get_units_from_subdivision_guns.json"))));

        WireMock.stubFor(WireMock.get("/subdivisions/1/enemies")
                .willReturn(WireMock.okJson(fromResource("battlecontroller/get_units_from_subdivision_enemies.json"))));

        mockMvc.perform(MockMvcRequestBuilders.post("/battles")
                        .contentType("application/json")
                        .content(fromResource("battlecontroller/create_battle.json")))
                .andExpect(status().isCreated());

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlPathEqualTo("/subdivisions/1/guns")));
        WireMock.verify(WireMock.getRequestedFor(WireMock.urlPathEqualTo("/subdivisions/1/enemies")));
    }

    @Test
    public void given_WhenGivenUnits_ThenMustReturnSetUnits() throws Exception {
        UnitData unitData1 = new UnitData(null, 1, 2, 6, 5, UnitType.TANK, UnitState.ACTIVE, 0.0);

        Battle battle = new Battle(null, 1, 1);
        battle.getUnits().add(unitData1);

        int battleId = battleRepository.save(battle).getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/battles/{battleId}/units", battleId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].posY", Matchers.is(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    public void given_WhenUpdateEnemyPosition_ThenUnitMustDecrementPosY() throws Exception {
        UnitData unitData = new UnitData(null, 1, 2, 6, 5, UnitType.TANK, UnitState.ACTIVE, 0.0);

        Battle battle = new Battle(null, 1, 1);
        battle.getUnits().add(unitData);

        int battleId = battleRepository.save(battle).getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/battles/{battleId}/units/position/update", battleId)
                .contentType("application/json")
                .content(TestUtils.fromResource("battlecontroller/position_update.json")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        UnitData unit = battleRepository.findById(battleId).get().getUnits().iterator().next();
        assertEquals(5, (int) unit.getPosY());
    }

    @Test
    public void given_WhenUpdateStateState_ThenStateMustBeCritical() throws Exception {
        UnitData unitData = new UnitData(null, 1, 2, 6, 5, UnitType.TANK, UnitState.ACTIVE, 0.0);

        Battle battle = new Battle(null, 1, 1);
        battle.getUnits().add(unitData);

        int battleId = battleRepository.save(battle).getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/battles/{battleId}/units/state/update", battleId)
                .contentType("application/json")
                .content(TestUtils.fromResource("battlecontroller/state_update.json")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        UnitData unit = battleRepository.findById(battleId).get().getUnits().iterator().next();
        assertTrue(unit.getUnitState().equals(UnitState.CRITICAL_DISTANCE_REACHED));
    }

    @Ignore
    @Test
    public void given_When_Then() throws Exception {
        UnitData unitData = new UnitData(null, 1, 2, 6, 1, UnitType.TANK, UnitState.ACTIVE, 0.0);

        Battle battle = new Battle(null, 1, 1);
        battle.getUnits().add(unitData);

        int battleId = battleRepository.save(battle).getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/battles/{battleId}/units/damage", battleId)
                .contentType("application/json")
                .content(TestUtils.fromResource("battlecontroller/damage_enemy.json")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        UnitData unit = battleRepository.findById(battleId).get().getUnits().iterator().next();
        assertTrue(unit.getTakenDamage() == 1.5);
//        /subdivisions/units/state/update

    }
}