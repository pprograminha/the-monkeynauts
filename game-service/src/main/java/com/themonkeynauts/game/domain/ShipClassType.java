package com.themonkeynauts.game.domain;

public enum ShipClassType {

    FIGHTER(33, "Bounty Hunting Mission Reward"),
    MINER(33, "Mining Mission Success Rate"),
    EXPLORER(34, "Exploration Mission Time");

    private final int chance;
    private final String bonusDescription;

    ShipClassType(int chance, String bonusDescription) {
        this.chance = chance;
        this.bonusDescription = bonusDescription;
    }

    public int chance() {
        return this.chance;
    }

    public String bonusDescription() {
        return this.bonusDescription;
    }
}
