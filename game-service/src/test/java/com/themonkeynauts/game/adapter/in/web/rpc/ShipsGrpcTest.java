package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautClassMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautRankMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InRoleMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InShipClassMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InShipMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InShipRankMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InUserMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InWalletMapper;
import com.themonkeynauts.game.application.port.in.AddMonkeynautToCrew;
import com.themonkeynauts.game.application.port.in.AddMonkeynautToCrewCommand;
import com.themonkeynauts.game.application.port.in.CreateShip;
import com.themonkeynauts.game.application.port.in.CreateShipCommand;
import com.themonkeynauts.game.application.port.in.FetchShip;
import com.themonkeynauts.game.application.port.in.ListShip;
import com.themonkeynauts.game.application.port.in.RemoveMonkeynautFromCrew;
import com.themonkeynauts.game.application.port.in.RemoveMonkeynautFromCrewCommand;
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
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipDurabilityAttribute;
import com.themonkeynauts.game.domain.ShipRankType;
import com.themonkeynauts.proto.common.messages.MonkeynautBonus;
import com.themonkeynauts.proto.common.messages.Ship;
import com.themonkeynauts.proto.common.messages.ShipAttributes;
import com.themonkeynauts.proto.common.messages.ShipBonus;
import com.themonkeynauts.proto.common.messages.ShipClass;
import com.themonkeynauts.proto.common.messages.ShipCrew;
import com.themonkeynauts.proto.common.messages.ShipRank;
import com.themonkeynauts.proto.service.ships.AddMonkeynautToCrewRequest;
import com.themonkeynauts.proto.service.ships.AddMonkeynautToCrewResponse;
import com.themonkeynauts.proto.service.ships.CreateShipRequest;
import com.themonkeynauts.proto.service.ships.CreateShipResponse;
import com.themonkeynauts.proto.service.ships.GetShipRequest;
import com.themonkeynauts.proto.service.ships.GetShipResponse;
import com.themonkeynauts.proto.service.ships.ListShipRequest;
import com.themonkeynauts.proto.service.ships.ListShipResponse;
import com.themonkeynauts.proto.service.ships.RandomShipGenerationRequest;
import com.themonkeynauts.proto.service.ships.RemoveMonkeynautFromCrewRequest;
import com.themonkeynauts.proto.service.ships.RemoveMonkeynautFromCrewResponse;
import com.themonkeynauts.proto.service.ships.SpecifiedShipGenerationRequest;
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
class ShipsGrpcTest {

    private ShipsGrpc shipsGrpc;

    @Mock
    private CreateShip createShip;

    @Mock
    private ListShip listShip;

    @Mock
    private FetchShip fetchShip;

    @Mock
    private AddMonkeynautToCrew addMonkeynautToCrew;

    @Mock
    private RemoveMonkeynautFromCrew removeMonkeynautFromCrew;

    @Mock
    private StreamObserver<CreateShipResponse> createResponseStreamObserver;

    @Mock
    private StreamObserver<ListShipResponse> listResponseStreamObserver;

    @Mock
    private StreamObserver<GetShipResponse> getResponseStreamObserver;

    @Mock
    private StreamObserver<AddMonkeynautToCrewResponse> addCrewResponseStreamObserver;

    @Mock
    private StreamObserver<RemoveMonkeynautFromCrewResponse> removeCrewResponseStreamObserver;

    @BeforeEach
    public void setUp() {
        var userMapper = new InUserMapper(new InRoleMapper(), new InWalletMapper());
        var monkeynautMapper = new InMonkeynautMapper(new InMonkeynautClassMapper(), new InMonkeynautRankMapper(), userMapper);
        shipsGrpc = new ShipsGrpc(createShip, listShip, fetchShip, addMonkeynautToCrew, removeMonkeynautFromCrew, new InShipMapper(new InShipClassMapper(), new InShipRankMapper(), monkeynautMapper, userMapper));
    }

    @Test
    public void shouldCreateSpecifiedShip() {
        var clazz = new com.themonkeynauts.game.domain.ShipClass(ShipClassType.FIGHTER);
        var rank = new com.themonkeynauts.game.domain.ShipRank(ShipRankType.S);
        var command = new CreateShipCommand(clazz, rank);
        var id = UUID.randomUUID();
        var attributes = new com.themonkeynauts.game.domain.ShipAttributes(new ShipDurabilityAttribute(400));
        var createdShip = new com.themonkeynauts.game.domain.Ship(id, "Saturn V", clazz, rank, attributes);
        when(createShip.create(command)).thenReturn(createdShip);

        var specified = SpecifiedShipGenerationRequest.newBuilder().setClass_(ShipClass.FIGHTER).setRank(ShipRank.S).build();
        var request = CreateShipRequest.newBuilder().setSpecified(specified).build();
        shipsGrpc.create(request, createResponseStreamObserver);

        var proto = Ship.newBuilder()
                .setId(id.toString())
                .setName("Saturn V")
                .setClass_(ShipClass.FIGHTER)
                .setRank(ShipRank.S)
                .setAttributes(ShipAttributes.newBuilder().setMaxDurability(400).setCurrentDurability(400).build())
                .setBonus(ShipBonus.newBuilder().setDescription("Bounty Hunting Mission Reward").setValue(0.4f).build())
                .setCrew(ShipCrew.newBuilder().setSeats(4).build())
                .build();
        verify(createResponseStreamObserver).onNext(CreateShipResponse.newBuilder().setShip(proto).build());
        verify(createResponseStreamObserver).onCompleted();
    }

