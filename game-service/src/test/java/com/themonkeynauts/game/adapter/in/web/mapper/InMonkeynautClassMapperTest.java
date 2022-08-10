package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.proto.common.messages.MonkeynautClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMonkeynautClassMapperTest {

    private InMonkeynautClassMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new InMonkeynautClassMapper();
    }

    @Test
    public void shouldConvertEngineerDomain() {
        var clazz = MonkeynautClass.ENGINEER;

        var result = mapper.toDomain(clazz);

        assertThat(result).isEqualTo(new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.ENGINEER));
    }

    @Test
    public void shouldConvertScientistDomain() {
        var clazz = MonkeynautClass.SCIENTIST;

        var result = mapper.toDomain(clazz);

        assertThat(result).isEqualTo(new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.SCIENTIST));
    }

    @Test
    public void shouldConvertSoldierDomain() {
        var clazz = MonkeynautClass.SOLDIER;

        var result = mapper.toDomain(clazz);

        assertThat(result).isEqualTo(new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.SOLDIER));
    }

    @Test
    public void shouldNotConvertUnspecifiedDomain() {
        var clazz = MonkeynautClass.MONKEYNAUT_CLASS_UNSPECIFIED;

        assertThatThrownBy(() -> mapper.toDomain(clazz))
                .isInstanceOf(MonkeynautRequiredAttributeException.class)
                .hasMessage("validation.required.monkeynaut");
    }

    @Test
    public void shouldConvertEngineerProto() {
        var clazz = new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.ENGINEER);

        var result = mapper.toProto(clazz);

        assertThat(result).isEqualTo(MonkeynautClass.ENGINEER);
    }

    @Test
    public void shouldConvertScientistProto() {
        var clazz = new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.SCIENTIST);

        var result = mapper.toProto(clazz);

        assertThat(result).isEqualTo(MonkeynautClass.SCIENTIST);
    }

    @Test
    public void shouldConvertSoldierProto() {
        var clazz = new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.SOLDIER);

        var result = mapper.toProto(clazz);

        assertThat(result).isEqualTo(MonkeynautClass.SOLDIER);
    }

}