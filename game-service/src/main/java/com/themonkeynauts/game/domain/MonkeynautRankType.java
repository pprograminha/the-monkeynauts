package com.themonkeynauts.game.domain;

public enum MonkeynautRankType {

    PRIVATE(45, 0f, 0.2f, 2, 0f, 0f, 0f),
    SERGEANT(35, 0.25f, 0.3f, 4, 5f, 5f, 5f),
    CAPTAIN(17, 0.5f, 0.4f, 6, 10f, 10f, 10f),
    MAJOR(3, 0.75f, 0.5f, 8, 15f, 15f, 15f);

    private final int chance;
    private final float rankBonus;
    private final float classBonus;
    private final int baseEnergy;
    private final float bountyHuntingMissionBonus;
    private final float miningMissionBonus;
    private final float explorationMissionBonus;

    MonkeynautRankType(int chance, float rankBonus, float classBonus, int baseEnergy, float bountyHuntingMissionBonus, float miningMissionBonus, float explorationMissionBonus) {
        this.chance = chance;
        this.rankBonus = rankBonus;
        this.classBonus = classBonus;
        this.baseEnergy = baseEnergy;
        this.bountyHuntingMissionBonus = bountyHuntingMissionBonus;
        this.miningMissionBonus = miningMissionBonus;
        this.explorationMissionBonus = explorationMissionBonus;
    }

    public int chance() {
        return this.chance;
    }

    public float rankBonus() {
        return this.rankBonus;
    }

    public float classBonus() {
        return this.classBonus;
    }

    public int baseEnergy() {
        return this.baseEnergy;
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
