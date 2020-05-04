package com.bhonnso.hypixelapi.games.skyblock.profile.minions;

import java.util.Arrays;
import java.util.Optional;

public enum MinionTier {

    I(1),
    II(2),
    III(3),
    IV(4),
    V(5),
    VI(6),
    VII(7),
    VIII(8),
    IX(9),
    X(10),
    XI(11);

    private final int tier;

    MinionTier(int tier) {
        this.tier = tier;
    }

    public int toInt() {
        return tier;
    }

    public String getDisplayName() {
        return name();
    }

    public static Optional<MinionTier> fromInt(int tier) {
        return Arrays.stream(values()).filter(minionTier -> minionTier.tier == tier).findAny();
    }

}
