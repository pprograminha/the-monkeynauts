package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautClassEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautRankEnum;
import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.game.domain.MonkeynautAttributes;
import com.themonkeynauts.game.domain.MonkeynautClass;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.game.domain.MonkeynautEnergyAttribute;
import com.themonkeynauts.game.domain.MonkeynautLifeAttribute;
import com.themonkeynauts.game.domain.MonkeynautRank;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.game.domain.MonkeynautResistanceAttribute;
import com.themonkeynauts.game.domain.MonkeynautSkillAttribute;
import com.themonkeynauts.game.domain.MonkeynautSpeedAttribute;
import lombok.RequiredArgsConstructor;

@MapperAdapter
@RequiredArgsConstructor
public class OutMonkeynautMapper {

    private final OutUserMapper userMapper;

    public MonkeynautEntity toEntity(Monkeynaut domain) {
        var clazz = domain.getClazz().isEngineer() ? MonkeynautClassEnum.ENGINEER
                : domain.getClazz().isScientist() ? MonkeynautClassEnum.SCIENTIST
                : MonkeynautClassEnum.SOLDIER;
        var rank = domain.getRank().isPrivate() ? MonkeynautRankEnum.PRIVATE
                : domain.getRank().isSergeant() ? MonkeynautRankEnum.SERGEANT
                : domain.getRank().isCaptain() ? MonkeynautRankEnum.CAPTAIN
                : MonkeynautRankEnum.MAJOR;
        return MonkeynautEntity.builder()
                .id(domain.getId())
                .number(domain.getNumber())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .clazz(clazz)
                .rank(rank)
                .skill(domain.getAttributes().getSkill().getValue())
                .speed(domain.getAttributes().getSpeed().getValue())
                .resistance(domain.getAttributes().getResistance().getValue())
                .life(domain.getAttributes().getLife().getValue())
                .currentEnergy(domain.getAttributes().getCurrentEnergy().getValue())
                .maxEnergy(domain.maxEnergy())
                .shipId(domain.getShipId())
                .user(domain.getPlayer() != null ? userMapper.toEntity(domain.getPlayer()) : null)
                .operator(domain.getOperator() != null ? userMapper.toEntity(domain.getOperator()) : null)
                .build();
    }

    public Monkeynaut toDomain(MonkeynautEntity entity) {
        var classType = MonkeynautClassEnum.ENGINEER.equals(entity.getClazz()) ? MonkeynautClassType.ENGINEER
                : MonkeynautClassEnum.SCIENTIST.equals(entity.getClazz()) ? MonkeynautClassType.SCIENTIST
                : MonkeynautClassType.SOLDIER;
        var rankType = MonkeynautRankEnum.MAJOR.equals(entity.getRank()) ? MonkeynautRankType.MAJOR
                : MonkeynautRankEnum.CAPTAIN.equals(entity.getRank()) ? MonkeynautRankType.CAPTAIN
                : MonkeynautRankEnum.SERGEANT.equals(entity.getRank()) ? MonkeynautRankType.SERGEANT
                : MonkeynautRankType.PRIVATE;
        var skill = new MonkeynautSkillAttribute(entity.getSkill());
        var speed = new MonkeynautSpeedAttribute(entity.getSpeed());
        var resistance = new MonkeynautResistanceAttribute(entity.getResistance());
        var life = new MonkeynautLifeAttribute(entity.getLife());
        var currentEnergy = new MonkeynautEnergyAttribute(entity.getCurrentEnergy());
        var attributes = new MonkeynautAttributes(skill, speed, resistance, life, currentEnergy);
        var monkeynaut = new Monkeynaut(entity.getId(), entity.getFirstName(), entity.getLastName(), new MonkeynautClass(classType), new MonkeynautRank(rankType), attributes);
        monkeynaut.setNumber(entity.getNumber());
        monkeynaut.setPlayer(userMapper.toDomain(entity.getUser()));
        monkeynaut.setOperator(userMapper.toDomain(entity.getOperator()));
        monkeynaut.setShipId(entity.getShipId());
        return monkeynaut;
    }

    public OutUserMapper getUserMapper() {
        return this.userMapper;
    }
}
