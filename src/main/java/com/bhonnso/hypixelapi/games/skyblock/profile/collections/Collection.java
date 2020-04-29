package com.bhonnso.hypixelapi.games.skyblock.profile.collections;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Collection {

    private CollectionType collectionType;
    private List<CollectionTier> unlocked;
    private int items;

    private static Comparator<CollectionTier> sorterCollectionTier = Comparator.comparingInt(t -> t.getTier().getTier());

    private static Comparator<CollectionTier.Tier> sorterTier = Comparator.comparingInt(CollectionTier.Tier::getTier);

    public Collection(CollectionType collectionType, List<CollectionTier> unlocked, int items) {
        this.collectionType = collectionType;
        this.unlocked = unlocked;
        this.items = items;
        unlocked.sort(sorterCollectionTier);
    }


    public CollectionType getCollectionType() {
        return collectionType;
    }

    public List<CollectionTier> getUnlockedTiers() {
        return unlocked;
    }

    public List<CollectionTier> getLockedTiers() {
        return collectionType.getTiers().stream()
                .filter(tier -> !isUnlocked(tier))
                .sorted(sorterCollectionTier)
                .collect(Collectors.toList());
    }

    public boolean isUnlocked(CollectionTier collectionTier) {
        return this.unlocked.contains(collectionTier);
    }

    public CollectionTier highestTier() {
        return unlocked.stream()
                .max(sorterCollectionTier)
                .orElse(null);
    }

    @Override
    public String toString() {
        CollectionTier highestTier = highestTier();
        return String.format("%s %s (%d)", collectionType.getDisplayName(),
                highestTier == null ? "(Not unlocked)" : highestTier.getTier().getDisplayName(),
                items);
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Collection))
            return false;
        Collection otherCollection = (Collection) other;
        return otherCollection.collectionType == collectionType;
    }
}