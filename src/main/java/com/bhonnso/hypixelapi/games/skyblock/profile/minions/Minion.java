package com.bhonnso.hypixelapi.games.skyblock.profile.minions;

public class Minion {

    private boolean[] tiers = new boolean[11];
    private MinionType minionType;

    public Minion(MinionType minionType) {
        this.minionType = minionType;
    }

    public MinionType getMinionType() {
        return minionType;
    }

    /**
     * @param tier Tier 1-11
     */
    public void unlockTier(int tier) {
       this.tiers[tier-1] = true;
    }

    /**
     * @param tier Tier 1-11
     */
    public boolean isUnlocked(int tier) {
        return this.tiers[tier-1];
    }

    @Override
    public String toString() {
        return String.format("{%s, %b %b %b %b %b %b %b %b %b %b %b}", minionType.name(),
                tiers[0], tiers[1], tiers[2], tiers[3], tiers[4],
                tiers[5], tiers[6], tiers[7], tiers[8], tiers[9], tiers[10]);
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Minion))
            return false;
        Minion otherMinion = (Minion) other;
        return otherMinion.minionType == minionType;
    }
}
