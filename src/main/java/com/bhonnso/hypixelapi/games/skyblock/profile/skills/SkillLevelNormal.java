package com.bhonnso.hypixelapi.games.skyblock.profile.skills;

import com.bhonnso.hypixelapi.APIUtils;

import java.util.Arrays;

public enum SkillLevelNormal implements SkillLevel {

    LV_0(0),
    LV_1(50),
    LV_2(175),
    LV_3(375),
    LV_4(675),
    LV_5(1175),
    LV_6(1925),
    LV_7(2925),
    LV_8(4425),
    LV_9(6425),
    LV_10(9925),
    LV_11(14925),
    LV_12(22425),
    LV_13(32425),
    LV_14(47425),
    LV_15(67425),
    LV_16(97425),
    LV_17(147425),
    LV_18(222425),
    LV_19(322425),
    LV_20(522425),
    LV_21(822425),
    LV_22(1222425),
    LV_23(1722425),
    LV_24(2322425),
    LV_25(3022425),
    LV_26(3822425),
    LV_27(4722425),
    LV_28(5722425),
    LV_29(6822425),
    LV_30(8022425),
    LV_31(9322425),
    LV_32(10722425),
    LV_33(12222425),
    LV_34(13822425),
    LV_35(15522425),
    LV_36(17322425),
    LV_37(19222425),
    LV_38(21222425),
    LV_39(23322425),
    LV_40(25522425),
    LV_41(27822425),
    LV_42(30222425),
    LV_43(32722425),
    LV_44(35322425),
    LV_45(38072425),
    Lv_46(40972425),
    Lv_47(44072425),
    LV_48(47472425),
    LV_49(51172425),
    LV_50(55172425);

    private static int levelBase = 0;
    private final int xp;
    private int level;

    SkillLevelNormal(int xp) {
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

    public SkillLevelNormal previousLevel() {
        if(hasPreviousLevel()) {
            return getLevel(this.level-1);
        }
        return this;
    }

    public SkillLevelNormal nextLevel() {
        if(hasNextLevel()) {
            return getLevel(this.level+1);
        }
        return this;
    }

    public static SkillLevelNormal getLevelFromXp(int xp) {
        SkillLevelNormal res = LV_0;
        for (SkillLevelNormal level : values()) {
            if(level.getCumulativeXp() < xp) {
                res = level;
            } else {
                break;
            }
        }
        return res;
    }

    public static SkillLevelNormal getLevel(int level) {
        return Arrays.stream(values()).filter(skillLevel -> skillLevel.getLevel() == level).findAny().orElse(null);
    }

    public SkillLevelNormal getMaxLevel() {
        return LV_50;
    }

    private void setLevel() {
        this.level = levelBase;
        levelBase += 1;
    }

    public String getDisplayName() {
        return APIUtils.toRoman(level);
    }

}
