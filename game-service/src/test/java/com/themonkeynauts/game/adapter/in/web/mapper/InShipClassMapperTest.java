package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.exception.ShipRequiredAttributeException;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.proto.common.messages.ShipClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InShipClassMapperTest {

    private InShipClassMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new InShipClassMapper();
    }

    @Test
    public void shouldConvertFighterDomain() {
        var clazz = ShipClass.FIGHTER;

        var result = mapper.toDomain(clazz);

        assertThat(result).isEqualTo(new com.themonkeynauts.game.domain.ShipClass(ShipClassType.FIGHTER));
    }

    @Test
    public void shouldConvertMinerDomain() {
        var clazz = ShipClass.MINER;

        var result = mapper.toDomain(clazz);

        assertThat(result).isEqualTo(new com.themonkeynauts.game.domain.ShipClass(ShipClassType.MINER));
    }

    @Test
    public void shouldConvertExplorerDomain() {
        var clazz = ShipClass.EXPLORER;

        var result = mapper.toDomain(clazz);

        assertThat(result).isEqualTo(new com.themonkeynauts.game.domain.ShipClass(ShipClassType.EXPLORER));
    }

    @Test
    public void shouldNotConvertUnspecifiedDomain() {
        var clazz = ShipClass.SHIP_CLASS_UNSPECIFIED;

        assertThatThrownBy(() -> mapper.toDomain(clazz))
                .isInstanceOf(ShipRequiredAttributeException.class)
                .hasMessage("validation.required.ship");
    }

    @Test
    public void shouldConvertFighterProto() {
        var clazz = new com.themonkeynauts.game.domain.ShipClass(ShipClassType.FIGHTER);

        var result = mapper.toProto(clazz);

        assertThat(result).isEqualTo(ShipClass.FIGHTER);
    }

    @Test
    public void shouldConvertMinerProto() {
        var clazz = new com.themonkeynauts.game.domain.ShipClass(ShipClassType.MINER);

        var result = mapper.toProto(clazz);

        assertThat(result).isEqualTo(ShipClass.MINER);
    }

    @Test
    public void shouldConvertExplorerProto() {
        var clazz = new com.themonkeynauts.game.domain.ShipClass(ShipClassType.EXPLORER);

        var result = mapper.toProto(clazz);

        assertThat(result).isEqualTo(ShipClass.EXPLORER);
    }

}