package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.proto.common.messages.MonkeynautRank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMonkeynautRankMapperTest {

    private InMonkeynautRankMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new InMonkeynautRankMapper();
    }

    @Test
    public void shouldConvertPrivateDomain() {
        var rank = MonkeynautRank.PRIVATE;

        var result = mapper.toDomain(rank);

        assertThat(result.isPrivate()).isTrue();
    }

    @Test
    public void shouldConvertSergeantDomain() {
        var rank = MonkeynautRank.SERGEANT;

        var result = mapper.toDomain(rank);

        assertThat(result.isSergeant()).isTrue();
    }

    @Test
    public void shouldConvertCaptainDomain() {
        var rank = MonkeynautRank.CAPTAIN;

        var result = mapper.toDomain(rank);

        assertThat(result.isCaptain()).isTrue();
    }

    @Test
    public void shouldConvertMajorDomain() {
        var rank = MonkeynautRank.MAJOR;

        var result = mapper.toDomain(rank);

        assertThat(result.isMajor()).isTrue();
    }

    @Test
    public void shouldNotConvertUnspecifiedDomain() {
        var rank = MonkeynautRank.MONKEYNAUT_RANK_UNSPECIFIED;

        assertThatThrownBy(() -> mapper.toDomain(rank))
        .isInstanceOf(MonkeynautRequiredAttributeException.class)
        .hasMessage("validation.required.monkeynaut");
    }

    @Test
    public void shouldConvertPrivateProto() {
        var rank = new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.PRIVATE);

        var result = mapper.toProto(rank);

        assertThat(result).isEqualTo(MonkeynautRank.PRIVATE);
    }

    @Test
    public void shouldConvertSergeantProto() {
        var rank = new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.SERGEANT);

        var result = mapper.toProto(rank);

        assertThat(result).isEqualTo(MonkeynautRank.SERGEANT);
    }

    @Test
    public void shouldConvertCaptainProto() {
        var rank = new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.CAPTAIN);

        var result = mapper.toProto(rank);

        assertThat(result).isEqualTo(MonkeynautRank.CAPTAIN);
    }

    @Test
    public void shouldConvertMajorProto() {
        var rank = new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.MAJOR);

        var result = mapper.toProto(rank);

        assertThat(result).isEqualTo(MonkeynautRank.MAJOR);
    }

}