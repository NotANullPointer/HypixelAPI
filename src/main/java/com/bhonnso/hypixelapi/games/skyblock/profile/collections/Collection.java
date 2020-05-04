package com.bhonnso.hypixelapi.games.skyblock.profile.collections;

import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Collection implements Comparable<Collection> {

    private final CollectionType collectionType;
    private final List<CollectionTier> unlocked;
    private final int items;

    private static final Comparator<CollectionTier> sorterCollectionTier = Comparator.comparingInt(t -> t.getTier().toInt());
    private static final Comparator<Collection> collectionComparator = Comparator.comparingInt(c -> c.getItems());

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

    public CollectionTier highestTierUnlocked() {
        return unlocked.stream()
                .max(sorterCollectionTier)
                .orElse(null);
    }

    public int getItems() {
        return items;
    }

    @Override
    public String toString() {
        CollectionTier highestTier = highestTierUnlocked();
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

    @Override
    public int compareTo(Collection collection) {
        Preconditions.checkNotNull(collection);
        return collectionComparator.compare(this, collection);
    }
}