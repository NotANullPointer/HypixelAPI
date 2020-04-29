package com.bhonnso.hypixelapi.games.skyblock.profile.skills;

public interface SkillLevel {

    int getLevel();

    int getCumulativeXp();

    int getXp();

    SkillLevel getMaxLevel();

    default boolean isMaxLevel() {
        return getMaxLevel() == this;
    }

    default boolean hasNextLevel() {
        return !(this == getMaxLevel());
    }

    SkillLevel nextLevel();

    boolean hasPreviousLevel();

    SkillLevel previousLevel();

    String getDisplayName();

}
