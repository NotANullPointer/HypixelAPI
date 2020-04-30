package com.bhonnso.hypixelapi.games.skyblock.profile.collections;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionTier extends com.bhonnso.hypixelapi.JSONObject {

    private final Tier tier;
    private final int required;
    private final List<String> unlocks;
    private CollectionType collectionType;

    public CollectionTier(JSONObject data) {
        super(data);
        this.tier = Tier.get(data.getInt("tier"));
        this.required = data.getInt("amountRequired");
        this.unlocks = data.getJSONArray("unlocks").toList().stream().map(Object::toString).collect(Collectors.toList());
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public Tier getTier() {
        return tier;
    }

    public int getRequired() {
        return required;
    }

    public List<String> getUnlocks() {
        return unlocks;
    }

    public String toString() {
        return String.format("%s %s %s", collectionType.getDisplayName(), tier.getDisplayName(), unlocks.toString());
    }

    public enum Tier {

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
        XI(11),
        XII(12),
        XIII(13),
        XIV(14),
        XV(15);

        private final int tier;

        Tier(int tier) {
            this.tier = tier;
        }

        public String getDisplayName() {
            return name();
        }

        public int getTier() {
            return tier;
        }

        public static Tier get(int tier) {
            return Arrays.stream(values()).filter(t -> tier == t.tier).findAny().orElse(null);
        }

    }

}
