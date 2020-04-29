package com.bhonnso.hypixelapi.games.skyblock.profile.skills;

import com.bhonnso.hypixelapi.APIUtils;

import java.util.Arrays;

public enum SkillLevelSpecial implements SkillLevel {

    LV_0(0),
    LV_1(50),
    LV_2(150),
    LV_3(275),
    LV_4(435),
    LV_5(635),
    LV_6(885),
    LV_7(1200),
    LV_8(1600),
    LV_9(2100),
    LV_10(2725),
    LV_11(3510),
    LV_12(4510),
    LV_13(5760),
    LV_14(7325),
    LV_15(9325),
    LV_16(11825),
    LV_17(14950),
    LV_18(18950),
    LV_19(23950),
    LV_20(30200),
    LV_21(38050),
    LV_22(47850),
    LV_23(60100),
    LV_24(75400);

    private static int levelBase = 0;
    private final int xp;
    private int level;

    SkillLevelSpecial(int xp) {
        this.xp = xp;
        setLevel();
    }

    public int getLevel() {
        return level;
    }

    public int getCumulativeXp() {
        return xp;
    }

    public boolean hasPreviousLevel() {
        return level != 0;
    }

    public int getXp() {
        return getCumulativeXp() - previousLevel().getCumulativeXp();
    }

    public SkillLevelSpecial previousLevel() {
        if(hasPreviousLevel()) {
            return getLevel(this.level-1);
        }
        return this;
    }

    public SkillLevelSpecial nextLevel() {
        if(hasNextLevel()) {
            return getLevel(this.level+1);
        }
        return this;
    }

    public static SkillLevelSpecial getLevelFromXp(int xp) {
        SkillLevelSpecial res = LV_0;
        for (SkillLevelSpecial level : values()) {
            if(level.getCumulativeXp() < xp) {
                res = level;
            } else {
                break;
            }
        }
        return res;
    }

    public static SkillLevelSpecial getLevel(int level) {
        return Arrays.stream(values()).filter(skillLevel -> skillLevel.getLevel() == level).findAny().orElse(null);
    }

    public SkillLevelSpecial getMaxLevel() {
        return LV_24;
    }

    private void setLevel() {
        this.level = levelBase;
        levelBase += 1;
    }

    public String getDisplayName() {
        return APIUtils.toRoman(level);
    }

}
