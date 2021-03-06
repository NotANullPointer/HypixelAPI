package com.bhonnso.hypixelapi.games.skyblock.profile.skills;

public class Skill {

    private int xp;
    private SkillType skillType;
    private SkillLevel skillLevel;

    public Skill(int xp, SkillType skillType) {
        this.xp = xp;
        this.skillType = skillType;
        this.skillLevel = skillType.getLevelType() == SkillType.LevelType.NORMAL ?
                SkillLevelNormal.getLevelFromXp(xp) :
                SkillLevelSpecial.getLevelFromXp(xp);
    }

    public Skill(int level, SkillType skillType, boolean useless) {
        this.skillType = skillType;
        this.skillLevel = skillType.getLevelType() == SkillType.LevelType.NORMAL ?
                SkillLevelNormal.getLevel(level) :
                SkillLevelSpecial.getLevel(level);
        this.xp = skillLevel.getCumulativeXp();
    }

    public String toString() {
        return String.format("%s %s", getSkillType().getDisplayName(), getSkillLevel().getDisplayName());
    }

    public int getXp() {
        return xp;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public SkillLevel getSkillLevel() {
        return skillLevel;
    }
}
