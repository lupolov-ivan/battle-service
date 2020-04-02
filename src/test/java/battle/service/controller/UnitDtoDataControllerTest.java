package battle.service.controller;

import battle.service.entity.UnitData;
import battle.service.entity.UnitType;
import battle.service.repository.UnitDataRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static battle.service.TestUtils.fromResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UnitDtoDataControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UnitDataRepository repository;

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void registerUnit_whenUnitIsValid_thenUnitSave() throws Exception {

        mockMvc.perform(post("/units")
                        .contentType("application/json")
                        .content(fromResource("unit/create_unit_x1_y1.json")))
                .andExpect(status().isCreated());
        assertThat(repository.findByPosXAndPosY(1,1)).isPresent();
    }

    @Test
    public void registerUnit_whenUnitIsNotValid_then400Status() throws Exception {

        mockMvc.perform(post("/units")
                .contentType("application/json")
                .content(fromResource("unit/create_not_valid_unit_x100_y100.json")))
                .andExpect(status().isBadRequest());
        assertThat(repository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void getUnit_whenUnitExist_thenReturnThisUnit() throws Exception {

        UnitData unitData =  repository.save(new UnitData(null, 2, 3, 10, UnitType.TANK, 0.0, true));

        mockMvc.perform(get("/units/x/{posy}/y/{posY}", unitData.getPosX(), unitData.getPosY()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posX", is(2)))
                .andExpect(jsonPath("$.posY", is(3)))
                .andExpect(jsonPath("$.unitType", is(UnitType.TANK.name())));
    }

    @Test
    public void getUnit_whenCoordinateIsEmpty_then404Status() throws Exception {

        mockMvc.perform(get("/units/x/{posy}/y/{posY}", 4, 4))
                .andExpect(status().isNotFound());
    }

    @Test
    public void setDamageUnit_whenUnitExist_thenDamageIncrease() throws Exception {

        repository.save(new UnitData(null, 1, 1, 10, UnitType.TANK, 0.0, true));

        mockMvc.perform(patch("/units/damage")
                        .contentType("application/json")
                        .content(fromResource("unit/unit_damage_x1_y1.json")))
                .andExpect(status().isNoContent());

        Optional<UnitData> actual = repository.findByPosXAndPosY(1,1);
        assertThat(actual).isPresent();
        assertThat(actual.get().getTakenDamage()).isEqualTo(5);
    }

    @Test
    public void setDamageUnit_whenDamageIsMoreProtectionLevel_thenUnitIsDead() throws Exception {

        repository.save(new UnitData(null, 1, 1, 5, UnitType.TANK, 0.0, true));

        mockMvc.perform(patch("/units/damage")
                .contentType("application/json")
                .content(fromResource("unit/unit_damage_x1_y1.json")))
                .andExpect(status().isNoContent());

        Optional<UnitData> actual = repository.findByPosXAndPosY(1,1);
        assertThat(actual).isPresent();
        assertThat(actual.get().getTakenDamage()).isEqualTo(5);
        assertFalse(actual.get().getIsAlive());
    }

    @Test
    public void setDamageUnit_whenCoordinateIsEmpty_then404Status() throws Exception {

        mockMvc.perform(patch("/units/damage")
                .contentType("application/json")
                .content(fromResource("unit/unit_damage_x1_y1.json")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePositionUnit_whenUnitExist_thenUnitGetNewPosition() throws Exception {

        repository.save(new UnitData(null, 1, 2, 5, UnitType.TANK, 0.0, true));

        mockMvc.perform(patch("/units/position/update")
                .contentType("application/json")
                .content(fromResource("unit/update_position_x1_y2_to_x3_y4.json")))
                .andExpect(status().isNoContent());

        Optional<UnitData> old = repository.findByPosXAndPosY(1,2);
        assertThat(old).isEmpty();

        Optional<UnitData> actual = repository.findByPosXAndPosY(3,4);
        assertThat(actual).isPresent();
    }

    @Test
    public void updatePositionUnit_whenCoordinateIsEmpty_then404Status() throws Exception {

        mockMvc.perform(patch("/units/update/position")
                .contentType("application/json")
                .content(fromResource("unit/update_position_x1_y2_to_x3_y4.json")))
                .andExpect(status().isNotFound());
    }
}