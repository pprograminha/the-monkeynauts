package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.exception.ShipRequiredAttributeException;
import com.themonkeynauts.game.domain.ShipRankType;
import com.themonkeynauts.proto.common.messages.ShipRank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InShipRankMapperTest {

    private InShipRankMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new InShipRankMapper();
    }

    @Test
    public void shouldConvertToRankBDomain() {
        var rank = ShipRank.B;

        var result = mapper.toDomain(rank);

        assertThat(result).isEqualTo(new com.themonkeynauts.game.domain.ShipRank(ShipRankType.B));
    }

    @Test
    public void shouldConvertToRankADomain() {
        var rank = ShipRank.A;

        var result = mapper.toDomain(rank);

        assertThat(result).isEqualTo(new com.themonkeynauts.game.domain.ShipRank(ShipRankType.A));
    }

    @Test
    public void shouldConvertToRankSDomain() {
        var rank = ShipRank.S;

        var result = mapper.toDomain(rank);

        assertThat(result).isEqualTo(new com.themonkeynauts.game.domain.ShipRank(ShipRankType.S));
    }

    @Test
    public void shouldNotConvertUnspecifiedDomain() {
        var clazz = ShipRank.SHIP_RANK_UNSPECIFIED;

        assertThatThrownBy(() -> mapper.toDomain(clazz))
                .isInstanceOf(ShipRequiredAttributeException.class)
                .hasMessage("validation.required.ship");
    }

    @Test
    public void shouldConvertToRankBProto() {
        var rank = new com.themonkeynauts.game.domain.ShipRank(ShipRankType.B);

        var result = mapper.toProto(rank);

        assertThat(result).isEqualTo(ShipRank.B);
    }

    @Test
    public void shouldConvertToRankAProto() {
        var rank = new com.themonkeynauts.game.domain.ShipRank(ShipRankType.A);

        var result = mapper.toProto(rank);

        assertThat(result).isEqualTo(ShipRank.A);
    }

    @Test
    public void shouldConvertToRankSProto() {
        var rank = new com.themonkeynauts.game.domain.ShipRank(ShipRankType.S);

        var result = mapper.toProto(rank);

        assertThat(result).isEqualTo(ShipRank.S);
    }

}