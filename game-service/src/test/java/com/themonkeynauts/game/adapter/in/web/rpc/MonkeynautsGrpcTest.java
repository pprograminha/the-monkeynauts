package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautClassMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautRankMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InRoleMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InUserMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InWalletMapper;
import com.themonkeynauts.game.application.port.in.CreateMonkeynaut;
import com.themonkeynauts.game.application.port.in.CreateMonkeynautCommand;
import com.themonkeynauts.game.application.port.in.ListMonkeynaut;
import com.themonkeynauts.game.domain.MonkeynautAttributes;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.game.domain.MonkeynautEnergyAttribute;
import com.themonkeynauts.game.domain.MonkeynautLifeAttribute;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.game.domain.MonkeynautResistanceAttribute;
import com.themonkeynauts.game.domain.MonkeynautSkillAttribute;
import com.themonkeynauts.game.domain.MonkeynautSpeedAttribute;
import com.themonkeynauts.proto.common.messages.Monkeynaut;
import com.themonkeynauts.proto.common.messages.MonkeynautBonus;
import com.themonkeynauts.proto.common.messages.MonkeynautClass;
import com.themonkeynauts.proto.common.messages.MonkeynautRank;
import com.themonkeynauts.proto.service.monkeynauts.CreateMonkeynautRequest;
import com.themonkeynauts.proto.service.monkeynauts.CreateMonkeynautResponse;
import com.themonkeynauts.proto.service.monkeynauts.ListMonkeynautRequest;
import com.themonkeynauts.proto.service.monkeynauts.ListMonkeynautResponse;
import com.themonkeynauts.proto.service.monkeynauts.RandomMonkeynautGenerationRequest;
import com.themonkeynauts.proto.service.monkeynauts.SpecifiedMonkeynautGenerationRequest;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonkeynautsGrpcTest {

    private MonkeynautsGrpc monkeynautsGrpc;

    @Mock
    private CreateMonkeynaut createMonkeynaut;

    @Mock
    private ListMonkeynaut listMonkeynaut;

    @Mock
    private StreamObserver<CreateMonkeynautResponse> createResponseStreamObserver;

    @Mock
    private StreamObserver<ListMonkeynautResponse> listResponseStreamObserver;

    @BeforeEach
    public void setUp() {
        var userMapper = new InUserMapper(new InRoleMapper(), new InWalletMapper());
        monkeynautsGrpc = new MonkeynautsGrpc(createMonkeynaut, listMonkeynaut, new InMonkeynautMapper(new InMonkeynautClassMapper(), new InMonkeynautRankMapper(), userMapper));
    }

    @Test
    public void shouldCreateSpecifiedMonkeynaut() {
        var clazz = new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.ENGINEER);
        var rank = new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.SERGEANT);
        var command = new CreateMonkeynautCommand(clazz, rank);
        var id = UUID.randomUUID();
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(4));
        var createdMonkeynaut = new com.themonkeynauts.game.domain.Monkeynaut(id, "Firstname", "Lastname", clazz, rank, attributes);
        createdMonkeynaut.setNumber(1L);
        when(createMonkeynaut.create(command)).thenReturn(createdMonkeynaut);

        var specified = SpecifiedMonkeynautGenerationRequest.newBuilder().setClass_(MonkeynautClass.ENGINEER).setRank(MonkeynautRank.SERGEANT).build();
        var request = CreateMonkeynautRequest.newBuilder().setSpecified(specified).build();
        monkeynautsGrpc.create(request, createResponseStreamObserver);

        var proto = Monkeynaut.newBuilder()
                .setId(id.toString())
                .setNumber(1)
                .setFirstName("Firstname").setLastName("Lastname")
                .setClass_(MonkeynautClass.ENGINEER)
                .setRank(MonkeynautRank.SERGEANT)
                .setAttributes(com.themonkeynauts.proto.common.messages.MonkeynautAttributes.newBuilder().setSkill(62).setSpeed(50).setResistance(46).setLife(250).setCurrentEnergy(4).setMaxEnergy(4).build())
                .setBonus(MonkeynautBonus.newBuilder().setDescription("Mining Reward").setValue(5f).build())
                .build();
        verify(createResponseStreamObserver).onNext(CreateMonkeynautResponse.newBuilder().setMonkeynaut(proto).build());
        verify(createResponseStreamObserver).onCompleted();
    }

    @Test
    public void shouldCreateRandomMonkeynaut() {
        var id = UUID.randomUUID();
        var clazz = new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.SCIENTIST);
        var rank = new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.MAJOR);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(8));
        var createdMonkeynaut = new com.themonkeynauts.game.domain.Monkeynaut(id, "Firstname", "Lastname", clazz, rank, attributes);
        createdMonkeynaut.setNumber(1L);
        when(createMonkeynaut.createRandom()).thenReturn(createdMonkeynaut);

        var random = RandomMonkeynautGenerationRequest.newBuilder().build();
        var request = CreateMonkeynautRequest.newBuilder().setRandom(random).build();
        monkeynautsGrpc.create(request, createResponseStreamObserver);

        var proto = Monkeynaut.newBuilder()
                .setId(id.toString())
                .setNumber(1)
                .setFirstName("Firstname").setLastName("Lastname")
                .setClass_(MonkeynautClass.SCIENTIST)
                .setRank(MonkeynautRank.MAJOR)
                .setAttributes(com.themonkeynauts.proto.common.messages.MonkeynautAttributes.newBuilder().setSkill(87).setSpeed(90).setResistance(52).setLife(250).setCurrentEnergy(8).setMaxEnergy(8).build())
                .setBonus(MonkeynautBonus.newBuilder().setDescription("Exploration Reward").setValue(15f).build())
                .build();
        verify(createResponseStreamObserver).onNext(CreateMonkeynautResponse.newBuilder().setMonkeynaut(proto).build());
        verify(createResponseStreamObserver).onCompleted();
    }

    @Test
    public void shouldListAllMonkeynauts() {
        var monkeynaut1 = createMonkeynaut(1L, "Firstname 1", "Lastname 1");
        var monkeynaut2 = createMonkeynaut(2L, "Firstname 1", "Lastname 1");
        var monkeynauts = asList(monkeynaut1, monkeynaut2);
        when(listMonkeynaut.all()).thenReturn(monkeynauts);

        monkeynautsGrpc.list(ListMonkeynautRequest.newBuilder().build(), listResponseStreamObserver);

        var protoMonkeynaut1 = createProtoMonkeynaut(monkeynaut1.getId(), monkeynaut1.getNumber(), monkeynaut1.getFirstName(), monkeynaut1.getLastName(), monkeynaut1.getClazz(), monkeynaut1.getRank(), monkeynaut1.skill(), monkeynaut1.speed(), monkeynaut1.resistance());
        var protoMonkeynaut2 = createProtoMonkeynaut(monkeynaut2.getId(), monkeynaut2.getNumber(), monkeynaut2.getFirstName(), monkeynaut2.getLastName(), monkeynaut2.getClazz(), monkeynaut2.getRank(), monkeynaut2.skill(), monkeynaut2.speed(), monkeynaut2.resistance());
        var protoMonkeynauts = asList(protoMonkeynaut1, protoMonkeynaut2);
        verify(listResponseStreamObserver).onNext(ListMonkeynautResponse.newBuilder().addAllMonkeynauts(protoMonkeynauts).build());
        verify(listResponseStreamObserver).onCompleted();
    }

    private com.themonkeynauts.game.domain.Monkeynaut createMonkeynaut(Long number, String firstName, String lastName) {
        var id = UUID.randomUUID();
        var clazz = new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.SCIENTIST);
        var rank = new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.MAJOR);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(8));
        var monkeynaut = new com.themonkeynauts.game.domain.Monkeynaut(id, firstName, lastName, clazz, rank, attributes);
        monkeynaut.setNumber(number);
        return monkeynaut;
    }

    private Monkeynaut createProtoMonkeynaut(UUID id, Long number, String firstName, String lastName, com.themonkeynauts.game.domain.MonkeynautClass clazz, com.themonkeynauts.game.domain.MonkeynautRank rank, int skill, int speed, int resistance) {
        var bonus = clazz.isSoldier() ? rank.bountyHuntingMissionBonus()
                : clazz.isEngineer() ? rank.miningMissionBonus()
                : clazz.isScientist() ? rank.explorationMissionBonus()
                : 0f;
        return Monkeynaut.newBuilder()
                .setId(id.toString())
                .setNumber(number)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setClass_(MonkeynautClass.valueOf(clazz.getType().toString()))
                .setRank(MonkeynautRank.valueOf(rank.getType().toString()))
                .setAttributes(com.themonkeynauts.proto.common.messages.MonkeynautAttributes.newBuilder().setSkill(skill).setSpeed(speed).setResistance(resistance).setLife(250).setCurrentEnergy(8).setMaxEnergy(8).build())
                .setBonus(MonkeynautBonus.newBuilder().setDescription(clazz.getType().bonusDescription()).setValue(bonus).build())
                .build();
    }

}