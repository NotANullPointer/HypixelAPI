package com.bhonnso.hypixelapi.games.skyblock.profile.skills;


import static com.bhonnso.hypixelapi.games.skyblock.profile.skills.SkillType.LevelType.*;

public enum SkillType {

    FARMING(NORMAL, false),
    MINING(NORMAL, false),
    COMBAT(NORMAL, false),
    FORAGING(NORMAL, false),
    FISHING(NORMAL, false),
    ENCHANTING(NORMAL, false),
    ALCHEMY(NORMAL, false),
    CARPENTRY(NORMAL, true),
    RUNECRAFTING(LevelType.RUNECRAFTING, true);

    enum LevelType {
        NORMAL,
        RUNECRAFTING
    }


    private LevelType levelType;
    private boolean isCosmetic;

    SkillType(LevelType levelType, boolean isCosmetic) {
        this.levelType = levelType;
        this.isCosmetic = isCosmetic;
    }

    public boolean isCosmetic() {
        return isCosmetic;
    }

    public LevelType getLevelType() {
        return levelType;
    }
}