    @Test
    public void shouldCreateRandomShip() {
        var clazz = new com.themonkeynauts.game.domain.ShipClass(ShipClassType.MINER);
        var rank = new com.themonkeynauts.game.domain.ShipRank(ShipRankType.A);
        var command = new CreateShipCommand(clazz, rank);
        var id = UUID.randomUUID();
        var attributes = new com.themonkeynauts.game.domain.ShipAttributes(new ShipDurabilityAttribute(300));
        var createdShip = new com.themonkeynauts.game.domain.Ship(id, "Sombrero", clazz, rank, attributes);
        when(createShip.createRandom()).thenReturn(createdShip);

        var random = RandomShipGenerationRequest.newBuilder().build();
        var request = CreateShipRequest.newBuilder().setRandom(random).build();
        shipsGrpc.create(request, createResponseStreamObserver);

        var proto = Ship.newBuilder()
                .setId(id.toString())
                .setName("Sombrero")
                .setClass_(ShipClass.MINER)
                .setRank(ShipRank.A)
                .setAttributes(ShipAttributes.newBuilder().setMaxDurability(300).setCurrentDurability(300).build())
                .setBonus(ShipBonus.newBuilder().setDescription("Mining Mission Success Rate").setValue(30f).build())
                .setCrew(ShipCrew.newBuilder().setSeats(3).build())
                .build();
        verify(createResponseStreamObserver).onNext(CreateShipResponse.newBuilder().setShip(proto).build());
        verify(createResponseStreamObserver).onCompleted();
    }

    @Test
    public void shouldListAllShips() {
        var ship1 = createShip("Name 1");
        var ship2 = createShip("Name 2");
        var ships = asList(ship1, ship2);
        when(listShip.all()).thenReturn(ships);

        shipsGrpc.list(ListShipRequest.newBuilder().build(), listResponseStreamObserver);

        var protoShip1 = createProtoShip(ship1.getId(), ship1.getName(), ship1.getClazz(), ship1.getRank(), ship1.getAttributes().getCurrentDurability().getValue());
        var protoShip2 = createProtoShip(ship2.getId(), ship2.getName(), ship2.getClazz(), ship2.getRank(), ship2.getAttributes().getCurrentDurability().getValue());
        var protoShips = asList(protoShip1, protoShip2);
        verify(listResponseStreamObserver).onNext(ListShipResponse.newBuilder().addAllShips(protoShips).build());
        verify(listResponseStreamObserver).onCompleted();
    }

    @Test
    public void shouldFetchShipById() {
        var ship = createShip("Name 1");
        when(fetchShip.byId(ship.getId())).thenReturn(ship);

        var request = GetShipRequest.newBuilder().setShipId(ship.getId().toString()).build();
        shipsGrpc.get(request, getResponseStreamObserver);

        var protoShip = createProtoShip(ship.getId(), ship.getName(), ship.getClazz(), ship.getRank(), ship.getAttributes().getCurrentDurability().getValue());
        verify(getResponseStreamObserver).onNext(GetShipResponse.newBuilder().setShip(protoShip).build());
        verify(getResponseStreamObserver).onCompleted();
    }

    @Test
    public void shouldAddMonkeynautToCrewOfShip() {
        var shipId = UUID.randomUUID();
        var monkeynautId = UUID.randomUUID();
        var ship = createShip(shipId, "Name");
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(6));
        var monkeynaut = new Monkeynaut(monkeynautId, "First", "Last", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes);
        monkeynaut.setNumber(1L);
        ship.getCrew().addMonkeynaut(monkeynaut);
        when(fetchShip.byId(shipId)).thenReturn(ship);

        var request = AddMonkeynautToCrewRequest.newBuilder().setShipId(shipId.toString()).setMonkeynautId(monkeynautId.toString()).build();
        shipsGrpc.addMonkeynautToCrew(request, addCrewResponseStreamObserver);

