package com.themonkeynauts.game.domain;

public enum ShipRankType {

    B(50, 2, 200, 0.3f, 20f, -20f),
    A(35, 3, 300, 0.35f, 30f, -35f),
    S(15, 4, 400, 0.4f, 40f, -50f);

    private final int chance;
    private final int seats;
    private final int tankCapacity;
    private final float bountyHuntingMissionBonus;
    private final float miningMissionBonus;
    private final float explorationMissionBonus;

    ShipRankType(int chance, int seats, int tankCapacity, float bountyHuntingMissionBonus, float miningMissionBonus, float explorationMissionBonus) {
        this.chance = chance;
        this.seats = seats;
        this.tankCapacity = tankCapacity;
        this.bountyHuntingMissionBonus = bountyHuntingMissionBonus;
        this.miningMissionBonus = miningMissionBonus;
        this.explorationMissionBonus = explorationMissionBonus;
    }

    public int chance() {
        return this.chance;
    }

    public int seats() {
        return this.seats;
    }

    public int tankCapacity() {
        return this.tankCapacity;
    }

    public float bountyHuntingMissionBonus() {
        return this.bountyHuntingMissionBonus;
    }

    public float miningMissionBonus() {
        return this.miningMissionBonus;
    }

    public float explorationMissionBonus() {
        return this.explorationMissionBonus;
    }
}
