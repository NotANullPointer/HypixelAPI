package com.bhonnso.hypixelapi.games.skyblock.profile.skills;

public interface SkillLevel {

    int getLevel();

    int getCumulativeXp();

    int getXp();

    SkillLevel getMaxLevel();

    boolean hasNextLevel();

    SkillLevel nextLevel();

    boolean hasPreviousLevel();

    SkillLevel previousLevel();

}
