package com.bhonnso.hypixelapi.games.skyblock.profile.skills;


import com.sun.xml.internal.ws.util.StringUtils;

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


    private final LevelType levelType;
    private final boolean isCosmetic;

    SkillType(LevelType levelType, boolean isCosmetic) {
        this.levelType = levelType;
        this.isCosmetic = isCosmetic;
    }

    public String getDisplayName() {
        return StringUtils.capitalize(name().toLowerCase());
    }

    public boolean isCosmetic() {
        return isCosmetic;
    }

    public LevelType getLevelType() {
        return levelType;
    }
}
