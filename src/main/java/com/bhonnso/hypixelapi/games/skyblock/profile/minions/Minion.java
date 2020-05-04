package com.bhonnso.hypixelapi.games.skyblock.profile.minions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Minion {

    private MinionType minionType;
    private List<MinionTier> unlocked;

    public Minion(MinionType minionType, List<MinionTier> unlocked) {
        this.minionType = minionType;
        this.unlocked = unlocked;
        unlocked.sort(Comparator.comparingInt(MinionTier::toInt));
    }

    public MinionType getMinionType() {
        return minionType;
    }

    public List<MinionTier> getUnlockedTiers() {
        return unlocked;
    }

    public List<MinionTier> getLockedTiers() {
        return Arrays.stream(MinionTier.values())
                .filter(tier -> !isUnlocked(tier))
                .sorted(Comparator.comparingInt(MinionTier::toInt))
                .collect(Collectors.toList());
    }

    public boolean isUnlocked(MinionTier minionTier) {
        return this.unlocked.contains(minionTier);
    }

    @Override
    public String toString() {
        return String.format("%s %s", minionType.getDisplayName(), unlocked.toString());
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Minion))
            return false;
        Minion otherMinion = (Minion) other;
        return otherMinion.minionType == minionType;
    }
}
