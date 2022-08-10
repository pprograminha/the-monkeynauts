package com.themonkeynauts.game.domain;

public enum MonkeynautClassType {

    ENGINEER(30, "Mining Reward"),
    SCIENTIST(30, "Exploration Reward"),
    SOLDIER(40, "Bounty Hunting Reward");

    private final int chance;
    private final String bonusDescription;

    MonkeynautClassType(int chance, String bonusDescription) {
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