        var protoMonkeynaut = com.themonkeynauts.proto.common.messages.Monkeynaut.newBuilder()
                .setId(monkeynautId.toString())
                .setNumber(1L)
                .setFirstName("First")
                .setLastName("Last")
                .setClass_(com.themonkeynauts.proto.common.messages.MonkeynautClass.ENGINEER)
                .setRank(com.themonkeynauts.proto.common.messages.MonkeynautRank.CAPTAIN)
                .setAttributes(com.themonkeynauts.proto.common.messages.MonkeynautAttributes.newBuilder()
                        .setSkill(75)
                        .setSpeed(75)
                        .setResistance(95)
                        .setLife(250)
                        .setMaxEnergy(6)
                        .setCurrentEnergy(6)
                        .build())
                .setShipId(shipId.toString())
                .setBonus(MonkeynautBonus.newBuilder().setDescription("Mining Reward").setValue(10f).build())
                .build();
        var protoShip = Ship.newBuilder()
                .setId(shipId.toString())
                .setName("Name")
                .setClass_(ShipClass.FIGHTER)
                .setRank(ShipRank.A)
                .setAttributes(ShipAttributes.newBuilder().setMaxDurability(300).setCurrentDurability(8).build())
                .setBonus(ShipBonus.newBuilder().setDescription("Bounty Hunting Mission Reward").setValue(0.35f).build())
                .setCrew(ShipCrew.newBuilder().setSeats(3).addMonkeynauts(protoMonkeynaut).build())
                .build();
        verify(addMonkeynautToCrew).ofShip(new AddMonkeynautToCrewCommand(shipId, monkeynautId));
        verify(addCrewResponseStreamObserver).onNext(AddMonkeynautToCrewResponse.newBuilder().setShip(protoShip).build());
        verify(addCrewResponseStreamObserver).onCompleted();
    }

    @Test
    public void shouldRemoveMonkeynautFromCrewOfShip() {
        var shipId = UUID.randomUUID();
        var monkeynautId = UUID.randomUUID();
        var ship = createShip(shipId, "Name");
        when(fetchShip.byId(shipId)).thenReturn(ship);

        var request = RemoveMonkeynautFromCrewRequest.newBuilder().setShipId(shipId.toString()).setMonkeynautId(monkeynautId.toString()).build();
        shipsGrpc.removeMonkeynautFromCrew(request, removeCrewResponseStreamObserver);

        var protoShip = Ship.newBuilder()
                .setId(shipId.toString())
                .setName("Name")
                .setClass_(ShipClass.FIGHTER)
                .setRank(ShipRank.A)
                .setAttributes(ShipAttributes.newBuilder().setMaxDurability(300).setCurrentDurability(8).build())
                .setBonus(ShipBonus.newBuilder().setDescription("Bounty Hunting Mission Reward").setValue(0.35f).build())
                .setCrew(ShipCrew.newBuilder().setSeats(3).build())
                .build();
        verify(removeMonkeynautFromCrew).ofShip(new RemoveMonkeynautFromCrewCommand(shipId, monkeynautId));
        verify(removeCrewResponseStreamObserver).onNext(RemoveMonkeynautFromCrewResponse.newBuilder().setShip(protoShip).build());
        verify(removeCrewResponseStreamObserver).onCompleted();
    }

    private com.themonkeynauts.game.domain.Ship createShip(String name) {
        var id = UUID.randomUUID();
        return createShip(id, name);
    }

    private com.themonkeynauts.game.domain.Ship createShip(UUID id, String name) {
        var clazz = new com.themonkeynauts.game.domain.ShipClass(ShipClassType.FIGHTER);
        var rank = new com.themonkeynauts.game.domain.ShipRank(ShipRankType.A);
        var attributes = new com.themonkeynauts.game.domain.ShipAttributes(new ShipDurabilityAttribute(8));
        var ship = new com.themonkeynauts.game.domain.Ship(id, name, clazz, rank, attributes);
        return ship;
    }

    private Ship createProtoShip(UUID id, String name, com.themonkeynauts.game.domain.ShipClass clazz, com.themonkeynauts.game.domain.ShipRank rank, int durability) {
        var bonus = clazz.isFighter() ? rank.bountyHuntingMissionBonus()
                : clazz.isMiner() ? rank.miningMissionBonus()
                : clazz.isExplorer() ? rank.explorationMissionBonus()
                : 0f;
        return Ship.newBuilder()
                .setId(id.toString())
                .setName(name)
                .setClass_(ShipClass.valueOf(clazz.getType().toString()))
                .setRank(ShipRank.valueOf(rank.getType().toString()))
                .setAttributes(ShipAttributes.newBuilder().setMaxDurability(rank.getType().tankCapacity()).setCurrentDurability(durability).build())
                .setBonus(ShipBonus.newBuilder().setDescription(clazz.getType().bonusDescription()).setValue(bonus).build())
                .setCrew(ShipCrew.newBuilder().setSeats(rank.seats()).build())
                .build();
    }

}